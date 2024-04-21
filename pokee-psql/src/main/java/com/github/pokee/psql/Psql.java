package com.github.pokee.psql;

public class Psql {

    public static void main(final String[] args) {
        final Lexer lexer = new Lexer("""
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
                ];""");
        while (lexer.nextToken()) {
            System.out.println(lexer.getCurrentToken().type() + " " + lexer.getCurrentToken().value());
        }
    }

}
