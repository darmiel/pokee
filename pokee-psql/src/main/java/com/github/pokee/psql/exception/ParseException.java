package com.github.pokee.psql.exception;

import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.token.support.TokenType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParseException extends Exception {

    private final String formattedError;
    private final String message;

    public ParseException(final String formattedError, final String message) {
        this.formattedError = formattedError;
        this.message = message;
    }

    public ParseException(final String formattedError) {
        this(formattedError, "Error parsing query");
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
        appendErrorHeader(bob, line, errorIndex, actualValue.length());

        if (customError != null) {
            appendCustomError(bob, customError, errorIndex, expectedTypes, actualType, actualValue);
        } else {
            appendDefaultError(bob, errorIndex, expectedTypes, actualType, actualValue);
        }

        return bob.toString();
    }

    /**
     * Appends the error header to the error message.
     *
     * @param bob         The string builder to append to.
     * @param line        The line where the error occurred.
     * @param errorIndex  The index of the error within the line.
     * @param valueLength The length of the value that caused the error.
     */
    private static void appendErrorHeader(StringBuilder bob, String line, int errorIndex, int valueLength) {
        bob.append(Parser.TRACE_PREFIX).append(line).append("\n")
                .append(Parser.TRACE_PREFIX).append(" ".repeat(errorIndex)).append("^".repeat(valueLength)).append("\n");
    }

    /**
     * Appends a custom error message to the error message.
     *
     * @param bob           The string builder to append to.
     * @param customError   The custom error message to append.
     * @param errorIndex    The index of the error within the line.
     * @param expectedTypes The expected token types.
     * @param actualType    The actual token type.
     * @param actualValue   The actual token value.
     */
    private static void appendCustomError(StringBuilder bob, String customError, int errorIndex, TokenType[] expectedTypes, TokenType actualType, String actualValue) {
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
    }

    /**
     * Appends the default error message to the error message.
     *
     * @param bob           The string builder to append to.
     * @param errorIndex    The index of the error within the line.
     * @param expectedTypes The expected token types.
     * @param actualType    The actual token type.
     * @param actualValue   The actual token value.
     */
    private static void appendDefaultError(StringBuilder bob, int errorIndex, TokenType[] expectedTypes, TokenType actualType, String actualValue) {
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

    /**
     * Constructs a new ParseException with a custom error message.
     *
     * @param lexer    The lexer that encountered the error.
     * @param reason   The custom error message.
     * @param expected The expected token types.
     * @return A new ParseException with the custom error message.
     */
    public static ParseException because(final Lexer lexer,
                                         final String reason,
                                         final TokenType... expected) {
        return new ParseException(
                ParseException.getParseExceptionText(lexer, reason, expected),
                reason
        );
    }

    @Override
    public String getMessage() {
        return this.message + "\n" + this.formattedError;
    }

}
