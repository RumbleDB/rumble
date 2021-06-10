/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.types;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SequenceType implements Serializable {

    private static final long serialVersionUID = 1L;
    private ItemType itemType;
    private Arity arity;

    public final static SequenceType ITEM_STAR = new SequenceType(
            BuiltinTypesCatalogue.item,
            Arity.ZeroOrMore
    );

    public final static SequenceType EMPTY_SEQUENCE = new SequenceType();


    public SequenceType(ItemType itemType, Arity arity) {
        if (arity == Arity.Zero) {
            this.itemType = BuiltinTypesCatalogue.item;
            this.arity = Arity.Zero;
            return;
        }
        this.itemType = itemType;
        this.arity = arity;
        if (this.itemType == null) {
            System.err.println(
                "[WARNING] Missing item type in incomplete sequence type "
                    + this.arity
                    + ", defaulting to item. Please let us know as we would like to look into this!"
            );
            this.itemType = BuiltinTypesCatalogue.item;
        }
        if (this.arity == null) {
            System.err.println(
                "[WARNING] Missing arity in incomplete sequence type "
                    + this.itemType
                    + ", defaulting to *. Please let us know as we would like to look into this!"
            );
            this.arity = Arity.ZeroOrMore;
        }
    }

    public SequenceType(ItemType itemType) {
        this.itemType = itemType;
        this.arity = Arity.One;
        if (this.itemType == null) {
            throw new OurBadException("Missing item type in incomplete sequence type " + this.arity);
        }
    }

    private SequenceType() {
        this.itemType = BuiltinTypesCatalogue.item;
        this.arity = Arity.Zero;
    }

    public boolean isResolved() {
        if (isEmptySequence()) {
            return true;
        }
        return this.itemType.isResolved();
    }

    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (isEmptySequence()) {
            return;
        }
        this.itemType.resolve(context, metadata);
    }

    public boolean isEmptySequence() {
        return this.arity == Arity.Zero;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public Arity getArity() {
        return this.arity;
    }

    public boolean isSubtypeOf(SequenceType superType) {
        if (isEmptySequence()) {
            return superType.arity == Arity.OneOrZero || superType.arity == Arity.ZeroOrMore;
        }
        return this.itemType.isSubtypeOf(superType.getItemType())
            &&
            this.isAritySubtypeOf(superType.arity);
    }

    // keep in consideration also automatic promotion of integer > decimal > double and anyURI > string
    public boolean isSubtypeOfOrCanBePromotedTo(SequenceType superType) {
        if (isEmptySequence()) {
            return superType.arity == Arity.OneOrZero || superType.arity == Arity.ZeroOrMore;
        }
        return this.isAritySubtypeOf(superType.arity)
            && (this.itemType.isSubtypeOf(superType.getItemType())
                ||
                (this.itemType.canBePromotedTo(superType.itemType)));
    }

    // check if the arity of a sequence type is subtype of another arity, assume [this] is a non-empty sequence
    // TODO: consider removing it
    public boolean isAritySubtypeOf(Arity superArity) {
        return this.arity.isSubtypeOf(superArity);
    }

    public boolean hasEffectiveBooleanValue() {
        if (isEmptySequence()) {
            return true;
        } else if (this.itemType.isSubtypeOf(BuiltinTypesCatalogue.JSONItem)) {
            return true;
        } else if (
            (this.arity == Arity.One || this.arity == Arity.OneOrZero)
                && (this.itemType.isNumeric()
                    ||
                    this.itemType.equals(BuiltinTypesCatalogue.stringItem)
                    ||
                    this.itemType.equals(BuiltinTypesCatalogue.anyURIItem)
                    ||
                    this.itemType.equals(BuiltinTypesCatalogue.nullItem)
                    ||
                    this.itemType.equals(BuiltinTypesCatalogue.booleanItem))
        ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasOverlapWith(SequenceType other) {
        // types overlap if both itemType and Arity overlap, we also need to take care of empty sequence
        if (isEmptySequence()) {
            return other.isEmptySequence()
                || other.getArity() == Arity.OneOrZero
                || other.getArity() == Arity.ZeroOrMore;
        }
        if (other.isEmptySequence()) {
            return this.getArity() == Arity.OneOrZero || this.getArity() == Arity.ZeroOrMore;
        }
        // All arities overlap between each other
        return this.getItemType().isSubtypeOf(other.getItemType())
            || other.getItemType().isSubtypeOf(this.getItemType());
    }

    public SequenceType leastCommonSupertypeWith(SequenceType other) {
        if (isEmptySequence()) {
            if (other.isEmptySequence()) {
                return this;
            } else {
                Arity resultingArity = other.getArity();
                if (resultingArity == Arity.One) {
                    resultingArity = Arity.OneOrZero;
                } else if (resultingArity == Arity.OneOrMore) {
                    resultingArity = Arity.ZeroOrMore;
                }
                return new SequenceType(other.itemType, resultingArity);
            }
        }
        if (other.isEmptySequence()) {
            Arity resultingArity = this.getArity();
            if (resultingArity == Arity.One) {
                resultingArity = Arity.OneOrZero;
            } else if (resultingArity == Arity.OneOrMore) {
                resultingArity = Arity.ZeroOrMore;
            }
            return new SequenceType(this.itemType, resultingArity);
        }

        ItemType itemSupertype = this.getItemType().findLeastCommonSuperTypeWith(other.getItemType());
        Arity aritySuperType = Arity.ZeroOrMore;
        if (this.isAritySubtypeOf(other.getArity())) {
            aritySuperType = other.getArity();
        } else if (other.isAritySubtypeOf(this.getArity())) {
            aritySuperType = this.getArity();
        }
        // no need additional check because the only disjointed arity are ? and +, which least common supertype is *
        return new SequenceType(itemSupertype, aritySuperType);
    }

    // increment arity of a sequence type from ? to * and from 1 to +, leave others arity or sequence types untouched
    public SequenceType incrementArity() {
        if (!isEmptySequence()) {
            if (this.arity == Arity.One) {
                return new SequenceType(this.getItemType(), Arity.OneOrMore);
            } else if (this.arity == Arity.OneOrZero) {
                return new SequenceType(this.getItemType(), Arity.ZeroOrMore);
            }
        }
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SequenceType)) {
            return false;
        }
        SequenceType sequenceType = (SequenceType) other;
        if (isEmptySequence()) {
            return sequenceType.isEmptySequence();
        }
        if (sequenceType.isEmptySequence()) {
            return false;
        }
        return this.getItemType().equals(sequenceType.getItemType()) && this.getArity().equals(sequenceType.getArity());
    }

    public enum Arity {
        OneOrZero {
            @Override
            public String getSymbol() {
                return "?";
            }
        },
        OneOrMore {
            @Override
            public String getSymbol() {
                return "+";
            }
        },
        ZeroOrMore {
            @Override
            public String getSymbol() {
                return "*";
            }
        },
        One {
            @Override
            public String getSymbol() {
                return "";
            }
        },
        Zero {
            @Override
            public String getSymbol() {
                return "<void>";
            }
        };

        public abstract String getSymbol();

        public boolean isSubtypeOf(Arity superArity) {
            if (superArity == Zero) {
                return this == Arity.Zero;
            }
            if (this == Zero) {
                return this == Arity.ZeroOrMore || this == Arity.OneOrZero;
            }
            if (superArity == Arity.ZeroOrMore || superArity == this)
                return true;
            else
                return this == Arity.One;
        }

        public Arity multiplyWith(Arity other) {
            if (this == Zero || other == Zero) {
                return Zero;
            }
            if (this == One && other == One) {
                return One;
            } else if (this.isSubtypeOf(OneOrZero) && other.isSubtypeOf(OneOrZero)) {
                return OneOrZero;
            } else if (this.isSubtypeOf(OneOrMore) && other.isSubtypeOf(OneOrMore)) {
                return OneOrMore;
            } else {
                return ZeroOrMore;
            }
        }

    }

    @Override
    public String toString() {
        if (isEmptySequence()) {
            return "()";
        }
        StringBuilder result = new StringBuilder();
        result.append(this.getItemType().toString());
        result.append(this.arity.getSymbol());
        return result.toString();
    }

    private static final Map<String, SequenceType> sequenceTypes;

    static {
        sequenceTypes = new HashMap<>();
        sequenceTypes.put("item", new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.One));
        sequenceTypes.put("item?", new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("item*", new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore));
        sequenceTypes.put("item+", new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrMore));

        sequenceTypes.put("object", new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.One));
        sequenceTypes.put("object+", new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.OneOrMore));
        sequenceTypes.put("object*", new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put(
            "json-item*",
            new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.ZeroOrMore)
        );

        sequenceTypes.put("array?", new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("array*", new SequenceType(BuiltinTypesCatalogue.arrayItem, Arity.ZeroOrMore));

        sequenceTypes.put("atomic", new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.One));
        sequenceTypes.put("atomic?", new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("atomic*", new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("string", new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.One));
        sequenceTypes.put("string?", new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("string*", new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("integer", new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One));
        sequenceTypes.put(
            "integer?",
            new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero)
        );
        sequenceTypes.put(
            "integer*",
            new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.ZeroOrMore)
        );

        sequenceTypes.put(
            "numeric?",
            new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceType.Arity.OneOrZero)
        );
        sequenceTypes.put(
            "numeric",
            new SequenceType(BuiltinTypesCatalogue.numericItem)
        );
        sequenceTypes.put(
            "numeric+",
            new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.OneOrMore)
        );
        sequenceTypes.put(
            "numeric*",
            new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.ZeroOrMore)
        );

        sequenceTypes.put(
            "decimal?",
            new SequenceType(BuiltinTypesCatalogue.decimalItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("double", new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.One));
        sequenceTypes.put("double?", new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("float", new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.One));
        sequenceTypes.put("float?", new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("boolean", new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One));
        sequenceTypes.put(
            "boolean?",
            new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "duration?",
            new SequenceType(BuiltinTypesCatalogue.durationItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "yearMonthDuration?",
            new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "dayTimeDuration?",
            new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "dateTime?",
            new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("date?", new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("time?", new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("anyURI", new SequenceType(BuiltinTypesCatalogue.anyURIItem));
        sequenceTypes.put("anyURI?", new SequenceType(BuiltinTypesCatalogue.anyURIItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put(
            "hexBinary?",
            new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "base64Binary?",
            new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("null?", new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put(
            "function(object*, object) as object*",
            new SequenceType(
                    ItemTypeFactory.createFunctionItemType(
                        new FunctionSignature(
                                Arrays.asList(
                                    new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                    new SequenceType(BuiltinTypesCatalogue.objectItem)
                                ),
                                new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore)
                        )
                    )
            )
        );
        sequenceTypes.put(
            "function(object*, object) as function(object*, object) as object*",
            new SequenceType(
                    ItemTypeFactory.createFunctionItemType(
                        new FunctionSignature(
                                Arrays.asList(
                                    new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                    new SequenceType(BuiltinTypesCatalogue.objectItem)
                                ),
                                new SequenceType(
                                        ItemTypeFactory.createFunctionItemType(
                                            new FunctionSignature(
                                                    Arrays.asList(
                                                        new SequenceType(
                                                                BuiltinTypesCatalogue.objectItem,
                                                                SequenceType.Arity.ZeroOrMore
                                                        ),
                                                        new SequenceType(BuiltinTypesCatalogue.objectItem)
                                                    ),
                                                    new SequenceType(
                                                            BuiltinTypesCatalogue.objectItem,
                                                            SequenceType.Arity.ZeroOrMore
                                                    )
                                            )
                                        )
                                )
                        )
                    )
            )
        );
    }

    public static SequenceType createSequenceType(String userFriendlyName) {
        if (sequenceTypes.containsKey(userFriendlyName)) {
            return sequenceTypes.get(userFriendlyName);
        }
        throw new OurBadException("Unrecognized type: " + userFriendlyName);
    }



}
