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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import org.rumbledb.config.RumbleConfiguration;
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
import org.rumbledb.expressions.flowr.WindowClause;
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
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.TransformExpression;

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
 * Functions may depend on each other, including through mutually recursive calls.
 *
 * The visitor groups recursive functions and topologically orders the resulting DAG to re-sort
 * declarations in the prolog for further processing by other visitors.
 *
 */
public class VariableDependenciesVisitor extends AbstractNodeVisitor<Void> {

    @SuppressWarnings("unused")
    private final RumbleConfiguration configuration;

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
     * @param coniguration the configuration. This is used for trigerring or not debug output.
     */
    VariableDependenciesVisitor(RumbleConfiguration coniguration) {
        this.outputVariableDependenciesForClauses = new HashMap<>();
        this.inputVariableDependencies = new HashMap<>();
        this.configuration = coniguration;
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

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitWindowClause(WindowClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getWindowVariable());
        expression
                .getStartCondition()
                .variables()
                .names()
                .forEach(name -> addOutputVariableDependency(expression, name));
        if (expression.getEndCondition() != null) {
            expression
                    .getEndCondition()
                    .variables()
                    .names()
                    .forEach(name -> addOutputVariableDependency(expression, name));
        }
        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));
        visit(expression.getStartCondition().expression(), null);
        addInputVariableDependencies(
                expression,
                getInputVariableDependencies(expression.getStartCondition().expression()));
        if (expression.getEndCondition() != null) {
            visit(expression.getEndCondition().expression(), null);
            addInputVariableDependencies(
                    expression,
                    getInputVariableDependencies(expression.getEndCondition().expression()));
        }
        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitLetClause(LetClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        addOutputVariableDependency(expression, expression.getVariableName());

        visit(expression.getExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
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

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitOrderByClause(OrderByClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        visit(expression.getPreviousClause(), null);
        for (OrderByClauseSortingKey var : expression.getSortingKeys()) {
            visit(var.getExpression(), null);
            addInputVariableDependencies(expression, getInputVariableDependencies(var.getExpression()));
        }

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitWhereClause(WhereClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        visit(expression.getWhereExpression(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getWhereExpression()));

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitCountClause(CountClause expression, Void argument) {
        visit(expression.getPreviousClause(), null);
        addOutputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitReturnClause(ReturnClause expression, Void argument) {
        visit(expression.getReturnExpr(), null);
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getReturnExpr()));

        removeInputVariableDependencies(expression, getOutputVariableDependencies(expression.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitFilterExpression(FilterExpression expression, Void argument) {
        visit(expression.getMainExpression(), null);
        visit(expression.getPredicateExpression(), null);

        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getPredicateExpression()));
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
                expression, expression.getFunctionIdentifier().getNameWithArity());
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
                        variableDeclaration.getVariableName(), variableDeclaration.getMetadata());
            }
            visit(variableDeclaration, null);
            nameToNodeMap.put(variableDeclaration.getVariableName(), variableDeclaration);
        }
        for (FunctionDeclaration functionDeclaration : prolog.getFunctionDeclarations()) {
            visit(functionDeclaration, null);
            nameToNodeMap.put(functionDeclaration.getFunctionIdentifier().getNameWithArity(), functionDeclaration);
        }
        for (TypeDeclaration typeDeclaration : prolog.getTypeDeclarations()) {
            visit(typeDeclaration, null);
            nameToNodeMap.put(typeDeclaration.getDefinition().getName(), typeDeclaration);
        }
        return nameToNodeMap;
    }

    private Graph<Node, DefaultEdge> buildDependencyGraph(Map<Name, Node> nameToNodeMap, Prolog prolog) {
        Graph<Node, DefaultEdge> dependencyGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (Node declaration : prolog.getDeclarations()) {
            if (!(declaration instanceof VariableDeclaration) && !(declaration instanceof FunctionDeclaration)) {
                continue;
            }
            dependencyGraph.addVertex(declaration);
            for (Name name : getInputVariableDependencies(declaration)) {
                Node dependency = nameToNodeMap.get(name);
                if (dependency != null) {
                    dependencyGraph.addVertex(dependency);
                    dependencyGraph.addEdge(dependency, declaration);
                }
            }
        }
        return dependencyGraph;
    }

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        Map<Name, Node> nameToNodeMap = buildNameToNodeMap(prolog);
        Graph<Node, DefaultEdge> dependencyGraph = buildDependencyGraph(nameToNodeMap, prolog);
        var components = new KosarajuStrongConnectivityInspector<>(dependencyGraph);
        // Function-only cycles are legal recursion; cycles involving variables cannot be initialized.
        for (Graph<Node, DefaultEdge> component : components.getStronglyConnectedComponents()) {
            if (component.edgeSet().isEmpty()) {
                continue;
            }
            for (Node declaration : prolog.getDeclarations()) {
                if (declaration instanceof VariableDeclaration && component.containsVertex(declaration)) {
                    throw new CycleInVariableDeclarationsException(
                            "There is a cycle in the dependencies in the variable and function declarations. It is thus impossible to build the dynamic context.",
                            declaration.getMetadata());
                }
            }
        }
        List<Node> resolvedList = new ArrayList<>();
        for (TypeDeclaration typeDeclaration : prolog.getTypeDeclarations()) {
            resolvedList.add(typeDeclaration);
        }
        for (Node declaration : prolog.getDeclarations()) {
            if (!(declaration instanceof TypeDeclaration)
                    && !(declaration instanceof VariableDeclaration)
                    && !(declaration instanceof FunctionDeclaration)) {
                resolvedList.add(declaration);
            }
        }
        Map<Node, Integer> declarationOrder = new HashMap<>();
        for (int i = 0; i < prolog.getDeclarations().size(); i++) {
            declarationOrder.put(prolog.getDeclarations().get(i), i);
        }
        // Keep independent groups in declaration order as well.
        Iterator<Graph<Node, DefaultEdge>> iterator = new TopologicalOrderIterator<>(
                components.getCondensation(), Comparator.comparingInt(group -> group.vertexSet().stream()
                        .mapToInt(declarationOrder::get)
                        .min()
                        .orElseThrow()));
        while (iterator.hasNext()) {
            Set<Node> group = iterator.next().vertexSet();
            // Register the entire recursive group before evaluating any dependent declaration.
            for (Node declaration : prolog.getDeclarations()) {
                if (group.contains(declaration) && !(declaration instanceof TypeDeclaration)) {
                    resolvedList.add(declaration);
                }
            }
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
        addInputVariableDependencies(expression, getInputVariableDependencies(expression.getExpression()));
        return null;
    }

    @Override
    public Void visitTransformExpression(TransformExpression expression, Void argument) {
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            visit(copyDecl.getSourceExpression(), null);
            addInputVariableDependencies(expression, getInputVariableDependencies(copyDecl.getSourceExpression()));
        }
        visit(expression.getModifyExpression(), null);
        visit(expression.getReturnExpression(), null);

        return null;
    }

    @Override
    public Void visitApplyStatement(ApplyStatement statement, Void argument) {
        visit(statement.getApplyExpression(), null);
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getApplyExpression()));
        return null;
    }

    @Override
    public Void visitAssignStatement(AssignStatement statement, Void argument) {
        visit(statement.getAssignExpression(), null);
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getAssignExpression()));
        return null;
    }

    @Override
    public Void visitExitStatement(ExitStatement statement, Void argument) {
        visit(statement.getExitExpression(), null);
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getExitExpression()));
        return null;
    }

    @Override
    public Void visitReturnStatementClause(ReturnStatementClause clause, Void argument) {
        visit(clause.getReturnStatement(), null);
        addInputVariableDependencies(clause, getInputVariableDependencies(clause.getReturnStatement()));

        removeInputVariableDependencies(clause, getOutputVariableDependencies(clause.getPreviousClause()));
        return null;
    }

    @Override
    public Void visitTypeSwitchStatement(TypeSwitchStatement statement, Void argument) {
        visit(statement.getTestCondition(), null);
        for (TypeSwitchStatementCase tswc : statement.getCases()) {
            visit(tswc.getReturnStatement(), null);
            addInputVariableDependencies(statement, getOutputVariableDependencies(tswc.getReturnStatement()));
            if (tswc.getVariableName() != null) {
                removeInputVariableDependency(statement, tswc.getVariableName());
            }
        }
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getTestCondition()));
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement, Void argument) {
        visit(statement.getTestCondition(), null);
        visit(statement.getStatement(), null);
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getStatement()));
        addInputVariableDependencies(statement, getInputVariableDependencies(statement.getTestCondition()));
        return null;
    }

    @Override
    public Void visitVariableDeclStatement(VariableDeclStatement statement, Void argument) {
        if (statement.getVariableExpression() != null) {
            visit(statement.getVariableExpression(), null);
            addInputVariableDependencies(statement, getInputVariableDependencies(statement.getVariableExpression()));
        }
        return null;
    }
}
