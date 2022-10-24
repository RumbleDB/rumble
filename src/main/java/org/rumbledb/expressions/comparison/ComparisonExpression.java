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

package org.rumbledb.expressions.comparison;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class ComparisonExpression extends Expression {

    public static enum ComparisonOperator {
        VC_EQ("eq"),
        VC_NE("ne"),
        VC_LT("lt"),
        VC_LE("le"),
        VC_GT("gt"),
        VC_GE("ge"),
        GC_EQ("="),
        GC_NE("!="),
        GC_LT("<"),
        GC_LE("<="),
        GC_GT(">"),
        GC_GE(">=");

        private String name;
        private boolean isValueComparison;

        ComparisonOperator(String name) {
            switch (name) {
                case "eq":
                case "ne":
                case "le":
                case "lt":
                case "ge":
                case "gt":
                    this.isValueComparison = true;
                    break;
                default:
                    this.isValueComparison = false;
            }
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public boolean isValueComparison() {
            return this.isValueComparison;
        }
        
        public ComparisonOperator getCorrespondingValueComparison() {
        	switch(this)
        	{
	        	case GC_EQ: return VC_EQ;
	        	case GC_NE: return VC_NE;
	        	case GC_LE: return VC_LE;
	        	case GC_LT: return VC_LT;
	        	case GC_GE: return VC_GE;
	        	case GC_GT: return VC_GT;
	        	default: throw new OurBadException("Only works on general comparisons.");
        	}
        }

        public static ComparisonOperator fromSymbol(String symbol) {
            if (symbol.equals(VC_EQ.toString())) {
                return VC_EQ;
            }
            if (symbol.equals(VC_NE.toString())) {
                return VC_NE;
            }
            if (symbol.equals(VC_LT.toString())) {
                return VC_LT;
            }
            if (symbol.equals(VC_LE.toString())) {
                return VC_LE;
            }
            if (symbol.equals(VC_GT.toString())) {
                return VC_GT;
            }
            if (symbol.equals(VC_GE.toString())) {
                return VC_GE;
            }
            if (symbol.equals(GC_EQ.toString())) {
                return GC_EQ;
            }
            if (symbol.equals(GC_NE.toString())) {
                return GC_NE;
            }
            if (symbol.equals(GC_LT.toString())) {
                return GC_LT;
            }
            if (symbol.equals(GC_LE.toString())) {
                return GC_LE;
            }
            if (symbol.equals(GC_GT.toString())) {
                return GC_GT;
            }
            if (symbol.equals(GC_GE.toString())) {
                return GC_GE;
            }
            throw new OurBadException("Unrecognized comparison symbol: " + symbol);
        }
    };

    private Expression leftExpression;
    private Expression rightExpression;
    private ComparisonOperator comparisonOperator;

    public ComparisonExpression(
            Expression leftExpression,
            Expression rightExpression,
            ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.comparisonOperator = comparisonOperator;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitComparisonExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.leftExpression, this.rightExpression);
    }

    public ComparisonOperator getComparisonOperator() {
        return this.comparisonOperator;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (" + (this.comparisonOperator) + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + (this.staticSequenceType == null ? "not set" : this.staticSequenceType));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("(\n");

        this.leftExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");

        indentIt(sb, indent);
        sb.append(this.comparisonOperator.toString() + "\n");

        indentIt(sb, indent);
        sb.append("(\n");

        this.rightExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");
    }
}
