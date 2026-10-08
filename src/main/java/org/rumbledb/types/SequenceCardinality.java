/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.types;

/**
 * Possible sequence sizes, with all lengths of two or more represented by MANY.
 * Unlike the declared occurrence indicators, this can express multiple-only and empty-or-multiple.
 */
public enum SequenceCardinality {
    /** The empty sequence, (). */
    EMPTY(1),
    /** Exactly one item. */
    ONE(2),
    /** Two or more items. */
    MANY(4),
    /** The empty sequence or one item, ?. */
    ZERO_OR_ONE(3),
    /** One or more items, +. */
    ONE_OR_MANY(6),
    /** The empty sequence or two or more items, but never exactly one. Displayed as *. */
    EMPTY_OR_MANY(5),
    /** Any number of items, *. */
    ANY(7);

    private static final SequenceCardinality[] BY_MASK = new SequenceCardinality[8];

    static {
        for (SequenceCardinality cardinality : values()) {
            BY_MASK[cardinality.mask] = cardinality;
        }
    }

    private final int mask;

    SequenceCardinality(int mask) {
        this.mask = mask;
    }

    public boolean allowsZero() {
        return (this.mask & 1) != 0;
    }

    public boolean allowsOne() {
        return (this.mask & 2) != 0;
    }

    public boolean allowsMany() {
        return (this.mask & 4) != 0;
    }

    public boolean isSubtypeOf(SequenceCardinality other) {
        return (this.mask & other.mask) == this.mask;
    }

    public boolean overlaps(SequenceCardinality other) {
        return (this.mask & other.mask) != 0;
    }

    public SequenceCardinality union(SequenceCardinality other) {
        return fromMask(this.mask | other.mask);
    }

    public SequenceCardinality concatenate(SequenceCardinality other) {
        boolean zero = allowsZero() && other.allowsZero();
        boolean one = (allowsOne() && other.allowsZero()) || (allowsZero() && other.allowsOne());
        boolean many = allowsMany() || other.allowsMany() || (allowsOne() && other.allowsOne());
        return fromPossibilities(zero, one, many);
    }

    /**
     * The cardinality of concatenating one sequence of this cardinality per iteration, for a number of
     * iterations with the given cardinality. Each iteration can produce a different size: for example,
     * two iterations of an optional sequence can produce exactly one item.
     */
    public SequenceCardinality repeated(SequenceCardinality iterations) {
        boolean someIteration = iterations.allowsOne() || iterations.allowsMany();
        boolean zero = iterations.allowsZero() || allowsZero();
        boolean one = allowsOne() && (iterations.allowsOne() || (iterations.allowsMany() && allowsZero()));
        boolean many = (allowsMany() && someIteration) || (iterations.allowsMany() && (allowsOne() || allowsMany()));
        return fromPossibilities(zero, one, many);
    }

    public SequenceCardinality replaceZeroWithOne() {
        return fromPossibilities(false, allowsZero() || allowsOne(), allowsMany());
    }

    /**
     * The cardinality after grouping, which can merge any number of items into one but never removes all of them.
     */
    public SequenceCardinality grouped() {
        return fromPossibilities(allowsZero(), allowsOne() || allowsMany(), allowsMany());
    }

    /**
     * The cardinality after filtering, which can keep any number of items up to the original size.
     */
    public SequenceCardinality filtered() {
        return fromPossibilities(true, allowsOne() || allowsMany(), allowsMany());
    }

    public SequenceType.Arity toArity() {
        return switch (this) {
            case EMPTY -> SequenceType.Arity.Zero;
            case ONE -> SequenceType.Arity.One;
            case ZERO_OR_ONE -> SequenceType.Arity.OneOrZero;
            case MANY, ONE_OR_MANY -> SequenceType.Arity.OneOrMore;
            case EMPTY_OR_MANY, ANY -> SequenceType.Arity.ZeroOrMore;
        };
    }

    public static SequenceCardinality fromArity(SequenceType.Arity arity) {
        return switch (arity) {
            case Zero -> EMPTY;
            case One -> ONE;
            case OneOrZero -> ZERO_OR_ONE;
            case OneOrMore -> ONE_OR_MANY;
            case ZeroOrMore -> ANY;
        };
    }

    private static SequenceCardinality fromPossibilities(boolean zero, boolean one, boolean many) {
        return fromMask((zero ? 1 : 0) | (one ? 2 : 0) | (many ? 4 : 0));
    }

    private static SequenceCardinality fromMask(int mask) {
        if (BY_MASK[mask] == null) {
            throw new IllegalArgumentException("A sequence cardinality must allow at least one size.");
        }
        return BY_MASK[mask];
    }
}
