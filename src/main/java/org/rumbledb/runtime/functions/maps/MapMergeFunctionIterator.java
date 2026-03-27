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
import java.math.BigDecimal;

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

    /**
     * Hash/equals wrapper compatible with {@code op:same-key}.
     *
     * <p>
     * Requirement: if {@code MapAtomicSameKey.sameKey(a, b)} then {@code hash(a) == hash(b)}.
     * Collisions are fine; they only affect performance, not correctness.
     */
    private static final class SameKeyWrapper {
        private final Item key;
        private final int hash;

        private SameKeyWrapper(Item key) {
            this.key = key;
            // Must satisfy: if op:same-key(a,b) then hash(a) == hash(b), otherwise HashMap may miss duplicates.
            // Keep hashing minimal and focused on common key families (string-like + numeric).
            if (key == null || !key.isAtomic()) {
                this.hash = 0;
            } else if (key.isString() || key.isAnyURI() || key.isUntypedAtomic()) {
                String s = key.getStringValue();
                this.hash = s == null ? 0 : s.hashCode();
            } else if (key.isNumeric()) {
                this.hash = numericSameKeyHash(key);
            } else if (
                key.isDate()
                    || key.isTime()
                    || key.isDateTime()
                    || key.isGYear()
                    || key.isGYearMonth()
                    || key.isGMonth()
                    || key.isGMonthDay()
                    || key.isGDay()
            ) {
                this.hash = 0x47; // 'G' (gregorian family)
            } else if (
                key.isBoolean()
                    || key.isHexBinary()
                    || key.isBase64Binary()
                    || key.isDuration()
                    || key.isYearMonthDuration()
                    || key.isDayTimeDuration()
            ) {
                this.hash = 0x4D; // 'M' (misc deep-equal family)
            } else {
                this.hash = 0x4F; // 'O' (other)
            }
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SameKeyWrapper)) {
                return false;
            }
            SameKeyWrapper other = (SameKeyWrapper) o;
            return MapAtomicSameKey.sameKey(this.key, other.key);
        }

        private static int numericSameKeyHash(Item k) {
            // Mirror MapAtomicSameKey.sameNumericKey special cases and decimal compare semantics.
            if (
                (k.isFloat() && Float.isNaN(k.getFloatValue()))
                    || (k.isDouble() && Double.isNaN(k.getDoubleValue()))
            ) {
                return 0x4E614E; // 'NaN'
            }
            if (k.isDouble()) {
                double d = k.getDoubleValue();
                if (d == Double.POSITIVE_INFINITY) {
                    return 0x2B494E46; // '+INF'
                }
                if (d == Double.NEGATIVE_INFINITY) {
                    return 0x2D494E46; // '-INF'
                }
            }
            if (k.isFloat()) {
                float f = k.getFloatValue();
                if (f == Float.POSITIVE_INFINITY) {
                    return 0x2B494E46; // '+INF'
                }
                if (f == Float.NEGATIVE_INFINITY) {
                    return 0x2D494E46; // '-INF'
                }
            }
            try {
                BigDecimal d = toExactDecimalKeyForHash(k);
                if (d.signum() == 0) {
                    return 0; // all zeros
                }
                BigDecimal normalized = d.stripTrailingZeros();
                return normalized.toPlainString().hashCode();
            } catch (Exception e) {
                return 1;
            }
        }

        private static BigDecimal toExactDecimalKeyForHash(Item k) {
            if (k.isDecimal()) {
                return k.getDecimalValue();
            }
            if (k.isInteger()) {
                return new BigDecimal(k.getIntegerValue());
            }
            if (k.isInt()) {
                return BigDecimal.valueOf(k.getIntValue());
            }
            if (k.isDouble()) {
                return BigDecimal.valueOf(k.getDoubleValue());
            }
            if (k.isFloat()) {
                return new BigDecimal(Float.toString(k.getFloatValue()));
            }
            return k.castToDecimalValue();
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ExceptionMetadata metadata = getMetadata();

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

        // 3. Implement fold-left over maps with a hashed same-key accumulator.
        // Streaming consumption avoids materializing huge sequences of maps.
        Map<SameKeyWrapper, List<Item>> accumulator = new HashMap<>();
        boolean sawAnyMap = false;
        this.mapsIterator.open(context);
        try {
            while (this.mapsIterator.hasNext()) {
                Item mapItem = this.mapsIterator.next();
                if (!mapItem.isMap()) {
                    throw new UnexpectedTypeException(
                            "map:merge expects a sequence of map(*) items as first argument [err:XPTY0004].",
                            metadata
                    );
                }
                sawAnyMap = true;
                List<Item> bKeys = mapItem.getItemKeys();
                for (Item bKey : bKeys) {
                    List<Item> bSeq = mapItem.getSequenceByKey(bKey);
                    if (bSeq == null) {
                        bSeq = new ArrayList<>();
                    }

                    SameKeyWrapper lookup = new SameKeyWrapper(bKey);
                    List<Item> existingSeq = accumulator.get(lookup);
                    if (existingSeq == null) {
                        accumulator.put(lookup, new ArrayList<>(bSeq));
                        continue;
                    }

                    switch (policy) {
                        case USE_FIRST:
                            break;
                        case USE_LAST:
                            accumulator.put(lookup, new ArrayList<>(bSeq));
                            break;
                        case USE_ANY:
                            break;
                        case COMBINE:
                            List<Item> combined = new ArrayList<>(existingSeq.size() + bSeq.size());
                            combined.addAll(existingSeq);
                            combined.addAll(bSeq);
                            accumulator.put(lookup, combined);
                            break;
                        case REJECT:
                            throw new UnexpectedTypeException(
                                    "map:merge encountered duplicate map keys with duplicates=\"reject\" [err:FOJS0003].",
                                    metadata
                            );
                        default:
                            throw new OurBadException("Unexpected duplicates policy in map:merge.");
                    }
                }
            }
        } finally {
            this.mapsIterator.close();
        }

        // Empty input -> empty map.
        if (!sawAnyMap) {
            return ItemFactory.getInstance()
                .createMapItem(new HashMap<>(), metadata, false);
        }

        // 4. Build final MapItem from accumulator via map overload to avoid duplicate-key verification.
        Map<Item, List<Item>> finalKeyValuePairs = new HashMap<>();
        for (Map.Entry<SameKeyWrapper, List<Item>> entry : accumulator.entrySet()) {
            finalKeyValuePairs.put(
                entry.getKey().key,
                entry.getValue() == null ? new ArrayList<>() : entry.getValue()
            );
        }

        return ItemFactory.getInstance()
            .createMapItem(finalKeyValuePairs, metadata, false);
    }
}

