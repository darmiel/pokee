package com.github.pokee.psql;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;

import java.util.Map;

public class Lexer {

    private final String query;
    private final int queryLength;

    private int currentIndex;

    private Token currentToken;
    private Token previousToken;

    private final Map<String, TokenType> keywords = Map.of(
            "and", TokenType.BOOL_AND,
            "or", TokenType.BOOL_OR,
            "use", TokenType.USE,
            "as", TokenType.AS,
            "filter", TokenType.FILTER,
            "map", TokenType.MAP,
            "query", TokenType.QUERY
    );

    public Lexer(final String query) {
        this.query = query;
        this.queryLength = query.length();
        this.currentIndex = 0;
    }

    public void reset() {
        this.currentIndex = 0;
    }

    public String getQuery() {
        return query;
    }

    private void getAndAdvance(final TokenType tokenType, final String value) {
        this.currentIndex++;
        this.currentToken = new Token(tokenType, value);
    }

    public boolean nextToken() {
        this.skipWhitespaces();
        if (this.isEndOfQuery()) {
            this.currentToken = new Token(TokenType.EOF, "");
            return false;
        }

        this.previousToken = this.currentToken;
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
            case '&' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '&') {
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.BOOL_AND, "&&");
                } else {
                    this.currentToken = new Token(TokenType.BIT_AND, "&");
                }
            }
            case '|' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '|') {
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.BOOL_OR, "||");
                } else {
                    this.currentToken = new Token(TokenType.BIT_OR, "|");
                }
            }

            // a ^ b is a XOR b
            case '^' -> this.getAndAdvance(TokenType.BIT_XOR, "^");

            // ~ is a bitwise NOT
            case '~' -> this.getAndAdvance(TokenType.BIT_NOT, "~");

            // +, -, / and % are simple math operators
            case '+' -> this.getAndAdvance(TokenType.PLUS, "+");
            case '-' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '>') {
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.RIGHT_ARROW, "->");
                } else {
                    this.currentToken = new Token(TokenType.MINUS, "-");
                }
            }
            case '/' -> this.getAndAdvance(TokenType.DIVIDE, "/");
            case '%' -> this.getAndAdvance(TokenType.MODULO, "%");

            // value tokens
            case '"' -> this.currentToken = new Token(TokenType.STRING_LITERAL, this.readStringLiteral());

            // comparison operators
            case '<' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '<') {
                    // << is a bit shift left
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.BIT_SHIFT_LEFT, "<<");
                } else if (this.query.charAt(this.currentIndex) == '>') {
                    // <> is not equals
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.CMP_NOT_EQUALS, "<>");
                } else if (this.query.charAt(this.currentIndex) == '=') {
                    // <= is lower or equals
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.CMP_LOWER_OR_EQUALS, "<=");
                } else if (this.query.charAt(this.currentIndex) == '-') {
                    // <- is a left arrow
                    this.currentIndex++;

                    if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '>') {
                        this.currentIndex++;
                        this.currentToken = new Token(TokenType.LEFT_RIGHT_ARROW, "<->");
                        break;
                    }

                    this.currentToken = new Token(TokenType.LEFT_ARROW, "<-");
                } else {
                    // < is lower than
                    this.currentToken = new Token(TokenType.CMP_LOWER_THAN, "<");
                }
            }
            case '>' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '>') {
                    // >> is a bit shift right
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.BIT_SHIFT_RIGHT, ">>");
                } else if (this.query.charAt(this.currentIndex) == '=') {
                    // >= is greater or equals
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.CMP_GREATER_OR_EQUALS, ">=");
                } else {
                    // > is greater than
                    this.currentToken = new Token(TokenType.CMP_GREATER_THAN, ">");
                }
            }
            case '=' -> {
                this.currentIndex++;
                if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == '=') {
                    this.currentIndex++;
                    this.currentToken = new Token(TokenType.CMP_EQUALS, "==");
                } else {
                    throw new RuntimeException("Unrecognized character: " + currentChar);
                }
            }

            default -> {
                // a digit should always be a number
                if (Character.isDigit(currentChar)) {
                    this.currentToken = new Token(TokenType.NUMBER, this.readNumber());
                    break;
                }

                // an identifier should always start with a letter
                if (Character.isLetter(currentChar)) {
                    final String identifier = this.readIdentifier();

                    // an identifier can be upper or lower case
                    final TokenType keywordType = this.keywords.get(identifier.toLowerCase());
                    if (keywordType != null) {
                        this.currentToken = new Token(keywordType, identifier);
                        break;
                    }

                    if (!this.isEndOfQuery()) {
                        if (this.query.charAt(this.currentIndex) == ':') {
                            this.currentIndex++;
                            if (!this.isEndOfQuery() && this.query.charAt(this.currentIndex) == ':') {
                                this.currentIndex++;
                                this.currentToken = new Token(TokenType.NAMESPACE_NAME, identifier);
                                break;
                            }
                            throw new RuntimeException("Unrecognized character: " + currentChar);
                        }
                        // an identifier followed by '[' should be recognized as a query name
                        if (this.query.charAt(this.currentIndex) == '[') {
                            this.currentToken = new Token(TokenType.QUERY_NAME, identifier);
                            break;
                        }
                        // an identifier followed by '(' should be recognized as a function
                        if (this.query.charAt(this.currentIndex) == '(') {
                            this.currentToken = new Token(TokenType.FUNCTION_NAME, identifier);
                            break;
                        }
                    }

                    // otherwise we have a simple, plain and dumb identifier.
                    this.currentToken = new Token(TokenType.IDENTIFIER, identifier);
                    break;
                }

                throw new RuntimeException("Unrecognized character: " + currentChar);
            }
        }
        return true;
    }

    public boolean isEndOfQuery() {
        return currentIndex >= queryLength;
    }

    public void skipWhitespaces() {
        while (!this.isEndOfQuery() && Character.isWhitespace(this.query.charAt(this.currentIndex))) {
            this.currentIndex++;
        }
    }

    public String readNumber() {
        final StringBuilder number = new StringBuilder();
        while (!this.isEndOfQuery() && Character.isDigit(this.query.charAt(this.currentIndex))) {
            number.append(this.query.charAt(this.currentIndex));
            this.currentIndex++;
        }
        return number.toString();
    }

    public String readIdentifier() {
        final StringBuilder identifier = new StringBuilder();
        while (!this.isEndOfQuery() && (Character.isLetterOrDigit(this.query.charAt(this.currentIndex)) || this.query.charAt(this.currentIndex) == '_')) {
            identifier.append(this.query.charAt(this.currentIndex));
            this.currentIndex++;
        }
        return identifier.toString();
    }


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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token getPreviousToken() {
        return previousToken;
    }

}
