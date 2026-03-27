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

import org.apache.log4j.LogManager;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
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

    public final static SequenceType OBJECTS = new SequenceType(
            BuiltinTypesCatalogue.objectItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType FUNCTION = new SequenceType(
            BuiltinTypesCatalogue.anyFunctionItem,
            Arity.One
    );

    public final static SequenceType ITEM = new SequenceType(
            BuiltinTypesCatalogue.item,
            Arity.One
    );

    public final static SequenceType INTEGER = new SequenceType(
            BuiltinTypesCatalogue.integerItem,
            Arity.One
    );

    public final static SequenceType DECIMAL = new SequenceType(
            BuiltinTypesCatalogue.decimalItem,
            Arity.One
    );

    public final static SequenceType DOUBLE = new SequenceType(
            BuiltinTypesCatalogue.doubleItem,
            Arity.One
    );

    public final static SequenceType FLOAT = new SequenceType(
            BuiltinTypesCatalogue.floatItem,
            Arity.One
    );

    public final static SequenceType INT = new SequenceType(
            BuiltinTypesCatalogue.intItem,
            Arity.One
    );

    public final static SequenceType STRING = new SequenceType(
            BuiltinTypesCatalogue.stringItem,
            Arity.One
    );

    public final static SequenceType ANYURI = new SequenceType(
            BuiltinTypesCatalogue.anyURIItem,
            Arity.One
    );

    public final static SequenceType BOOLEAN = new SequenceType(
            BuiltinTypesCatalogue.booleanItem,
            Arity.One
    );

    public final static SequenceType INTEGER_STAR = new SequenceType(
            BuiltinTypesCatalogue.integerItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType DECIMAL_STAR = new SequenceType(
            BuiltinTypesCatalogue.decimalItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType DOUBLE_STAR = new SequenceType(
            BuiltinTypesCatalogue.doubleItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType FLOAT_STAR = new SequenceType(
            BuiltinTypesCatalogue.floatItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType INT_STAR = new SequenceType(
            BuiltinTypesCatalogue.intItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType STRING_STAR = new SequenceType(
            BuiltinTypesCatalogue.stringItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType ANYURI_STAR = new SequenceType(
            BuiltinTypesCatalogue.anyURIItem,
            Arity.ZeroOrMore
    );

    public final static SequenceType INTEGER_PLUS = new SequenceType(
            BuiltinTypesCatalogue.integerItem,
            Arity.OneOrMore
    );

    public final static SequenceType DECIMAL_PLUS = new SequenceType(
            BuiltinTypesCatalogue.decimalItem,
            Arity.OneOrMore
    );

    public final static SequenceType DOUBLE_PLUS = new SequenceType(
            BuiltinTypesCatalogue.doubleItem,
            Arity.OneOrMore
    );

    public final static SequenceType FLOAT_PLUS = new SequenceType(
            BuiltinTypesCatalogue.floatItem,
            Arity.OneOrMore
    );

    public final static SequenceType INT_PLUS = new SequenceType(
            BuiltinTypesCatalogue.intItem,
            Arity.OneOrMore
    );

    public final static SequenceType STRING_PLUS = new SequenceType(
            BuiltinTypesCatalogue.stringItem,
            Arity.OneOrMore
    );

    public final static SequenceType ANYURI_PLUS = new SequenceType(
            BuiltinTypesCatalogue.anyURIItem,
            Arity.OneOrMore
    );

    public final static SequenceType INTEGER_QM = new SequenceType(
            BuiltinTypesCatalogue.integerItem,
            Arity.OneOrZero
    );

    public final static SequenceType DECIMAL_QM = new SequenceType(
            BuiltinTypesCatalogue.decimalItem,
            Arity.OneOrZero
    );

    public final static SequenceType DOUBLE_QM = new SequenceType(
            BuiltinTypesCatalogue.doubleItem,
            Arity.OneOrZero
    );

    public final static SequenceType FLOAT_QM = new SequenceType(
            BuiltinTypesCatalogue.floatItem,
            Arity.OneOrZero
    );

    public final static SequenceType INT_QM = new SequenceType(
            BuiltinTypesCatalogue.intItem,
            Arity.OneOrZero
    );

    public final static SequenceType STRING_QM = new SequenceType(
            BuiltinTypesCatalogue.stringItem,
            Arity.OneOrZero
    );

    public final static SequenceType ANYURI_QM = new SequenceType(
            BuiltinTypesCatalogue.anyURIItem,
            Arity.OneOrZero
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
            LogManager.getLogger("SequenceType")
                .warn(
                    "Missing item type in incomplete sequence type "
                        + this.arity
                        + ", defaulting to item. Please let us know as we would like to look into this!"
                );
            this.itemType = BuiltinTypesCatalogue.item;
        }
        if (this.arity == null) {
            LogManager.getLogger("SequenceType")
                .warn(
                    "Missing arity in incomplete sequence type "
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

    public void resolve(StaticContext context, ExceptionMetadata metadata) {
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

    // increment arity of a sequence type from ? to * and from 1 to +, leave others arity or sequence types untouched
    public SequenceType refineArityIfSubtype(Arity otherArity) {
        if (!isEmptySequence()) {
            if (otherArity.isSubtypeOf(this.arity)) {
                return new SequenceType(this.itemType, otherArity);
            } else {
                return this;
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
                return superArity == Arity.ZeroOrMore || superArity == Arity.OneOrZero;
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
        ItemType itemType = this.getItemType();
        StringBuilder result = new StringBuilder();
        Name name = itemType.getName();
        if (name != null) {
            result.append(name);
        } else {
            result.append("<anonymous>(" + itemType + ")");
        }
        result.append(this.arity.getSymbol());
        return result.toString();
    }

    private static final Map<String, SequenceType> sequenceTypes;

    static {
        sequenceTypes = new HashMap<>();
    }

    public static SequenceType createSequenceType(String userFriendlyName) {
        // lazily caching of sequence types
        SequenceType st = sequenceTypes.get(userFriendlyName);
        if (st != null) {
            return st;
        }
        // if not found in cache, create the sequence type on the fly, cache it and return
        switch (userFriendlyName) {
            case "item":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.One);
                break;
            case "item?":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrZero);
                break;
            case "item*":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore);
                break;
            case "item+":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrMore);
                break;
            case "object":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.One);
                break;
            case "object+":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.OneOrMore);
                break;
            case "object*":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "json-item*":
                st = new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "array":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.One);
                break;
            case "array?":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.OneOrZero);
                break;
            case "array*":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, Arity.ZeroOrMore);
                break;
            case "anyAtomicType":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.One);
                break;
            case "anyAtomicType?":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.OneOrZero);
                break;
            case "anyAtomicType*":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "string":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.One);
                break;
            case "string?":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.OneOrZero);
                break;
            case "string*":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "integer":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One);
                break;
            case "integer?":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero);
                break;
            case "integer*":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "numeric?":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceType.Arity.OneOrZero);
                break;
            case "numeric":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceType.Arity.One);
                break;
            case "numeric+":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.OneOrMore);
                break;
            case "numeric*":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.ZeroOrMore);
                break;
            case "decimal?":
                st = new SequenceType(BuiltinTypesCatalogue.decimalItem, SequenceType.Arity.OneOrZero);
                break;
            case "double":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.One);
                break;
            case "double?":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.OneOrZero);
                break;
            case "float":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.One);
                break;
            case "float?":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.OneOrZero);
                break;
            case "boolean":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One);
                break;
            case "boolean?":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.OneOrZero);
                break;
            case "duration?":
                st = new SequenceType(BuiltinTypesCatalogue.durationItem, SequenceType.Arity.OneOrZero);
                break;
            case "yearMonthDuration?":
                st = new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.OneOrZero);
                break;
            case "dayTimeDuration?":
                st = new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.OneOrZero);
                break;
            case "dateTime?":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.OneOrZero);
                break;
            case "dateTimeStamp?":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeStampItem, SequenceType.Arity.OneOrZero);
                break;
            case "date?":
                st = new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.OneOrZero);
                break;
            case "time?":
                st = new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.OneOrZero);
                break;
            case "gDay?":
                st = new SequenceType(BuiltinTypesCatalogue.gDayItem, SequenceType.Arity.OneOrZero);
                break;
            case "gMonth?":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthItem, SequenceType.Arity.OneOrZero);
                break;
            case "gYear?":
                st = new SequenceType(BuiltinTypesCatalogue.gYearItem, SequenceType.Arity.OneOrZero);
                break;
            case "gMonthDay?":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthDayItem, SequenceType.Arity.OneOrZero);
                break;
            case "gYearMonth?":
                st = new SequenceType(BuiltinTypesCatalogue.gYearMonthItem, SequenceType.Arity.OneOrZero);
                break;
            case "anyURI":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, SequenceType.Arity.One);
                break;
            case "anyURI?":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, SequenceType.Arity.OneOrZero);
                break;
            case "hexBinary?":
                st = new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.OneOrZero);
                break;
            case "base64Binary?":
                st = new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.OneOrZero);
                break;
            case "null?":
                st = new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.OneOrZero);
                break;
            case "function(object*, object) as object*":
                st = new SequenceType(
                        ItemTypeFactory.createFunctionItemType(
                            new FunctionSignature(
                                    Arrays.asList(
                                        new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                        new SequenceType(BuiltinTypesCatalogue.objectItem)
                                    ),
                                    new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore)
                            )
                        )
                );
                break;
            case "function(item*, item*) as item*":
                st = new SequenceType(
                    ItemTypeFactory.createFunctionItemType(
                        new FunctionSignature(
                                Arrays.asList(
                                    new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore),
                                    new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)
                                ),
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)
                        )
                    )
                );
                break;
            case "function(item*) as item*":
                st =  new SequenceType(
                        ItemTypeFactory.createFunctionItemType(
                            new FunctionSignature(
                                    Collections.singletonList(
                                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)
                                    ),
                                    new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)
                            )
                        )
                );
                break;
            case "function(object*, object) as function(object*, object) as object*":
                st = new SequenceType(
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
            );
                break;
            case "int?":
                st = new SequenceType(BuiltinTypesCatalogue.intItem, SequenceType.Arity.OneOrZero);
                break;
            case "long?":
                st = new SequenceType(BuiltinTypesCatalogue.longItem, SequenceType.Arity.OneOrZero);
                break;
            case "short?":
                st = new SequenceType(BuiltinTypesCatalogue.shortItem, SequenceType.Arity.OneOrZero);
                break;
            case "byte?":
                st = new SequenceType(BuiltinTypesCatalogue.byteItem, SequenceType.Arity.OneOrZero);
                break;
            case "positiveInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.positiveIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "negativeInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.negativeIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "nonPositiveInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.nonPositiveIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "nonNegativeInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.nonNegativeIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedInt?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedIntItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedLong?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedLongItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedShort?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedShortItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedByte?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedByteItem, SequenceType.Arity.OneOrZero);
                break;
            case "map":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceType.Arity.One);
                break;
            case "map?":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceType.Arity.OneOrZero);
                break;
            case "map*":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, Arity.ZeroOrMore);
                break;
            case "map+":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, Arity.OneOrMore);
                break;
            default:
                throw new OurBadException("Unrecognized type: " + userFriendlyName);
        }
        sequenceTypes.put(userFriendlyName, st);
        return st;
    }

}
