/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

    private Arity _arity;
    private ItemType _itemType;
    private boolean _isEmpty = false;


    public SequenceType(ItemType itemType, Arity arity) {
        this._itemType = itemType;
        this._arity = arity;

    }

    public SequenceType(ItemType itemType) {
        this._itemType = itemType;
        this._arity = Arity.One;

    }

    public SequenceType() {
        this._itemType = new ItemType(ItemTypes.Item);
        this._arity = Arity.ZeroOrMore;
        this._isEmpty = true;
    }

    public boolean isEmpty() {
        return _isEmpty;
    }

    public ItemType getItemType() {
        return _itemType;
    }

    public Arity getArity() {
        return _arity;
    }

    public boolean isSubtypeOf(SequenceType superType) {
        return this._itemType.isSubtypeOf(superType.getItemType()) &&
                this._arity == superType._arity;
    }
    public enum Arity {
        OneOrZero,
        OneOrMore,
        ZeroOrMore,
        One
    }
}
