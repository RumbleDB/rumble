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
import java.util.stream.Collectors;
import java.util.Map;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
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
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;


/**
 * This visitor resolves dependencies between variable and function declarations.
 * 
 * If a variable $x depends on a variable $y, then $y must be evaluated before $x.
 * 
 * Example:
 * 
 * declare variable $y := 1;
 * declare variable $x := $y;
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
 * Note that a function cannot depend on a function, as mutually recursive calls are allowed.
 * 
 * Once all dependencies have been determined, the visitor builds a DAG, builds a topological ordering
 * thereof, and re-sorts declarations in the prolog for further processing by other visitors.
 * 
 */
public class VariableDependenciesVisitor extends AbstractNodeVisitor<Void> {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    /**
     * Input variable dependencies are lists of variables and functions that an expression depends on.
     */
    Map<Node, Set<Name>> inputVariableDependencies;
    /**
     * Output variable dependencies are lists of variables in the tuples that a clause produces.
     */
    Map<Node, Set<Name>> outputVariableDependenciesForClauses;

    /**
     * Builds a new visitor.
     * 
     * @param rumbleRuntimeConfiguration the configuration. This is used for trigerring or not debug output.
     */
    VariableDependenciesVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.outputVariableDependenciesForClauses = new HashMap<>();
        this.inputVariableDependencies = new HashMap<>();
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    private void addInputVariableDependencies(Node node, Set<Name> variables) {
        if (variables == null) {
            throw new OurBadException("Unexpected null set while resolving variable dependencies.");
        }
        if (variables.isEmpty()) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<Name>());
        }
        getInputVariableDependencies(node).addAll(variables);
    }

    private void removeInputVariableDependencies(Node node, Set<Name> variables) {
        if (variables == null) {
            throw new OurBadException("Unexpected null set while resolving variable dependencies.");
        }
        if (variables.isEmpty()) {
            return;
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        for (Name v : variables) {
            getInputVariableDependencies(node).remove(v);
        }
    }

    private void addInputVariableDependency(Node node, Name variable) {
        if (variable == null) {
            throw new OurBadException("Unexpected null string while resolving variable dependencies.");
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            this.inputVariableDependencies.put(node, new TreeSet<Name>());
        }
        getInputVariableDependencies(node).add(variable);
    }

    private void removeInputVariableDependency(Node node, Name variable) {
        if (variable == null) {
            throw new OurBadException("Unexpected null string while resolving variable dependencies.");
        }
        if (!this.inputVariableDependencies.keySet().contains(node)) {
            return;
        }
        getInputVariableDependencies(node).remove(variable);
    }

    private void addOutputVariableDependencies(Node node, Set<Name> variables) {
        if (variables == null) {
            throw new OurBadException("Unexpected null set while resolving variable dependencies.");
        }
        if (variables.isEmpty()) {
            return;
        }
        if (!this.outputVariableDependenciesForClauses.keySet().contains(node)) {
            this.outputVariableDependenciesForClauses.put(node, new TreeSet<Name>());
        }
        getOutputVariableDependencies(node).addAll(variables);
    }

    private void addOutputVariableDependency(Node node, Name variable) {
        if (variable == null) {
            throw new OurBadException("Unexpected null string while resolving variable dependencies.");
        }
        if (!this.outputVariableDependenciesForClauses.keySet().contains(node)) {
            this.outputVariableDependenciesForClauses.put(node, new TreeSet<Name>());
        }
        getOutputVariableDependencies(node).add(variable);
    }

    private Set<Name> getOutputVariableDependencies(Node node) {
        if (node == null) {
            throw new OurBadException("Unexpected null string while resolving variable dependencies.");
        }
        if (!this.outputVariableDependenciesForClauses.containsKey(node)) {
            return Collections.emptySet();
        }
        return this.outputVariableDependenciesForClauses.get(node);
    }

    private Set<Name> getInputVariableDependencies(Node node) {
        if (node == null) {
            throw new OurBadException("Unexpected null string while resolving variable dependencies.");
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

    public Void visitFilterExpression(FilterExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        visit(expression.getPredicateExpression(), null);

        addInputVariableDependencies(
            expression,
            getInputVariableDependencies(expression.getPredicateExpression())
        );
        removeInputVariableDependency(expression, Name.CONTEXT_ITEM);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getMainExpression()));
        return null;
    }

    @Override
    public Void visitContextExpr(ContextItemExpression expression, Void argument) {
        addInputVariableDependency(expression, Name.CONTEXT_ITEM);
        return null;
    }

    @Override
    public Void visitInlineFunctionExpr(InlineFunctionExpression expression, Void argument) {
        for (long l : expression.getBodies().keySet()) {
            visit(expression.getBodies().get(l), null);
        }
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
    public Void visitTypeSwitchExpression(TypeSwitchExpression expression, Void argument) {
        visit(expression.getTestCondition(), null);
        for (TypeswitchCase v : expression.getCases()) {
            visit(v.getReturnExpression(), null);
            addInputVariableDependencies(expression, getInputVariableDependencies(v.getReturnExpression()));
            if (v.getVariableName() != null) {
                removeInputVariableDependency(expression, v.getVariableName());
            }
        }
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getTestCondition()));
        return null;
    }

    @Override
    public Void visitNamedFunctionRef(NamedFunctionReferenceExpression expression, Void argument) {
        addInputVariableDependency(expression, expression.getIdentifier().getNameWithArity());
        return null;
    }

    @Override
    public Void visitFunctionCall(FunctionCallExpression expression, Void argument) {
        addInputVariableDependency(
            expression,
            expression.getFunctionIdentifier().getNameWithArity()
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

    private Map<Name, Node> buildNameToNodeMap(Prolog prolog) {
        Map<Name, Node> nameToNodeMap = new TreeMap<>();
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
                System.err.print(variableDeclaration.getVariableName());
                System.err.println(
                    String.join(
                        ", ",
                        getInputVariableDependencies(variableDeclaration).stream()
                            .map(x -> x.toString())
                            .collect(Collectors.toList())
                    )
                );
            }
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            visit(functionDeclaration, null);
            nameToNodeMap.put(functionDeclaration.getFunctionIdentifier().getNameWithArity(), functionDeclaration);
            if (this.rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.err.print(functionDeclaration.getFunctionIdentifier().toString());
                System.err.println(
                    String.join(
                        ", ",
                        getInputVariableDependencies(functionDeclaration).stream()
                            .map(x -> x.toString())
                            .collect(Collectors.toList())
                    )
                );
            }
        }
        for (TypeDeclaration typeDeclaration : prolog.getTypeDeclarations()) {
            visit(typeDeclaration, null);
            nameToNodeMap.put(typeDeclaration.getDefinition().getName(), typeDeclaration);
            if (this.rumbleRuntimeConfiguration.isPrintIteratorTree()) {
                System.err.print(typeDeclaration.getDefinition().getName().toString());
                System.err.println(
                    String.join(
                        ", ",
                        getInputVariableDependencies(typeDeclaration).stream()
                            .map(x -> x.toString())
                            .collect(Collectors.toList())
                    )
                );
            }
        }
        return nameToNodeMap;
    }

    private DirectedAcyclicGraph<Node, DefaultEdge> buildDependencyGraph(
            Map<Name, Node> nameToNodeMap,
            Prolog prolog
    ) {
        DirectedAcyclicGraph<Node, DefaultEdge> dependencyGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        for (VariableDeclaration variableDeclaration : prolog.getVariableDeclarations()) {
            Set<Name> names = getInputVariableDependencies(variableDeclaration);
            dependencyGraph.addVertex(variableDeclaration);
            for (Name name : names) {
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
            Set<Name> names = getInputVariableDependencies(functionDeclaration);
            dependencyGraph.addVertex(functionDeclaration);
            for (Name name : names) {
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
        Map<Name, Node> nameToNodeMap = buildNameToNodeMap(prolog);
        DirectedAcyclicGraph<Node, DefaultEdge> dependencyGraph = buildDependencyGraph(nameToNodeMap, prolog);
        List<Node> resolvedList = new ArrayList<>();
        for (TypeDeclaration typeDeclaration : prolog.getTypeDeclarations()) {
            resolvedList.add(typeDeclaration);
        }
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
            return null;
        }
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration expression, Void argument) {
        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression));
        return null;
    }
}
