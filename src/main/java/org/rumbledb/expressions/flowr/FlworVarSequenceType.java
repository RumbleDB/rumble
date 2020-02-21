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

package org.rumbledb.expressions.flowr;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class FlworVarSequenceType extends Expression {

    private SequenceType sequence;
    private boolean isEmpty = false;

    public FlworVarSequenceType(ExceptionMetadata metadata) {
        super(metadata);
        this.sequence = new SequenceType();
        this.isEmpty = true;
    }

    public FlworVarSequenceType(ItemTypes item, SequenceType.Arity arity, ExceptionMetadata metadata) {
        super(metadata);
        this.sequence = new SequenceType(new ItemType(item), arity);
    }

    public FlworVarSequenceType(ItemTypes item, ExceptionMetadata metadata) {
        super(metadata);
        this.sequence = new SequenceType(new ItemType(item), SequenceType.Arity.One);
    }

    public FlworVarSequenceType(SequenceType sequenceType, ExceptionMetadata metadata) {
        super(metadata);
        this.sequence = sequenceType;
    }


    public boolean isEmpty() {
        return this.isEmpty;
    }

    public SequenceType getSequence() {
        return this.sequence;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        // TO this class should not be an expression.
        // nothing to do as no children
        return argument;
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(sequenceType ";
        if (this.isEmpty)
            return result + "( )";
        result += "(itemType ";
        result += getSerializationOfItemType(this.sequence.getItemType().getType());
        result += "))";
        return result;
    }

    private String getSerializationOfItemType(ItemTypes item) {
        switch (item) {
            case Item:
                return "item";
            case IntegerItem:
                return "(atomicType integer)";
            case DecimalItem:
                return "(atomicType decimal)";
            case DoubleItem:
                return "(atomicType double)";
            case AtomicItem:
                return "(atomicType atomic)";
            case StringItem:
                return "(atomicType string)";
            case BooleanItem:
                return "(atomicType boolean)";
            case NullItem:
                return "(atomicType null)";

            case JSONItem:
                return "(jSONItemTest json-item)";
            case ArrayItem:
                return "(jSONItemTest array)";
            case ObjectItem:
                return "(jSONItemTest object)";
            default:
                return "item";
        }
    }
}
