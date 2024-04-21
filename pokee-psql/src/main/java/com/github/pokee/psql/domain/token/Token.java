package com.github.pokee.psql.domain.token;

import com.github.pokee.psql.domain.token.support.TokenType;

/**
 * Represents a token in the query.
 *
 * @param type  TokenType
 * @param value String
 */
public record Token(TokenType type, String value, int startIndex, int endIndex) {

}
