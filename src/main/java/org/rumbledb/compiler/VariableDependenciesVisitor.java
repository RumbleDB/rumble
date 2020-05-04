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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CycleInVariableDeclarationsException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.VariableAlreadyExistsException;
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
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<String>());
        }
        this.inputVariableDependencies.get(node).addAll(variables);
    }

    private void removeInputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null) {
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        for (String v : variables) {
            this.inputVariableDependencies.get(node).remove(v);
        }
    }

    private void addInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<String>());
        }
        this.inputVariableDependencies.get(node).add(variable);
    }

    private void removeInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        this.inputVariableDependencies.get(node).remove(variable);
    }

    private void addOutputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null) {
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.outputVariableDependencies.keySet().contains(node)) {
            this.outputVariableDependencies.put(node, new TreeSet<String>());
        }
        this.outputVariableDependencies.get(node).addAll(variables);
    }

    private void addOutputVariableDependency(Node node, String variable) {
        if (variable == null) {
            addInputVariableDependencies(node, Collections.emptySet());
            return;
        }
        if (!this.outputVariableDependencies.keySet().contains(node)) {
            this.outputVariableDependencies.put(node, new TreeSet<String>());
        }
        this.outputVariableDependencies.get(node).add(variable);
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
            addInputVariableDependencies(node, this.inputVariableDependencies.get(child));
        }
        if (!this.inputVariableDependencies.containsKey(node)) {
            addInputVariableDependencies(node, Collections.emptySet());
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
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getExpression()));

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    @Override
    public Void visitLetClause(LetClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getExpression()));

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitGroupByClause(GroupByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));

        for (GroupByVariableDeclaration var : expression.getGroupVariables()) {
            if (var.getExpression() != null) {
                visit(var.getExpression(), null);
                addInputVariableDependencies(expression, this.inputVariableDependencies.get(var.getExpression()));
                addOutputVariableDependency(expression, var.getVariableName());
            } else {
                addInputVariableDependency(expression, var.getVariableName());
            }
        }

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitOrderByClause(OrderByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));

        visit(expression.getPreviousClause(), null);
        for (OrderByClauseSortingKey var : expression.getSortingKeys()) {
            visit(var.getExpression(), null);
            addInputVariableDependencies(expression, this.inputVariableDependencies.get(var.getExpression()));
        }

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitWhereClause(WhereClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));

        visit(expression.getWhereExpression(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getWhereExpression()));

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitCountClause(CountClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, this.outputVariableDependencies.get(expression.getPreviousClause()));

        addInputVariableDependencies(expression, Collections.emptySet());

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitReturnClause(ReturnClause expression, Void argument) {
        visit(expression.getReturnExpr(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getReturnExpr()));

        removeInputVariableDependencies(
            expression,
            this.outputVariableDependencies.get(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitPredicateExpression(PredicateExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        visit(expression.getPredicateExpression(), null);

        addInputVariableDependencies(
            expression,
            this.inputVariableDependencies.get(expression.getPredicateExpression())
        );
        removeInputVariableDependency(expression, "$");
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getMainExpression()));
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
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getBody()));
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
        addInputVariableDependencies(
            expression,
            this.inputVariableDependencies.get(expression.getEvaluationExpression())
        );
        for (QuantifiedExpressionVar v : var) {
            visit(v.getExpression(), null);
            removeInputVariableDependency(expression, v.getVariableName());
        }
        for (QuantifiedExpressionVar v : var) {
            addInputVariableDependencies(expression, this.inputVariableDependencies.get(v.getExpression()));
        }
        return null;
    }

    @Override
    public Void visitTypeSwitchExpression(TypeSwitchExpression expression, Void argument) {
        visit(expression.getTestCondition(), null);
        for (TypeswitchCase v : expression.getCases()) {
            visit(v.getReturnExpression(), null);
            addInputVariableDependencies(expression, this.inputVariableDependencies.get(v.getReturnExpression()));
            removeInputVariableDependency(expression, v.getVariableName());
        }
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getTestCondition()));
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
                addInputVariableDependencies(expression, this.inputVariableDependencies.get(e));
            }
        }
        return null;
    }

    @Override
    public Void visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getMainExpression()));
        for (Expression e : expression.getArguments()) {
            if (e == null) {
                continue;
            }
            visit(e, null);
            addInputVariableDependencies(expression, this.inputVariableDependencies.get(e));
        }
        return null;
    }

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        Map<String, Node> nameToNodeMap = new TreeMap<>();
        for (VariableDeclaration variableDeclaration : prolog.getVariableDeclarations()) {
            if (nameToNodeMap.containsKey(variableDeclaration.getVariableName())) {
                throw new VariableAlreadyExistsException(
                        variableDeclaration.getVariableName(),
                        variableDeclaration.getMetadata()
                );
            }
            visit(variableDeclaration, null);
            nameToNodeMap.put(variableDeclaration.getVariableName(), variableDeclaration);
            if (this.rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.out.print(variableDeclaration.getVariableName());
                System.out.println(String.join(", ", this.inputVariableDependencies.get(variableDeclaration)));
            }
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            visit(functionDeclaration, null);
            nameToNodeMap.put(functionDeclaration.getFunctionIdentifier().toString(), functionDeclaration);
            if (this.rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.out.print(functionDeclaration.getFunctionIdentifier().toString());
                System.out.println(String.join(", ", this.inputVariableDependencies.get(functionDeclaration)));
            }
        }
        DirectedAcyclicGraph<Node, DefaultEdge> dependencyGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        for (VariableDeclaration variableDeclaration : prolog.getVariableDeclarations()) {
            Set<String> names = this.inputVariableDependencies.get(variableDeclaration);
            if (names == null) {
                throw new OurBadException(
                        "Error while resolving dependencies! Dependencies not found for "
                            + variableDeclaration.getVariableName()
                );
            }
            dependencyGraph.addVertex(variableDeclaration);
            for (String name : names) {
                Node declaration = nameToNodeMap.get(name);
                if (declaration != null) {
                    dependencyGraph.addVertex(declaration);
                    try {
                        dependencyGraph.addEdge(declaration, variableDeclaration);
                    } catch (IllegalArgumentException e) {
                        throw new CycleInVariableDeclarationsException(
                                "There is a cycle in the dependencies in the variable and function declarations. It is thus impossible to build the dynamic context.",
                                variableDeclaration.getMetadata()
                        );
                    }
                }
            }
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            Set<String> names = this.inputVariableDependencies.get(functionDeclaration);
            if (names == null) {
                throw new OurBadException(
                        "Error while resolving dependencies! Dependencies not found for "
                            + functionDeclaration.getFunctionIdentifier()
                );
            }
            dependencyGraph.addVertex(functionDeclaration);
            for (String name : names) {
                Node declaration = nameToNodeMap.get(name);
                if (declaration != null) {
                    dependencyGraph.addVertex(declaration);
                    try {
                        dependencyGraph.addEdge(declaration, functionDeclaration);
                    } catch (IllegalArgumentException e) {
                        throw new CycleInVariableDeclarationsException(
                                "There is a cycle in the dependencies in the variable and function declarations. It is thus impossible to build the dynamic context.",
                                functionDeclaration.getMetadata()
                        );
                    }
                }
            }
        }
        List<Node> resolvedList = new ArrayList<>();
        Iterator<Node> iterator = dependencyGraph.iterator();
        while (iterator.hasNext()) {
            Node nextDeclaration = iterator.next();
            resolvedList.add(nextDeclaration);
        }
        prolog.setDeclarations(resolvedList);
        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclaration expression, Void argument) {
        if (expression.getExpression() != null) {
            visit(expression.getExpression(), null);
            addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression.getExpression()));
            System.out.println(String.join(", ", this.inputVariableDependencies.get(expression)));
            return null;
        }
        addInputVariableDependencies(expression, Collections.emptySet());
        System.out.println(String.join(", ", this.inputVariableDependencies.get(expression)));
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration expression, Void argument) {
        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, this.inputVariableDependencies.get(expression));
        return null;
    }
}
