package com.github.pokee.psql.query;

import com.github.pokee.psql.domain.token.support.TokenType;

import java.util.List;

public record QueryFilterFunction(List<TokenType> argumentTypes,
                                  QueryFilterFunctionRunner runner) {
}