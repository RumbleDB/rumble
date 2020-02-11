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

package sparksoniq.semantics.types;

import java.io.Serializable;

public class SequenceType implements Serializable {

    private static final long serialVersionUID = 1L;
    private Arity arity;
    private ItemType itemType;
    private boolean isEmptySequence = false;

    public final static SequenceType mostGeneralSequenceType = new SequenceType(
            new ItemType(ItemTypes.Item),
            Arity.ZeroOrMore
    );


    public SequenceType(ItemType itemType, Arity arity) {
        this.itemType = itemType;
        this.arity = arity;
    }

    public SequenceType(ItemType itemType) {
        this.itemType = itemType;
        this.arity = Arity.One;
    }

    public SequenceType() {
        this.itemType = new ItemType(ItemTypes.Item);
        this.arity = Arity.ZeroOrMore;
        this.isEmptySequence = true;
    }

    public boolean isEmptySequence() {
        return this.isEmptySequence;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public Arity getArity() {
        return this.arity;
    }

    public boolean isSubtypeOf(SequenceType superType) {
        return this.itemType.isSubtypeOf(superType.getItemType())
            &&
            this.arity == superType.arity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SequenceType))
            return false;
        SequenceType sequenceType = (SequenceType) o;
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
        };

        public abstract String getSymbol();
    }
}
