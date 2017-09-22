/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler.translator.expr.operational.base;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;


public abstract class OperationalExpressionBase extends Expression {

    public static List<Operator> getOperatorFromOpList(List<Token> ops) {
        List<Operator> result = new ArrayList<>();
        ops.forEach(op -> result.add(getOperatorFromString(op.getText())));
        return result;
    }

    public static Operator getOperatorFromString(String token)
    {
        token = token.toUpperCase();
        switch (token)
        {
            case "EQ":
                return Operator.EQ;
            case "NE":
                return Operator.NE;
            case "LT":
                return Operator.LT;
            case "LE":
                return Operator.LE;
            case "GT":
                return Operator.GT;
            case "GE":
                return Operator.GE;

            case "+":
                return Operator.PLUS;
            case "-":
                return Operator.MINUS;

            case "*":
                return Operator.MUL;
            case "DIV":
                return Operator.DIV;
            case "IDIV":
                return Operator.IDIV;
            case "MOD":
                return Operator.MOD;

            case "TO":
                return Operator.TO;
            case "||":
                return Operator.CONCAT;
            case "INSTANCE OF":
                return Operator.INSTANCE_OF;
        }

        return Operator.NONE;
    }

    public static String getStringFromOperator(Operator operator)
    {
        switch (operator)
        {

            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MUL:
                return "*";
            case CONCAT:
                return "||";
            case INSTANCE_OF:
                return "instance of";
            default: return operator.toString().toLowerCase();
        }
    }

    public enum Operator{
        OR,
        AND,
        NOT,

        EQ,
        NE,
        LT,
        LE,
        GT,
        GE,

        PLUS,
        MINUS,
        MUL,
        DIV,
        MOD,
        IDIV,

        TO,
        CONCAT,
        INSTANCE_OF,
        COMMA,


        NONE,

    }

    public abstract boolean isActive();

    public Expression getContent()
    {
        if(this.isActive())
            return this;
        else
        {
            if(this._mainExpression != null && this._mainExpression instanceof OperationalExpressionBase)
                return ((OperationalExpressionBase)_mainExpression).getContent();
        }

        return this;
    }


    public void validateOperators(List<Operator> validOps, List<Operator> ops)
    {
        for(Operator op : ops)
            if(!validOps.contains(op))
                throw new IllegalArgumentException("Operational operators exception");
    }

    public void validateOperator(List<Operator> validOps, Operator op)
    {
        if(!validOps.contains(op))
            throw new IllegalArgumentException("Operational operators exception");
    }

    public Expression getMainExpression() {
        return _mainExpression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        if(this._mainExpression!=null)
            result.add(this._mainExpression);
        if(depthSearch && _mainExpression != null)
            result.addAll(_mainExpression.getDescendants(depthSearch));
        return result;
    }

    protected OperationalExpressionBase()
    {
        super();
    }


    protected OperationalExpressionBase(Expression _mainExpression,
                                        Operator op)
    {
        super();
        this._mainExpression = _mainExpression;
        this._singleOperator = op;
        this._hasMultipleOperators = false;

    }

    protected OperationalExpressionBase(Expression _mainExpression,
                                        List<Operator> ops)
    {
        super();
        this._mainExpression = _mainExpression;
        this._multipleOperators = ops;
        this._hasMultipleOperators = true;

    }

    protected boolean _isActive;
    private boolean _hasMultipleOperators;
    protected Expression _mainExpression;
    protected List<Operator> _multipleOperators;
    protected Operator _singleOperator;
}
