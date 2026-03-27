package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.MapAtomicSameKey;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * W3C XPath/XQuery {@code map:merge}:
 *
 * <p>
 * Implements the FO 3.1 conceptual semantics:
 *
 * <pre>
 * let $duplicates-handler := map {
 *   "use-first":   function($a, $b) {$a},
 *   "use-last":    function($a, $b) {$b},
 *   "combine":     function($a, $b) {$a, $b},
 *   "reject":      function($a, $b) {fn:error($FOJS0003)},
 *   "use-any":     function($a, $b) { ... implementation-dependent ... }
 * },
 * $combine-maps := function($A as map(*), $B as map(*), $deduplicator as function(*)) {
 *   fn:fold-left(map:keys($B), $A, function($z, $k){
 *     if (map:contains($z, $k))
 *     then map:put($z, $k, $deduplicator($z($k), $B($k)))
 *     else map:put($z, $k, $B($k))
 *   })
 * }
 * return fn:fold-left($MAPS, map{},
 *   $combine-maps(?, ?, $duplicates-handler(($OPTIONS?duplicates, "use-first")[1])))
 * </pre>
 *
 * <p>
 * This implementation stays local-only and mirrors that behaviour using the existing MapItem
 * representation and {@code MapAtomicSameKey} key equality via {@code MapItem.getSequenceByKey()}.
 */
