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

package org.rumbledb.compiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.PredicateExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;


/**
 * Dynamic context visitor. Populates the dynamic context to evaluate the main expression.
 */
public class VariableDependenciesVisitor extends AbstractNodeVisitor<Void> {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    Map<Node, Set<String>> inputVariableDependencies;
    Map<Node, Set<String>> outputVariableDependencies;

    private void addInputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null) {
            return;
        }
        if (!inputVariableDependencies.keySet().contains(node)) {
            inputVariableDependencies.put(node, new TreeSet<String>());
        }
        inputVariableDependencies.get(node).addAll(variables);
    }

    private void removeInputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null) {
            return;
        }
        if (!inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        for (String v : variables) {
            inputVariableDependencies.get(node).remove(v);
        }
    }

    private void addInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!inputVariableDependencies.keySet().contains(node)) {
            inputVariableDependencies.put(node, new TreeSet<String>());
        }
        inputVariableDependencies.get(node).add(variable);
    }

    private void removeInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        inputVariableDependencies.get(node).remove(variable);
    }

    private void addOutputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null) {
            return;
        }
        if (!outputVariableDependencies.keySet().contains(node)) {
            outputVariableDependencies.put(node, new TreeSet<String>());
        }
        outputVariableDependencies.get(node).addAll(variables);
    }

    private void addOutputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!outputVariableDependencies.keySet().contains(node)) {
            outputVariableDependencies.put(node, new TreeSet<String>());
        }
        outputVariableDependencies.get(node).add(variable);
    }



    VariableDependenciesVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.outputVariableDependencies = new HashMap<>();
        this.inputVariableDependencies = new HashMap<>();
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    @Override
    protected Void defaultAction(Node node, Void argument) {
        for (Node child : node.getChildren()) {
            visit(child, null);
            addInputVariableDependencies(node, inputVariableDependencies.get(child));
        }
        return null;
    }

    @Override
    public Void visitVariableReference(VariableReferenceExpression expression, Void argument) {
        addInputVariableDependency(expression, expression.getVariableName());
        return null;
    }

    @Override
    public Void visitForClause(ForClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getExpression()));

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitLetClause(LetClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getExpression()));

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitGroupByClause(GroupByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));

        for (GroupByVariableDeclaration var : expression.getGroupVariables()) {
            if (var.getExpression() != null) {
                visit(var.getExpression(), null);
                addInputVariableDependencies(expression, inputVariableDependencies.get(var.getExpression()));
                addOutputVariableDependency(expression, var.getVariableName());
            } else {
                addInputVariableDependency(expression, var.getVariableName());
            }
        }

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitOrderByClause(OrderByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));

        visit(expression.getPreviousClause(), null);
        for (OrderByClauseSortingKey var : expression.getSortingKeys()) {
            visit(var.getExpression(), null);
            addInputVariableDependencies(expression, inputVariableDependencies.get(var.getExpression()));
        }

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitWhereClause(WhereClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));

        visit(expression.getWhereExpression(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getWhereExpression()));

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitCountClause(CountClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));

        addInputVariableDependencies(expression, Collections.emptySet());

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitReturnClause(ReturnClause expression, Void argument) {
        visit(expression.getReturnExpr(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getReturnExpr()));

        removeInputVariableDependencies(expression, outputVariableDependencies.get(expression.getPreviousClause()));
        return null;
    }

    public Void visitPredicateExpression(PredicateExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        visit(expression.getPredicateExpression(), null);

        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getPredicateExpression()));
        removeInputVariableDependency(expression, "$");
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getMainExpression()));
        return null;
    }

    @Override
    public Void visitContextExpr(ContextItemExpression expression, Void argument) {
        addInputVariableDependency(expression, "$");
        return null;
    }

    @Override
    public Void visitInlineFunctionExpr(InlineFunctionExpression expression, Void argument) {
        visit(expression.getBody(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getBody()));
        removeInputVariableDependencies(expression, expression.getParams().keySet());
        return null;
    }

    @Override
    public Void visitSimpleMapExpr(SimpleMapExpression expression, Void argument) {
        // TODO;
        return defaultAction(expression, argument);
    }

    @Override
    public Void visitQuantifiedExpression(QuantifiedExpression expression, Void argument) {
        List<QuantifiedExpressionVar> var = expression.getVariables();
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getEvaluationExpression()));
        for (QuantifiedExpressionVar v : var) {
            visit(v.getExpression(), null);
            removeInputVariableDependency(expression, v.getVariableName());
        }
        for (QuantifiedExpressionVar v : var) {
            addInputVariableDependencies(expression, inputVariableDependencies.get(v.getExpression()));
        }
        return null;
    }

    @Override
    public Void visitTypeSwitchExpression(TypeSwitchExpression expression, Void argument) {
        visit(expression.getTestCondition(), null);
        for (TypeswitchCase v : expression.getCases()) {
            visit(v.getReturnExpression(), null);
            addInputVariableDependencies(expression, inputVariableDependencies.get(v.getReturnExpression()));
            removeInputVariableDependency(expression, v.getVariableName());
        }
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getTestCondition()));
        return null;
    }

    @Override
    public Void visitNamedFunctionRef(NamedFunctionReferenceExpression expression, Void argument) {
        addInputVariableDependency(expression, "Function " + expression.getIdentifier());
        return null;
    }

    @Override
    public Void visitFunctionCall(FunctionCallExpression expression, Void argument) {
        addInputVariableDependency(
            expression,
            "Function " + expression.getFunctionName() + "#" + expression.getArguments().size()
        );
        for (Expression e : expression.getArguments()) {
            if (e != null) {
                visit(e, null);
                addInputVariableDependencies(expression, inputVariableDependencies.get(e));
            }
        }
        return null;
    }

    @Override
    public Void visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getMainExpression()));
        for (Expression e : expression.getArguments()) {
            if (e == null) {
                continue;
            }
            visit(e, null);
            addInputVariableDependencies(expression, inputVariableDependencies.get(e));
        }
        return null;
    }

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        Map<String, Node> nameToNodeMap = new TreeMap<>();
        for (VariableDeclaration variableDeclaration : prolog.getVariableDeclarations()) {
            visit(variableDeclaration, null);
            nameToNodeMap.put(variableDeclaration.getVariableName(), variableDeclaration);
            if (rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.out.print(variableDeclaration.getVariableName());
                for (String d : inputVariableDependencies.get(variableDeclaration)) {
                    System.out.print(" " + d);
                }
                System.out.println("");
            }
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            visit(functionDeclaration, null);
            nameToNodeMap.put(functionDeclaration.getFunctionIdentifier().toString(), functionDeclaration);
            if (rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.out.print(functionDeclaration.getFunctionIdentifier().toString());
                for (String d : inputVariableDependencies.get(functionDeclaration)) {
                    System.out.print(" " + d);
                }
                System.out.println("");
            }
        }
        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclaration expression, Void argument) {
        if (expression.getExpression() != null) {
            visit(expression.getExpression(), null);
            addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getExpression()));
            return null;
        }
        addInputVariableDependencies(expression, Collections.emptySet());
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration expression, Void argument) {
        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, inputVariableDependencies.get(expression.getExpression()));
        return null;
    }
}
