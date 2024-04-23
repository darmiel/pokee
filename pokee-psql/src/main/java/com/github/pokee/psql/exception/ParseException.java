package com.github.pokee.psql.exception;

import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParseException extends Exception {

    private final int index;
    private final Token currentToken;

    private final String formattedError;
    private final String message;

    public ParseException(final int index, final Token currentToken, final String formattedError, final String message) {
        this.index = index;
        this.currentToken = currentToken;

        this.formattedError = formattedError;
        this.message = message;
    }

    public ParseException(final int index, final Token currentToken, final String formattedError) {
        this(index, currentToken, formattedError, "Error parsing query");
    }

    public int getIndex() {
        return index;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getFormattedError() {
        return formattedError;
    }

    /**
     * Constructs a detailed error message showing the exact location of the parsing error within the current line,
     * what was expected, and what was actually found.
     *
     * @param customError   Custom error message to append or null to use default expected token message.
     * @param expectedTypes An array of expected token types.
     * @return A formatted error message string indicating the error's nature and location.
     */
    private static String getParseExceptionText(final Lexer lexer,
                                                final String customError,
                                                final TokenType... expectedTypes) {
        final int index = lexer.getCurrentIndex();
        final TokenType actualType = lexer.getCurrentToken().type();
        final String actualValue = lexer.getCurrentToken().value();


        int lineStartIndex = 0;
        int lineEndIndex = lexer.getQuery().length();

        boolean first = true;

        if (!lexer.isEndOfQuery()) {
            for (int i = lexer.getCurrentIndex(); i >= 0; i--) {
                if (first) {
                    first = false;
                    continue;
                }
                if (lexer.getQuery().charAt(i) != '\n') {
                    continue;
                }
                lineStartIndex = i;
                break;
            }

            first = true;
            for (int i = lexer.getCurrentIndex(); i < lexer.getQuery().length(); i++) {
                if (first) {
                    first = false;
                    continue;
                }
                if (lexer.getQuery().charAt(i) != '\n') {
                    continue;
                }
                lineEndIndex = i;
                break;
            }
        }

        final String line = lexer.getQuery().substring(lineStartIndex, lineEndIndex).stripTrailing();
        final int errorIndex = index - lineStartIndex - actualValue.length();

        final StringBuilder bob = new StringBuilder();
        bob.append(Parser.TRACE_PREFIX).append(line).append("\n")
                .append(Parser.TRACE_PREFIX).append(" ".repeat(errorIndex)).append("^".repeat(actualValue.length())).append("\n");

        if (customError != null) {
            for (final String customErrorLine : customError.split("\n")) {
                bob.append(Parser.TRACE_PREFIX).append(" ".repeat(errorIndex)).append("| ").append(customErrorLine).append("\n");
            }
            if (expectedTypes.length > 0) {
                bob.append(Parser.TRACE_PREFIX).append(" ".repeat(errorIndex)).append("| Expected ");
                if (expectedTypes.length > 1) {
                    bob.append("one of ");
                } else {
                    bob.append(" ");
                }
                bob.append(Arrays.stream(expectedTypes)
                                .map(TokenType::name)
                                .collect(Collectors.joining(", ")))
                        .append(" but got ").append(actualType).append(" with value ").append(actualValue).append("\n");
            }
        } else {
            bob.append(Parser.TRACE_PREFIX).append(" ".repeat(errorIndex)).append("| Expected");
            if (expectedTypes.length > 1) {
                bob.append(" one of ");
            } else {
                bob.append(" ");
            }
            bob.append(Arrays.stream(expectedTypes)
                            .map(TokenType::name)
                            .collect(Collectors.joining(", ")))
                    .append(" but got ").append(actualType).append(" with value ").append(actualValue).append("\n");
        }

        return bob.toString();
    }

    public static ParseException because(final Lexer lexer,
                                         final String reason,
                                         final TokenType... expected) {
        return new ParseException(
                lexer.getCurrentIndex(),
                lexer.getCurrentToken(),
                ParseException.getParseExceptionText(lexer, reason, expected),
                reason
        );
    }

    @Override
    public String getMessage() {
        return this.message + "\n" + this.formattedError;
    }

}