public class MapMergeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapsIterator;
    private final RuntimeIterator optionsIterator; // may be null for arity-1

    public MapMergeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() == 1) {
            this.mapsIterator = arguments.get(0);
            this.optionsIterator = null;
        } else if (arguments.size() == 2) {
            this.mapsIterator = arguments.get(0);
            this.optionsIterator = arguments.get(1);
        } else {
            throw new OurBadException("map:merge must have one or two arguments.");
        }
    }

    private enum DuplicatePolicy {
        USE_FIRST,
        USE_LAST,
        USE_ANY,
        COMBINE,
        REJECT
    }

    private static class AccumulatorEntry {
        private final Item key;
        private List<Item> valueSequence;

        private AccumulatorEntry(Item key, List<Item> valueSequence) {
            this.key = key;
            this.valueSequence = valueSequence;
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ExceptionMetadata metadata = getMetadata();

        // 1. Materialize $maps as item()* and collect only map items (FO says maps(*)*).
        List<Item> allMaps = new ArrayList<>();
        this.mapsIterator.materialize(context, allMaps);
        List<Item> maps = new ArrayList<>();
        for (Item i : allMaps) {
            if (i.isMap()) {
                maps.add(i);
            } else {
                // XPTY0004 for non-map members in $maps, per map:* convention.
                throw new UnexpectedTypeException(
                        "map:merge expects a sequence of map(*) items as first argument [err:XPTY0004].",
                        metadata
                );
            }
        }

        // Empty input -> empty map.
        if (maps.isEmpty()) {
            return ItemFactory.getInstance()
                .createObjectOrMapItem(new ArrayList<>(), new ArrayList<>(), metadata, false);
        }

        // 2. Resolve options and duplicates policy.
        DuplicatePolicy policy = DuplicatePolicy.USE_FIRST; // spec default
        if (this.optionsIterator != null) {
            List<Item> optionsSeq = this.optionsIterator.materialize(context);
            if (optionsSeq.isEmpty()) {
                // map-merge-026: second argument must not be empty -> XPTY0004.
                throw new UnexpectedTypeException(
                        "map:merge options argument must be a single map, not the empty sequence [err:XPTY0004].",
                        metadata
                );
            }
            if (optionsSeq.size() != 1 || !optionsSeq.get(0).isMap()) {
                throw new UnexpectedTypeException(
                        "map:merge options argument must be exactly one map(*) [err:XPTY0004].",
                        metadata
                );
            }
            Item optionsMap = optionsSeq.get(0);
            // $OPTIONS?duplicates, fallback to \"use-first\".
            List<Item> duplicatesSeq = optionsMap.getSequenceByKey("duplicates");
            if (duplicatesSeq != null && !duplicatesSeq.isEmpty()) {
                Item d = duplicatesSeq.get(0);
                if (!d.isString()) {
                    // FOJS0005: invalid option value for a recognized key.
                    throw new UnexpectedTypeException(
                            "Invalid value for map:merge option duplicates (expected xs:string) [err:FOJS0005].",
                            metadata
                    );
                }
                String v = d.getStringValue();
                if ("use-first".equals(v)) {
                    policy = DuplicatePolicy.USE_FIRST;
                } else if ("use-last".equals(v)) {
                    policy = DuplicatePolicy.USE_LAST;
                } else if ("use-any".equals(v)) {
                    policy = DuplicatePolicy.USE_ANY;
                } else if ("combine".equals(v)) {
                    policy = DuplicatePolicy.COMBINE;
                } else if ("reject".equals(v)) {
                    policy = DuplicatePolicy.REJECT;
                } else {
                    // FOJS0005: invalid value for a recognized key.
                    throw new UnexpectedTypeException(
                            "Invalid value for map:merge option duplicates: " + v + " [err:FOJS0005].",
                            metadata
                    );
                }
            }
        }

        // 3. Implement fold-left over maps with a hash-map accumulator.
        // We bucket keys by broad op:same-key families and resolve exact duplicates within each bucket
        // using MapAtomicSameKey.sameKey(...).
        Map<String, List<AccumulatorEntry>> accumulator = new HashMap<>();

        for (Item mapItem : maps) {
            // For each map $B: fold over its keys.
            List<Item> bKeys = mapItem.getItemKeys();
            for (Item bKey : bKeys) {
                List<Item> bSeq = mapItem.getSequenceByKey(bKey);
                if (bSeq == null) {
                    bSeq = new ArrayList<>();
                }

                String bucketId = getBucketId(bKey);
                List<AccumulatorEntry> bucket = accumulator.computeIfAbsent(bucketId, k -> new ArrayList<>());
                AccumulatorEntry existing = findExistingEntry(bucket, bKey);

                if (existing == null) {
                    // No duplicate -> behave like map:put ($A, $k, $B($k)).
                    bucket.add(new AccumulatorEntry(bKey, new ArrayList<>(bSeq)));
                } else {
                    // Duplicate key: apply duplicates handler.
                    List<Item> aSeq = existing.valueSequence;
                    switch (policy) {
                        case USE_FIRST:
                            // Keep existing A(k); ignore B(k).
                            break;
                        case USE_LAST:
                            // Replace with B(k).
                            existing.valueSequence = new ArrayList<>(bSeq);
                            break;
                        case USE_ANY:
                            // Implementation choice; we simply keep the existing A(k).
                            break;
                        case COMBINE:
                            // Concatenate in map order: A(k), then B(k).
                            List<Item> combined = new ArrayList<>(aSeq.size() + bSeq.size());
                            combined.addAll(aSeq);
                            combined.addAll(bSeq);
                            existing.valueSequence = combined;
                            break;
                        case REJECT:
                            // FOJS0003: duplicates not allowed. We re-use UnexpectedTypeException to
                            // surface the correct FOJS0003 code through the error mapping layer.
                            throw new UnexpectedTypeException(
                                    "map:merge encountered duplicate map keys with duplicates=\"reject\" [err:FOJS0003].",
                                    metadata
                            );
                        default:
                            throw new OurBadException("Unexpected duplicates policy in map:merge.");
                    }
                }
            }
        }

        // 4. Build final MapItem from accumulator via map overload to avoid
        // duplicate-key verification in MapItem(List, List, ...).
        Map<Item, List<Item>> finalKeyValuePairs = new HashMap<>();
        for (List<AccumulatorEntry> bucket : accumulator.values()) {
            for (AccumulatorEntry entry : bucket) {
                finalKeyValuePairs.put(
                    entry.key,
                    entry.valueSequence == null ? new ArrayList<>() : entry.valueSequence
                );
            }
        }

        return ItemFactory.getInstance()
            .createMapItem(finalKeyValuePairs, metadata, false);
    }

    /**
     * Broad bucket for likely op:same-key candidates.
     * We still always verify using MapAtomicSameKey.sameKey(...).
     */
    private String getBucketId(Item key) {
        if (key.isString() || key.isAnyURI() || key.isUntypedAtomic()) {
            return "string-like";
        }
        if (key.isNumeric()) {
            return "numeric";
        }
        if (
            key.isDate()
                || key.isTime()
                || key.isDateTime()
                || key.isGYear()
                || key.isGYearMonth()
                || key.isGMonth()
                || key.isGMonthDay()
                || key.isGDay()
        ) {
            return "gregorian";
        }
        if (key.isBoolean() || key.isHexBinary() || key.isBase64Binary()) {
            return "misc-deep-equal";
        }
        if (key.isDuration() || key.isYearMonthDuration() || key.isDayTimeDuration()) {
            return "misc-deep-equal";
        }
        return "other";
    }

    private AccumulatorEntry findExistingEntry(List<AccumulatorEntry> bucket, Item lookup) {
        for (AccumulatorEntry entry : bucket) {
            if (MapAtomicSameKey.sameKey(entry.key, lookup)) {
                return entry;
            }
        }
        return null;
    }
}

