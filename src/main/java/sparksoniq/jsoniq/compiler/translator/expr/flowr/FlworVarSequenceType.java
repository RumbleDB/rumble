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
package sparksoniq.jsoniq.compiler.translator.expr.flowr;


import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class FlworVarSequenceType extends Expression {

    public boolean isEmpty() {
        return isEmpty;
    }

    public static ItemTypes getItemType(String text) {
        text = text.toLowerCase();
        switch (text) {
            case "object":
                return ItemTypes.ObjectItem;
            case "atomic":
                return ItemTypes.AtomicItem;
            case "string":
                return ItemTypes.StringItem;
            case "integer":
                return ItemTypes.IntegerItem;
            case "decimal":
                return ItemTypes.DecimalItem;
            case "double":
                return ItemTypes.DoubleItem;
            case "boolean":
                return ItemTypes.BooleanItem;
            case "null":
                return ItemTypes.NullItem;
            case "array":
                return ItemTypes.ArrayItem;
            case "json-item":
                return ItemTypes.JSONItem;

            default:
                return ItemTypes.Item;
        }
    }

    public SequenceType getSequence() {
        return _sequence;
    }

    public FlworVarSequenceType(ExpressionMetadata metadata) {
        super(metadata);
        this._sequence = new SequenceType(new ItemType(ItemTypes.Item),
                SequenceType.Arity.ZeroOrMore);
        this.isEmpty = true;
    }

    public FlworVarSequenceType(ItemTypes item, SequenceType.Arity arity, ExpressionMetadata metadata) {
        super(metadata);
        this._sequence = new SequenceType(new ItemType(item), arity);
        this.isEmpty = false;

    }

    public FlworVarSequenceType(ItemTypes item, ExpressionMetadata metadata) {
        super(metadata);
        this._sequence = new SequenceType(new ItemType(item), SequenceType.Arity.One);
        this.isEmpty = false;

    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        return new ArrayList<>();
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(sequenceType ";
        if (isEmpty)
            return result + "( )";
        result += "(itemType ";
        result += getSerializationOfItemType(this._sequence.getItemType().getType());
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

    private SequenceType _sequence;
    private boolean isEmpty;
}
