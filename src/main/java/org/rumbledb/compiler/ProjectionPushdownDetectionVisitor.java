package org.rumbledb.compiler;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.*;

import java.util.HashMap;
import java.util.Map;


public class ProjectionPushdownDetectionVisitor
        extends
            AbstractNodeVisitor<ProjectionPushdownDetectionVisitor.ReferenceMap> {

    /*******************************
     * High-level explanations
     *
     * When visiting an expression, the ReferenceMap argument provides the projection
     * expected from the outside world, with the ReferenceMap keys being object keys.
     *
     * If the ReferenceMap has no keys, then the entire object is needed.
     *
     * The returned ReferenceMap contains all the variables that this expression needs
     * to access, together with the projections that they need in case these variables
     * are associated with objects (if the associated projection has no keys, then the
     * entire object is expected for this variable).
     *
     * When visiting a clause, the ReferenceMap argument provides the variables that
     * subsequenc clauses need to access, together with the projections that they need
     * in case these variables are associated with objects (if the associated projection
     * has no keys, then the entire object is expected for this variable).
     *
     * The returned ReferenceMap contains all the variables that this clause needs
     * to access, together with the projections that they need in case these variables
     * are associated with objects (if the associated projection has no keys, then the
     * entire object is expected for this variable).
     */

    @Override
    public ReferenceMap visitForClause(ForClause clause, ReferenceMap argument) {
        ReferenceMap result = new ReferenceMap(argument);
        // we fetch the references (nested keys) made by subsequent clauses to the current for variable
        ReferenceMap forReferenceMap = argument.getReferenceMap(clause.getVariableName());
        // we recursively visit the for expression, passing the references made to it
        ReferenceMap resultMap = this.visit(clause.getExpression(), forReferenceMap);
        result.drop(clause.getVariableName());
        if (clause.getPositionalVariableName() != null) {
            argument.drop(clause.getPositionalVariableName());
        }
        result.add(resultMap);
        // returns the references made by the for clause and subsequent clauses
        // (will be passed to previous clauses and their subexpressions)
        return result;
    }

    // The default action is to just return a copy of the passed map, extended
    // with all references made by the expression.
    @Override
    protected ReferenceMap defaultAction(Node node, ReferenceMap argument) {
        // We make a copy of the references made by the outside world
        ReferenceMap result = new ReferenceMap(argument);
        for (Node child : node.getChildren()) {
            // We pass an empty reference map in the general case.
            ReferenceMap map = visit(child, new ReferenceMap());
            // and add any new references
            result.add(map);
        }
        // We return all references made by the outside world augmented by those made by this expression
        return result;
    }

    @Override
    public ReferenceMap visitLetClause(LetClause clause, ReferenceMap argument) {
        // argument: the references made by subsequent clauses
        if (clause.getPreviousClause() != null && !argument.containsKey(clause.getVariableName())) {
            clause.setReferenced(false);
            return argument;
        }
        ReferenceMap result = new ReferenceMap(argument);
        // we fetch the references (nested keys) made by subsequent clauses to the current let variable
        ReferenceMap letReferenceMap = argument.getReferenceMap(clause.getVariableName());
        // we recursively visit the let expression, passing the references made to it
        ReferenceMap resultMap = this.visit(clause.getExpression(), letReferenceMap);
        result.drop(clause.getVariableName());
        result.add(resultMap);
        // returns the references made by the let clause and subsequent clauses
        // (will be passed to previous clauses and their subexpressions)
        return result;
    }

    @Override
    public ReferenceMap visitGroupByClause(GroupByClause clause, ReferenceMap argument) {
        ReferenceMap result = new ReferenceMap(argument);
        clause.getGroupVariables()
            .stream()
            .filter(expr -> expr.getExpression() != null)
            .map(expr -> visit(expr.getExpression(), new ReferenceMap()))
            .forEach(result::add);
        // drop variables that are introduced in the group by clause
        clause.getGroupVariables()
            .stream()
            .filter(expr -> expr.getExpression() != null)
            .map(GroupByVariableDeclaration::getVariableName)
            .forEach(result::drop);
        // add variables that are referenced in the group by clause
        clause.getGroupVariables()
            .stream()
            .filter(expr -> expr.getExpression() == null)
            .map(GroupByVariableDeclaration::getVariableName)
            .forEach(variable -> result.add(variable, new ReferenceMap()));
        return result;
    }

    @Override
    public ReferenceMap visitWhereClause(WhereClause expression, ReferenceMap argument) {
        // we create a copy of the references made by the outside world
        ReferenceMap result = new ReferenceMap(argument);
        // we add references made by the where clause
        // since the where clause has no variables, we only collect new references.
        result.add(visit(expression.getWhereExpression(), new ReferenceMap()));
        // returns the references made by the where clause and the outside world
        // (will be passed to previous clauses and their subexpressions)
        return result;
    }

    @Override
    public ReferenceMap visitCountClause(CountClause expression, ReferenceMap argument) {
        // we create a copy of the references made by the outside world
        ReferenceMap result = new ReferenceMap(argument);
        // since the count clause has its own variable, we remove it from the references
        result.drop(expression.getCountVariableName());
        // returns the references made by the count clause and the outside world
        // (will be passed to previous clauses and their subexpressions)
        return result;
    }

    public ReferenceMap visitOrderByClause(OrderByClause clause, ReferenceMap argument) {
        // we create a copy of the references made by the outside world
        ReferenceMap result = new ReferenceMap(argument);
        // since the count clause only looks up variable values, we add them
        // to the references made
        clause.getSortingKeys()
            .stream()
            .map(OrderByClauseSortingKey::getExpression)
            .map(expr -> visit(expr, new ReferenceMap()))
            .forEach(result::add);
        // returns the references made by the order by clause and the outside world
        // (will be passed to previous clauses and their subexpressions)
        return result;
    }

    @Override
    public ReferenceMap visitReturnClause(ReturnClause expression, ReferenceMap argument) {
        // We forward the needed projection to the retun expression and returns its referenced variables.
        return visit(expression.getReturnExpr(), argument);
    }

    @Override
    public ReferenceMap visitFlowrExpression(FlworExpression expression, ReferenceMap argument) {
        Clause clause = expression.getReturnClause();
        // we go through the clauses from the last one to the first one, passing and updating the references
        // starting with the references made by the outside world
        while (clause != null) {
            argument = this.visit(clause, argument);
            clause = clause.getPreviousClause();
        }
        // returns a copy of the references made by the FLWOR expression and the outside world
        // (will be passed to previous clauses and their subexpressions)
        return new ReferenceMap(argument);
    }

    @Override
    public ReferenceMap visitMainModule(MainModule expression, ReferenceMap argument) {
        visit(expression.getExpression(), new ReferenceMap());
        return null;
    }

    @Override
    public ReferenceMap visitProlog(Prolog prolog, ReferenceMap argument) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            visit(declaration, null);
        }
        for (LibraryModule libraryModule : prolog.getImportedModules()) {
            visit(libraryModule, null);
        }
        return null;
    }

    @Override
    public ReferenceMap visitVariableReference(VariableReferenceExpression expression, ReferenceMap argument) {
        // argument contains all the references to keys of objects returned by this expression.
        // we return a reference map that indicates that this expression fetches these keys for this very variable.
        ReferenceMap map = new ReferenceMap();
        map.add(expression.getVariableName(), argument);
        return map;
    }

    @Override
    public ReferenceMap visitObjectLookupExpression(ObjectLookupExpression expression, ReferenceMap argument) {
        if (expression.getLookupExpression() instanceof StringLiteralExpression) {
            ReferenceMap map = new ReferenceMap();
            map.add(
                Name.createVariableInNoNamespace(
                    ((StringLiteralExpression) expression.getLookupExpression()).getValue()
                ),
                argument
            );
            return visit(expression.getMainExpression(), map);
        }
        // In the general case, we return all references made by the current expression.
        ReferenceMap result = new ReferenceMap();
        result.add(visit(expression.getMainExpression(), new ReferenceMap()));
        result.add(visit(expression.getLookupExpression(), new ReferenceMap()));
        return result;
    }

    @Override
    public ReferenceMap visitObjectConstructor(ObjectConstructorExpression expression, ReferenceMap argument) {
        if (expression.isMergedConstructor()) {
            return this.defaultAction(expression, argument);
        }
        if (!expression.getKeys().stream().allMatch(exp -> exp instanceof StringLiteralExpression)) {
            return this.defaultAction(expression, argument);
        }
        ReferenceMap result = new ReferenceMap();
        for (int i = 0; i < expression.getKeys().size(); i++) {
            StringLiteralExpression key = (StringLiteralExpression) expression.getKeys().get(i);
            Name name = Name.createVariableInNoNamespace(key.getValue());
            if (!argument.isEmpty() && !argument.containsKey(name)) {
                // this key is not referenced, so we deactivate it and ignore what it references.
                expression.setReferenced(i, false);
            } else {
                // this key may be referenced, so we collect all variables it references.
                result.add(visit(expression.getValues().get(i), argument.getReferenceMap(name)));
            }
        }
        return result;
    }

    @Override
    public ReferenceMap visitFilterExpression(FilterExpression expression, ReferenceMap argument) {
        ReferenceMap result = new ReferenceMap();
        if (expression.getPredicateExpression() instanceof IntegerLiteralExpression) {
            expression.getChildren().stream().map(child -> visit(child, argument)).forEach(result::add);
        } else {
            expression.getChildren().stream().map(child -> visit(child, new ReferenceMap())).forEach(result::add);
        }
        return result;
    }

    @Override
    public ReferenceMap visitFunctionCall(FunctionCallExpression expression, ReferenceMap argument) {
        return defaultAction(expression, new ReferenceMap());
    }

    public static class ReferenceMap {

        private Map<Name, ReferenceMap> map;

        public ReferenceMap() {
            this.map = new HashMap<>();
        }

        public ReferenceMap(ReferenceMap other) {
            this.map = new HashMap<>();
            for (Map.Entry<Name, ReferenceMap> m : other.map.entrySet()) {
                this.map.put(m.getKey(), m.getValue());
            }
        }

        public void add(ReferenceMap object) {
            for (Map.Entry<Name, ReferenceMap> m : object.map.entrySet()) {
                add(m.getKey(), m.getValue());
            }
        }

        private void add(Name name, ReferenceMap object) {
            if (!this.map.containsKey(name)) {
                this.map.put(name, object);
            } else {
                ReferenceMap reference = this.map.get(name);
                if (object.isEmpty() || reference.isEmpty()) {
                    // if either this or the other is empty, then all fields are required
                    reference.map.clear();
                } else if (object.containsKey(name)) {
                    if (object.getReferenceMap(name).isEmpty()) {
                        reference.map.clear();
                    } else {
                        reference.add(object.getReferenceMap(name));
                    }
                } else {
                    reference.add(object);
                }
            }
        }

        public void drop(Name name) {
            this.map.remove(name);
        }

        public ReferenceMap getReferenceMap(Name name) {
            if (this.map.containsKey(name)) {
                return this.map.get(name);
            }
            return new ReferenceMap();
        }

        public boolean containsKey(Name name) {
            return this.map.containsKey(name);
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<Name, ReferenceMap> entry : this.map.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(entry.getKey().toString());
                sb.append(": ");
                sb.append(entry.getValue().toString());
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
