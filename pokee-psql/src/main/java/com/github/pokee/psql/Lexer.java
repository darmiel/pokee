package com.github.pokee.psql;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.exception.LexerException;

import java.util.Map;

/**
 * The Lexer class is responsible for converting a query-"program" into a stream of tokens.
 */
public class Lexer {

    /**
     * A mapping of keyword strings to their corresponding TokenType, facilitating quick lookup and categorization
     * of keywords within the query.
     */
    public static final Map<String, TokenType> KEYWORDS = Map.of(
            "and", TokenType.AND,
            "or", TokenType.OR,
            "not", TokenType.NOT,
            "use", TokenType.USE,
            "as", TokenType.AS,
            "filter", TokenType.FILTER,
            "map", TokenType.MAP,
            "query", TokenType.QUERY,
            "lang", TokenType.LANGUAGE
    );

    /**
     * The original query string that is being tokenized.
     */
    private final String query;

    /**
     * The total length of the query string, used to check if the end of the string has been reached.
     */
    private final int queryLength;

    /**
     * Current index in the query string, indicating the position of the next character to be processed.
     */
    private int currentIndex;

    /**
     * The most recently recognized token. This is the 'current' token in the context of the parsing process.
     */
    private Token currentToken;

    /**
     * Initializes a new Lexer instance with the specified query.
     *
     * @param query The SQL-like query string to be tokenized.
     */
    public Lexer(final String query) {
        this.query = query;
        this.queryLength = query.length();
        this.currentIndex = 0;
    }

    /**
     * Resets the lexer to the initial state for reusing the instance on new input.
     */
    public void reset() {
        this.currentIndex = 0;
        this.currentToken = null;
    }

