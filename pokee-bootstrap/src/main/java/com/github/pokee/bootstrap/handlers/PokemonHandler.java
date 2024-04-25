package com.github.pokee.bootstrap.handlers;

import com.github.pokee.common.LocalizedString;
import com.github.pokee.common.Pokemon;
import com.github.pokee.common.fielder.Fielder;
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.PsonBuilder;
import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProgramContext;
import com.github.pokee.psql.query.NamespaceValues;
import com.github.pokee.psql.query.QueryExecutor;
import com.github.pokee.psql.visitor.impl.InterpreterVisitor;
import com.github.pokee.psql.visitor.impl.NamespaceAnalyzerVisitor;
import com.github.pokee.psql.visitor.impl.SemanticAnalyzerVisitor;
import com.github.pokee.pswf.annotation.RoutePrefix;
import com.github.pokee.pswf.annotation.data.Param;
import com.github.pokee.pswf.annotation.data.Query;
import com.github.pokee.pswf.annotation.generator.ContentType;
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.response.ResponseBuilder;
import com.github.pokee.pswf.response.StatusCode;
import com.github.pokee.pswf.util.ContentTypes;

import java.util.*;

@RoutePrefix("/pokemon")
public class PokemonHandler {

    private final List<Pokemon> pokemonList;
    private final Map<String, NamespaceValues> namespaceValues;

    public PokemonHandler(final List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;

        this.namespaceValues = new HashMap<>();
        final NamespaceValues pokemonValues = new NamespaceValues(
                pokemonList,
                new Pokemon(1, LocalizedString.fromString(""), LocalizedString.fromString(""), 0, 0, 0, 0)
        );
        this.namespaceValues.put("Pokemon", pokemonValues);
    }

    private PsonBuilder createLocalizedPson(final String language) {
        return Pson.create().registerValueWriterMapper(
                LocalizedString.class,
                (writer, bob, field, value) -> writer.writeString(bob, ((LocalizedString) value).get(language))
        );
    }

    @GET("/")
    @ContentType(ContentTypes.JSON)
    public String handleListAll(@Query(value = "lang", fallback = "en") final String language) {
        final Pson pson = this.createLocalizedPson(language)
                .prettyPrint()
                .build();
        return pson.marshal(this.pokemonList);
    }

    @GET("/:id")
    @ContentType(ContentTypes.JSON)
    public Response handleGetById(@Param("id") final int id,
                                  @Query(value = "lang", fallback = "en") final String language) {
        final Pson pson = this.createLocalizedPson(language)
                .prettyPrint()
                .build();

        final Optional<Pokemon> pokemon = this.pokemonList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
        if (pokemon.isEmpty()) {
            return new ResponseBuilder()
                    .status(StatusCode.NOT_FOUND)
                    .text("Pokemon not found")
                    .build();
        }

        return new ResponseBuilder()
                .json(pson.marshal(pokemon.get()))
                .build();
    }

    @GET("/query")
    public Response handleQuery(@Query("query") final String query,
                                @Query(value = "lang", fallback = "en") final String language) {
        final Pson pson = this.createLocalizedPson(language)
                .prettyPrint()
                .build();

        final Lexer lexer = new Lexer(query);
        final Parser parser = new Parser(lexer);

        try {
            final ProgramContext program = parser.parseProgram();

            // make sure all namespaces are defined and parse aliases
            final Map<String, String> importedAliases = new HashMap<>();
            final NamespaceAnalyzerVisitor namespaceAnalyzerVisitor = new NamespaceAnalyzerVisitor(
                    this.namespaceValues.keySet(),
                    importedAliases
            );
            program.accept(namespaceAnalyzerVisitor);

            // do semantic analysis
            program.accept(new SemanticAnalyzerVisitor(
                    List.of("de", "en", "fr", "es"))
            );

            // parse queries
            final List<InterpreterVisitor.Query> queries = new ArrayList<>();
            program.accept(new InterpreterVisitor(
                    queries,
                    language,
                    importedAliases,
                    this.namespaceValues
            ));
            if (queries.isEmpty()) {
                return new ResponseBuilder()
                        .status(StatusCode.BAD_REQUEST)
                        .text("No queries found")
                        .build();
            }

            // run queries
            final Map<String, List<Fielder>> result = QueryExecutor.execute(
                    queries,
                    importedAliases,
                    this.namespaceValues
            );

            final Map<String, List<Map<String, Object>>> resultFlat = new HashMap<>();
            for (final Map.Entry<String, List<Fielder>> entry : result.entrySet()) {
                final List<Map<String, Object>> flat = new ArrayList<>();
                for (final Fielder fielder : entry.getValue()) {
                    final Map<String, Object> current = new HashMap<>();
                    for (final String field : fielder.getFields()) {
                        current.put(field, fielder.getField(field));
                    }
                    flat.add(current);
                }
                resultFlat.put(entry.getKey(), flat);
            }

            return new ResponseBuilder()
                    .status(StatusCode.OK)
                    .json(pson.marshal(resultFlat))
                    .build();
        } catch (final Exception exception) {
            return new ResponseBuilder()
                    .status(StatusCode.BAD_REQUEST)
                    .text(exception.getClass().getSimpleName() + ": " + exception.getMessage())
                    .build();
        }
    }

}
