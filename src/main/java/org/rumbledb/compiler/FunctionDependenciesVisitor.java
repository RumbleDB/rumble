package org.rumbledb.compiler;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.FunctionCallExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionDependenciesVisitor extends AbstractNodeVisitor<Name> {

    @Override
    public Name visitForClause(ForClause clause, Name argument) {
        return this.visit(clause.getExpression(), argument);
    }

    @Override
    public Name visitLetClause(LetClause clause, Name argument) {
        return this.visit(clause.getExpression(), argument);
    }

    @Override
    public Name visitGroupByClause(GroupByClause clause, Name argument) {
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            if (variable.getExpression() != null) {
                this.visit(variable.getExpression(), argument);
            }
        }
        return argument;
    }

    public Name visitOrderByClause(OrderByClause clause, Name argument) {
        for (OrderByClauseSortingKey orderClause : clause.getSortingKeys()) {
            this.visit(orderClause.getExpression(), argument);
        }
        return argument;
    }

    @Override
    public Name visitFlowrExpression(FlworExpression expression, Name argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        while (clause != null) {
            this.visit(clause, argument);
            clause = clause.getNextClause();
        }
        return argument;
    }

    private final Map<Name, List<Name>> edges;
    private final Map<Name, FunctionDeclaration> functionDeclarations;

    FunctionDependenciesVisitor() {
        this.edges = new HashMap<>();
        this.functionDeclarations = new HashMap<>();
    }

    private void createVertex(Name name) {
        this.edges.put(name, new ArrayList<>());
    }

    private void createEdge(Name source, Name target) {
        this.edges.get(source).add(target);
    }

    @Override
    public Name visitProlog(Prolog prolog, Name argument) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            visit(declaration, null);
        }
        Graph<Name, DefaultEdge> directedGraph =
            new DefaultDirectedGraph<>(DefaultEdge.class);
        this.edges.keySet().forEach(directedGraph::addVertex);
        this.edges.keySet().forEach(key -> this.edges.get(key).forEach(value -> directedGraph.addEdge(key, value)));
        StrongConnectivityAlgorithm<Name, DefaultEdge> scAlg =
            new KosarajuStrongConnectivityInspector<>(directedGraph);
        // every vertex in a strongly connected component corresponds to a cyclic function call
        List<Graph<Name, DefaultEdge>> stronglyConnectedComponents =
            scAlg.getStronglyConnectedComponents();
        stronglyConnectedComponents.stream()
            .filter(subGraph -> subGraph.edgeSet().size() > 0)
            .flatMap(subGraph -> subGraph.vertexSet().stream())
            .forEach(name -> this.functionDeclarations.get(name).setRecursive(true));
        return null;
    }

    public Name visitFunctionDeclaration(FunctionDeclaration expression, Name argument) {
        Name name = expression.getFunctionIdentifier().getName();
        this.functionDeclarations.put(name, expression);
        createVertex(name);
        visit(expression.getExpression(), name);
        return null;
    }

    public Name visitFunctionCall(FunctionCallExpression expression, Name argument) {
        if (argument == null) {
            return defaultAction(expression, null);
        }
        createEdge(argument, expression.getFunctionName());
        return null;
    }
}