    /**
     * Processes the next token in the query string, skipping whitespace and handling different types of characters.
     * It recognizes and categorizes tokens such as parentheses, operators, literals, and identifiers.
     *
     * @return true if a token is successfully processed, false if the end of the query is reached.
     * @throws LexerException if an unrecognized character or sequence is encountered.
     */
    public boolean nextToken() {
        this.skipWhitespaces();
        if (this.isEndOfQuery()) {
            this.currentToken = new Token(TokenType.EOF, "", this.currentIndex, this.currentIndex);
            return false;
        }

        final char currentChar = this.query.charAt(this.currentIndex);

        switch (currentChar) {
            // simple tokens
            case '(' -> this.getAndAdvance(TokenType.LPAREN, "(");
            case ')' -> this.getAndAdvance(TokenType.RPAREN, ")");
            case '[' -> this.getAndAdvance(TokenType.LBRACKET, "[");
            case ']' -> this.getAndAdvance(TokenType.RBRACKET, "]");
            case '{' -> this.getAndAdvance(TokenType.LBRACE, "{");
            case '}' -> this.getAndAdvance(TokenType.RBRACE, "}");
            case '*' -> this.getAndAdvance(TokenType.STAR, "*");
            case ',' -> this.getAndAdvance(TokenType.COMMA, ",");
            case '.' -> this.getAndAdvance(TokenType.DOT, ".");
            case ';' -> this.getAndAdvance(TokenType.SEMICOLON, ";");

            // logic and math operators
            case '&' -> this.processDoubleCharToken('&', TokenType.AND);
            case '|' -> this.processDoubleCharToken('|', TokenType.OR);

            // value tokens
            case '"' -> {
                final int startIndex = this.currentIndex;
                final String value = this.readStringLiteral();
                this.currentToken = new Token(TokenType.STRING_LITERAL, value, startIndex, this.currentIndex);
            }

            default -> {
                // a digit should always be a number
                if (Character.isDigit(currentChar)) {
                    final int startIndex = this.currentIndex;
                    final String number = this.readNumber();
                    this.currentToken = new Token(TokenType.NUMBER, number, startIndex, this.currentIndex);
                    break;
                }

                // an identifier should always start with a letter
                if (Character.isLetter(currentChar)) {
                    final int startIndex = this.currentIndex;
                    final String identifier = this.readIdentifier();

                    // an identifier can be upper or lower case
                    final TokenType keywordType = Lexer.KEYWORDS.get(identifier.toLowerCase());
                    if (keywordType != null) {
                        this.currentToken = new Token(keywordType, identifier, startIndex, this.currentIndex);
                        break;
                    }

                    if (!this.isEndOfQuery()) {
                        if (this.query.charAt(this.currentIndex) == ':') {
                            this.currentIndex++;
                            if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == ':') {
                                this.currentIndex++;
                                this.currentToken = new Token(TokenType.NAMESPACE_NAME, identifier, startIndex, this.currentIndex);
                                break;
                            }
                            throw new LexerException("Unrecognized character: " + currentChar);
                        }

                        // an identifier followed by '(' should be recognized as a function
                        if (this.query.charAt(this.currentIndex) == '(') {
                            this.currentToken = new Token(TokenType.FUNCTION_NAME, identifier, startIndex, this.currentIndex);
                            break;
                        }
                    }

                    // otherwise we have a simple, plain and dumb identifier.
                    this.currentToken = new Token(TokenType.IDENTIFIER, identifier, startIndex, this.currentIndex);
                    break;
                }

                throw new LexerException("Unrecognized character: " + currentChar);
            }
        }
        return true;
    }

    /**
     * Processes a token that consists of two identical characters, such as '&&' or '||'.
     *
     * @param expectedChar The character that is expected to be repeated.
     * @param tokenType    The TokenType to assign to the token.
     */
    private void processDoubleCharToken(char expectedChar, TokenType tokenType) {
        this.currentIndex++;
        if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == expectedChar) {
            this.currentIndex++;
            this.currentToken = new Token(tokenType, "" + expectedChar + expectedChar, this.currentIndex - 2, this.currentIndex);
        } else {
            throw new LexerException("Unrecognized character: " + expectedChar);
        }
    }

    /**
     * Skips whitespace characters in the query until a non-whitespace character is encountered.
     */
    public void skipWhitespaces() {
        while (!this.isEndOfQuery() && Character.isWhitespace(this.query.charAt(this.currentIndex))) {
            this.currentIndex++;
        }
    }

    /**
     * Determines if the current index has reached the end of the query string.
     *
     * @return true if the end of the query has been reached, otherwise false.
     */
    public boolean isEndOfQuery() {
        return currentIndex >= queryLength;
    }

    /**
     * Advances the current index in the query string and sets the current token based on the specified type and value.
     *
     * @param tokenType The type of token to set.
     * @param value     The value of the token.
     */
    private void getAndAdvance(final TokenType tokenType, final String value) {
        this.currentIndex++;
        this.currentToken = new Token(tokenType, value, this.currentIndex - 1, this.currentIndex);
    }

    /**
     * Reads a numeric literal from the query starting from the current index.
     *
     * @return The string representation of the number.
     */
    public String readNumber() {
        final StringBuilder number = new StringBuilder();
        while (!this.isEndOfQuery() && Character.isDigit(this.query.charAt(this.currentIndex))) {
            number.append(this.query.charAt(this.currentIndex));
            this.currentIndex++;
        }
        return number.toString();
    }

    /**
     * Reads an identifier from the query starting from the current index, recognizing letters, digits, and underscores.
     *
     * @return The identifier string.
     */
    public String readIdentifier() {
        final StringBuilder identifier = new StringBuilder();
        while (!this.isEndOfQuery() && (Character.isLetterOrDigit(this.query.charAt(this.currentIndex)) || this.query.charAt(this.currentIndex) == '_')) {
            identifier.append(this.query.charAt(this.currentIndex));
            this.currentIndex++;
        }
        return identifier.toString();
    }

    /**
     * Reads a string literal enclosed in double quotes from the query.
     *
     * @return The string literal without the enclosing quotes.
     */
    public String readStringLiteral() {
        StringBuilder literal = new StringBuilder();
        this.currentIndex++; // skip the initial quote
        while (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) != '"') {
            literal.append(this.query.charAt(this.currentIndex));
            this.currentIndex++;
        }
        this.currentIndex++; // skip the closing quote
        return literal.toString();
    }

    /**
     * Returns the query string associated with this Lexer.
     *
     * @return The original query string.
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * Returns the current position within the query string being processed.
     *
     * @return The current index.
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * Returns the current token that has been processed by the lexer.
     *
     * @return The current token.
     */
    public Token getCurrentToken() {
        return this.currentToken;
    }

}
