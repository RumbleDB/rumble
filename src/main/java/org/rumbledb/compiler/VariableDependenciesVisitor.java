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
import java.util.HashSet;
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
 * This visitor resolves dependencies between variable and function declarations.
 * 
 * If a variable $x depends on a variable $y, then $y must be evaluated before $x.
 * 
 * Example:
 * 
 * declare variable $x := 1;
 * declare variable $y := $x;
 * 
 * If a variable $x depends on a function f, then f's closure must be built before $x is evaluated.
 * 
 * declare function f() { 1 };
 * declare variable $x := f();
 * 
 * If a function f depends on a variable $x, then $x must be evaluated before f's closure is built.
 *
 * declare variable $x := 1;
 * declare function f() { $x };
 * 
 * Note that a function cannot depend of a function, as mutually recursive calls are allowed.
 * 
 * Once all dependencies have been determined, the visited builds a DAG, builds a topological ordering
 * thereof, and re-sorts declarations in the prolog for further processing by other visitors.
 * 
 */
public class VariableDependenciesVisitor extends AbstractNodeVisitor<Void> {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    /**
     * Input variable dependencies are lists of variables and functions that an expression depends on.
     */
    Map<Node, Set<String>> inputVariableDependencies;
    /**
     * Output variable dependencies are lists of variables in the tuples that a clause produces.
     */
    Map<Node, Set<String>> outputVariableDependencies;

