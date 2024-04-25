package com.github.pokee.psql.query;

import com.github.pokee.common.fielder.Fielder;
import com.github.pokee.common.fielder.MapFielder;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProjectionNode;
import com.github.pokee.psql.visitor.impl.InterpreterVisitor;

import java.util.*;

public class QueryExecutor {

    // functions contains the functions that can be used in the query.
    private static final Map<Class<?>, Map<String, QueryFilterFunction>> functions =
            QueryExecutor.createFunctions();

    /**
     * Returns the functions that can be used in the query.
     *
     * @return the functions that can be used in the query.
     */
    public static Map<Class<?>, Map<String, QueryFilterFunction>> getFunctions() {
        return QueryExecutor.functions;
    }

    /**
     * Processes a list of queries to produce a mapped result where each query name is associated
     * with a list of {@link com.github.pokee.common.fielder.Fielder} instances that match the query criteria. Each query involves
     * applying a set of predicates on fielder values from a specific namespace, and then mapping
     * the resulting fielders according to the specified projections.
     *
     * <p>This method iterates through each query, applying the predicates to each Fielder in the
     * relevant namespace. If a Fielder satisfies all the predicates, it is then transformed according
     * to the query's projections. Projections may involve renaming fields (aliases), selecting specific
     * fields, or including all fields from the Fielder. The result for each query is collected into
     * a list which is then mapped to the query's name.</p>
     *
     * @param queries            A list of {@link InterpreterVisitor.Query} objects, where each Query contains the information
     *                           needed to execute a single query, including name, projections, and predicates.
     * @param importedNamespaces A map of namespace aliases imported in the context, where each key is
     *                           a namespace identifier, and the associated value is the aliased namespace.
     * @param namespaceValues    A map where each key is a namespace, and the value is an instance of
     *                           {@link NamespaceValues}, which contains values corresponding to
     *                           that namespace.
     * @return A map where each key is a query name, and the value is a list of {@link Fielder}
     * objects that represent the result of applying the query's projections to the matching
     * fielders. Each Fielder in the list represents a set of fields after applying the
     * query's projections.
     * @throws com.github.pokee.psql.exception.ExpressionException If any of the query filter expressions evaluates to null, indicating
     *                                                             an issue in the filter expression setup.
     */
    public static Map<String, List<Fielder>> execute(final List<InterpreterVisitor.Query> queries,
                                                     final Map<String, String> importedNamespaces,
                                                     final Map<String, NamespaceValues> namespaceValues) {
        // result contains the final result of the query execution.
        // query name -> query result
        final Map<String, List<Fielder>> result = new HashMap<>();

        for (final InterpreterVisitor.Query query : queries) {
            final List<Fielder> values = new ArrayList<>();

            // Extracting the namespace once as it's reused in the loop.
            final ProjectionNode first = query.projections().get(0);
            final String namespace = importedNamespaces.get(first.getField().getNamespace().getText());
            final NamespaceValues a = namespaceValues.get(namespace);

            for (final Fielder value : a.values()) {
                // Using stream to simplify predicate application.
                final boolean allMatch = query.predicate() == null ||
                        query.predicate().stream().allMatch(pred -> pred.test(value));
                if (!allMatch) {
                    continue;
                }

                // map fielder is a simplified version of fielder that can accept an arbitrary number of fields.
                final MapFielder mapFielder = new MapFielder();
                for (final ProjectionNode projection : query.projections()) {
                    if (projection.isAll()) {
                        Arrays.stream(value.getFields()).forEach(field -> mapFielder.put(field, value.getField(field)));
                        continue;
                    }

                    final String target = projection.getField().getField().getText();
                    final String key = projection.hasAlias() ? projection.getAlias().getText() : target;

                    mapFielder.put(key, value.hasField(target) ? value.getField(target) : null);
                }
                values.add(mapFielder);
            }

            result.put(query.name(), values);
        }

        return result;
    }

    /**
     * Creates a map of functions that can be used in the query.
     *
     * @return a map of functions that can be used in the query.
     */
    private static Map<Class<?>, Map<String, QueryFilterFunction>> createFunctions() {
        final Map<Class<?>, Map<String, QueryFilterFunction>> functions = new HashMap<>();

        // string functions
        final Map<String, QueryFilterFunction> stringFieldFunctionMap = new HashMap<>();

        // starts_with function checks if the string starts with the given string.
        stringFieldFunctionMap.put("starts_with", new QueryFilterFunction(
                List.of(TokenType.STRING_LITERAL),
                (object, args) -> ((String) object).startsWith(args.get(0).getText())
        ));

        functions.put(String.class, stringFieldFunctionMap);

        /// integer functions
        final Map<String, QueryFilterFunction> integerFieldFunctionMap = new HashMap<>();

        // gt function checks if the integer is greater than the given integer.
        integerFieldFunctionMap.put("gt", new QueryFilterFunction(
                List.of(TokenType.NUMBER),
                (object, args) -> (Integer) object > Integer.parseInt(args.get(0).getText())
        ));

        // gte function checks if the integer is greater than or equal to the given integer.
        integerFieldFunctionMap.put("gte", new QueryFilterFunction(
                List.of(TokenType.NUMBER),
                (object, args) -> (Integer) object >= Integer.parseInt(args.get(0).getText())
        ));

        // lt function checks if the integer is less than the given integer.
        integerFieldFunctionMap.put("lt", new QueryFilterFunction(
                List.of(TokenType.NUMBER),
                (object, args) -> (Integer) object < Integer.parseInt(args.get(0).getText())
        ));

        // lte function checks if the integer is less than or equal to the given integer.
        integerFieldFunctionMap.put("lte", new QueryFilterFunction(
                List.of(TokenType.NUMBER),
                (object, args) -> (Integer) object <= Integer.parseInt(args.get(0).getText())
        ));

        // eq function checks if the integer is equal to the given integer.
        integerFieldFunctionMap.put("eq", new QueryFilterFunction(
                List.of(TokenType.NUMBER),
                (object, args) -> (Integer) object == Integer.parseInt(args.get(0).getText())
        ));
        functions.put(Integer.class, integerFieldFunctionMap);

        return functions;
    }

}