package org.rumbledb.compiler;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.FunctionCallExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionDependenciesVisitor extends AbstractNodeVisitor<FunctionIdentifier> {

    @Override
    public FunctionIdentifier visitForClause(ForClause clause, FunctionIdentifier argument) {
        return this.visit(clause.getExpression(), argument);
    }

    @Override
    public FunctionIdentifier visitLetClause(LetClause clause, FunctionIdentifier argument) {
        return this.visit(clause.getExpression(), argument);
    }

    @Override
    public FunctionIdentifier visitGroupByClause(GroupByClause clause, FunctionIdentifier argument) {
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            if (variable.getExpression() != null) {
                this.visit(variable.getExpression(), argument);
            }
        }
        return argument;
    }

    public FunctionIdentifier visitOrderByClause(OrderByClause clause, FunctionIdentifier argument) {
        for (OrderByClauseSortingKey orderClause : clause.getSortingKeys()) {
            this.visit(orderClause.getExpression(), argument);
        }
        return argument;
    }

    @Override
    public FunctionIdentifier visitFlowrExpression(FlworExpression expression, FunctionIdentifier argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        while (clause != null) {
            this.visit(clause, argument);
            clause = clause.getNextClause();
        }
        return argument;
    }

    private final Map<FunctionIdentifier, List<FunctionIdentifier>> edges;
    private final Map<FunctionIdentifier, FunctionDeclaration> functionDeclarations;

    FunctionDependenciesVisitor() {
        this.edges = new HashMap<>();
        this.functionDeclarations = new HashMap<>();
    }

    private void createVertex(FunctionIdentifier name) {
        this.edges.put(name, new ArrayList<>());
    }

    private void createEdge(FunctionIdentifier source, FunctionIdentifier target) {
        this.edges.get(source).add(target);
    }

    @Override
    public FunctionIdentifier visitMainModule(MainModule expression, FunctionIdentifier argument) {
        visitDescendants(expression, argument);
        Graph<FunctionIdentifier, DefaultEdge> directedGraph =
            new DefaultDirectedGraph<>(DefaultEdge.class);
        this.edges.keySet().forEach(directedGraph::addVertex);
        this.edges.keySet()
            .forEach(
                key -> this.edges.get(key)
                    .stream()
                    .filter(this.edges::containsKey)
                    .forEach(value -> directedGraph.addEdge(key, value))
            );
        StrongConnectivityAlgorithm<FunctionIdentifier, DefaultEdge> scAlg =
            new KosarajuStrongConnectivityInspector<>(directedGraph);
        // every vertex in a strongly connected component corresponds to a cyclic function call
        List<Graph<FunctionIdentifier, DefaultEdge>> stronglyConnectedComponents =
            scAlg.getStronglyConnectedComponents();
        stronglyConnectedComponents.stream()
            .filter(subGraph -> subGraph.edgeSet().size() > 0)
            .flatMap(subGraph -> subGraph.vertexSet().stream())
            .forEach(name -> this.functionDeclarations.get(name).setRecursive(true));
        return null;
    }

    @Override
    public FunctionIdentifier visitProlog(Prolog prolog, FunctionIdentifier argument) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            visit(declaration, null);
        }
        for (LibraryModule libraryModule : prolog.getImportedModules()) {
            visit(libraryModule, null);
        }
        return null;
    }

    public FunctionIdentifier visitFunctionDeclaration(FunctionDeclaration expression, FunctionIdentifier argument) {
        FunctionIdentifier name = expression.getFunctionIdentifier();
        this.functionDeclarations.put(name, expression);
        createVertex(name);
        visit(expression.getExpression(), name);
        return argument;
    }

    public FunctionIdentifier visitFunctionCall(FunctionCallExpression expression, FunctionIdentifier argument) {
        if (argument == null) {
            return defaultAction(expression, null);
        }
        createEdge(argument, expression.getFunctionIdentifier());
        visitDescendants(expression, argument);
        return argument;
    }
}