    /**
     * Builds a new visitor.
     * 
     * @param rumbleRuntimeConfiguration the configuration. This is used for trigerring or not debug output.
     */
    VariableDependenciesVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.outputVariableDependencies = new HashMap<>();
        this.inputVariableDependencies = new HashMap<>();
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    private void addInputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null || variables.isEmpty()) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<String>());
        }
        getInputVariableDependencies(node).addAll(variables);
    }

    private void removeInputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null || variables.isEmpty()) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        for (String v : variables) {
            getInputVariableDependencies(node).remove(v);
        }
    }

    private void addInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<String>());
        }
        getInputVariableDependencies(node).add(variable);
    }

    private void removeInputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        getInputVariableDependencies(node).remove(variable);
    }

    private void addOutputVariableDependencies(Node node, Set<String> variables) {
        if (variables == null || variables.isEmpty()) {
            return;
        }
        if (!this.outputVariableDependencies.keySet().contains(node)) {
            this.outputVariableDependencies.put(node, new TreeSet<String>());
        }
        getOutputVariableDependencies(node).addAll(variables);
    }

    private void addOutputVariableDependency(Node node, String variable) {
        if (variable == null) {
            return;
        }
        if (!this.outputVariableDependencies.keySet().contains(node)) {
            this.outputVariableDependencies.put(node, new TreeSet<String>());
        }
        getOutputVariableDependencies(node).add(variable);
    }

    private Set<String> getOutputVariableDependencies(Node node) {
        if (node == null) {
            return Collections.emptySet();
        }
        if (!this.outputVariableDependencies.containsKey(node)) {
            return Collections.emptySet();
        }
        return this.outputVariableDependencies.get(node);
    }

    private Set<String> getInputVariableDependencies(Node node) {
        if (node == null) {
            return Collections.emptySet();
        }
        if (!this.inputVariableDependencies.containsKey(node)) {
            return Collections.emptySet();
        }
        return this.inputVariableDependencies.get(node);
    }



    @Override
    protected Void defaultAction(Node node, Void argument) {
        for (Node child : node.getChildren()) {
            visit(child, null);
            addInputVariableDependencies(node, getInputVariableDependencies(child));
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
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    @Override
    public Void visitLetClause(LetClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitGroupByClause(GroupByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        for (GroupByVariableDeclaration var : expression.getGroupVariables()) {
            if (var.getExpression() != null) {
                visit(var.getExpression(), null);
                addInputVariableDependencies(expression, getInputVariableDependencies(var.getExpression()));
                addOutputVariableDependency(expression, var.getVariableName());
            } else {
                addInputVariableDependency(expression, var.getVariableName());
            }
        }

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitOrderByClause(OrderByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        visit(expression.getPreviousClause(), null);
        for (OrderByClauseSortingKey var : expression.getSortingKeys()) {
            visit(var.getExpression(), null);
            addInputVariableDependencies(expression, getInputVariableDependencies(var.getExpression()));
        }

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitWhereClause(WhereClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        visit(expression.getWhereExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getWhereExpression()));

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitCountClause(CountClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        addInputVariableDependencies(expression, new HashSet<String>());

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitReturnClause(ReturnClause expression, Void argument) {
        visit(expression.getReturnExpr(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getReturnExpr()));

        removeInputVariableDependencies(
            expression,
            getOutputVariableDependencies(expression.getPreviousClause())
        );
        return null;
    }

    public Void visitPredicateExpression(PredicateExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        visit(expression.getPredicateExpression(), null);

        addInputVariableDependencies(
            expression,
            getInputVariableDependencies(expression.getPredicateExpression())
        );
        removeInputVariableDependency(expression, "$");
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getMainExpression()));
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
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getBody()));
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
            getInputVariableDependencies(expression.getEvaluationExpression())
        );
        for (QuantifiedExpressionVar v : var) {
            visit(v.getExpression(), null);
            removeInputVariableDependency(expression, v.getVariableName());
        }
        for (QuantifiedExpressionVar v : var) {
            addInputVariableDependencies(expression, getInputVariableDependencies(v.getExpression()));
        }
        return null;
    }

    @Override
    public Void visitTypeSwitchExpression(TypeSwitchExpression expression, Void argument) {
        visit(expression.getTestCondition(), null);
        for (TypeswitchCase v : expression.getCases()) {
            visit(v.getReturnExpression(), null);
            addInputVariableDependencies(expression, getInputVariableDependencies(v.getReturnExpression()));
            removeInputVariableDependency(expression, v.getVariableName());
        }
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getTestCondition()));
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
                addInputVariableDependencies(expression, getInputVariableDependencies(e));
            }
        }
        return null;
    }

    @Override
    public Void visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getMainExpression()));
        for (Expression e : expression.getArguments()) {
            if (e == null) {
                continue;
            }
            visit(e, null);
            addInputVariableDependencies(expression, getInputVariableDependencies(e));
        }
        return null;
    }

    private Map<String, Node> buildNameToNodeMap(Prolog prolog) {
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
                System.out.println(String.join(", ", getInputVariableDependencies(variableDeclaration)));
            }
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            visit(functionDeclaration, null);
            nameToNodeMap.put(functionDeclaration.getFunctionIdentifier().toString(), functionDeclaration);
            if (this.rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.out.print(functionDeclaration.getFunctionIdentifier().toString());
                System.out.println(String.join(", ", getInputVariableDependencies(functionDeclaration)));
            }
        }
        return nameToNodeMap;
    }

    private DirectedAcyclicGraph<Node, DefaultEdge> buildDependencyGraph(
            Map<String, Node> nameToNodeMap,
            Prolog prolog
    ) {
        DirectedAcyclicGraph<Node, DefaultEdge> dependencyGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        for (VariableDeclaration variableDeclaration : prolog.getVariableDeclarations()) {
            Set<String> names = getInputVariableDependencies(variableDeclaration);
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
            Set<String> names = getInputVariableDependencies(functionDeclaration);
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
        return dependencyGraph;
    }

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        Map<String, Node> nameToNodeMap = buildNameToNodeMap(prolog);
        DirectedAcyclicGraph<Node, DefaultEdge> dependencyGraph = buildDependencyGraph(nameToNodeMap, prolog);
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
            addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));
            System.out.println(String.join(", ", getInputVariableDependencies(expression)));
            return null;
        }
        addInputVariableDependencies(expression, new HashSet<String>());
        System.out.println(String.join(", ", getInputVariableDependencies(expression)));
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration expression, Void argument) {
        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression));
        return null;
    }
}
