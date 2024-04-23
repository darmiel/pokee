package com.github.pokee.psql;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProgramContext;
import com.github.pokee.psql.exception.ParseException;
import com.github.pokee.psql.visitor.SemanticAnalyzerVisitor;

import java.util.List;

public class Psql {

    public static void main2(final String[] args) throws ParseException {
        final String query = """
                use Pokemon as P;
                use Sprites as S;
                                
                my_query P::{
                    name,
                    type,
                    hp as health,
                    S::sprite as sprite_url <-> P::name == S::name
                } filter [
                    P::name.startsWith("Pika")
                ] map [
                    P::name,
                    P::type,
                    P::hp
                ];""";
        final Lexer lexer = new Lexer(query);
//        while (lexer.nextToken()) {
//            System.out.println(lexer.getCurrentToken().type() + " " + lexer.getCurrentToken().value());
//        }
//        lexer.reset();

        final Parser parser = new Parser(lexer);
        System.out.println(parser.parseUseAliasContext());
    }

    public static void main(String[] args) throws ParseException {
        final String query = """
                use Pokemon as P;
                use Pokemon as F;
                            
                query test P::*;
                query test2 Feee::*;
                """;
        final Lexer lexer = new Lexer(query);

        // parse program
        final Parser parser = new Parser(lexer);
        final ProgramContext program = parser.parseProgram();
        System.out.println(program);

        // run semantic analysis
        program.accept(new SemanticAnalyzerVisitor(SemanticAnalyzerVisitor.EXAMPLE_NAMESPACE_PROJECTIONS, List.of("de", "en")));

    }
//
//    public static void main(String[] args) throws ParseException {
//        final String query = """
//                    P::name.startsWith()
//                """;
//        final Lexer lexer = new Lexer(query);
//        final Parser parser = new Parser(lexer);
//        System.out.println(parser.parseFunction());
//    }
//
}
