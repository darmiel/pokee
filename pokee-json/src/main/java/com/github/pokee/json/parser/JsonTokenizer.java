package com.github.pokee.json.parser;

public class JsonTokenizer {

    private final String json;

    private int index = 0;

    public JsonTokenizer(String json) {
        this.json = json;
    }

    /**
     * Advance the index to the specified position
     *
     * @param index the position to advance to
     */
    public void advanceTo(int index) {
        this.index = index;
    }

    /**
     * Peek at the next token without advancing the index
     *
     * @return the next token
     */
    public JsonToken peekNextToken() {
        final JsonToken token = this.nextToken();
        // reset the index
        this.index = token.startPosition();
        return token;
    }

    /**
     * Get the next token and advance the index
     *
     * @return the next token
     */
    public JsonToken nextToken() {
        final int startIndex = this.index;

        // skip whitespace
        while (this.index < this.json.length() && Character.isWhitespace(this.json.charAt(this.index))) {
            this.index++;
        }

        // check if we reached the end of the document
        if (this.index >= this.json.length()) {
            return new JsonToken(JsonTokenType.END_DOCUMENT, null, startIndex, -1);
        }

        char c = this.json.charAt(this.index);
        switch (c) {
            case '{' -> {
                this.index++;
                return new JsonToken(JsonTokenType.BEGIN_OBJECT, "{", startIndex, this.index);
            }
            case '}' -> {
                this.index++;
                return new JsonToken(JsonTokenType.END_OBJECT, "}", startIndex, this.index);
            }
            case '[' -> {
                this.index++;
                return new JsonToken(JsonTokenType.BEGIN_ARRAY, "[", startIndex, this.index);
            }
            case ']' -> {
                this.index++;
                return new JsonToken(JsonTokenType.END_ARRAY, "]", startIndex, this.index);
            }
            case ':' -> {
                this.index++;
                return new JsonToken(JsonTokenType.NAME_SEPARATOR, ":", startIndex, this.index);
            }
            case ',' -> {
                this.index++;
                return new JsonToken(JsonTokenType.VALUE_SEPARATOR, ",", startIndex, this.index);
            }
            case '"' -> {
                final String read = this.readString();
                return new JsonToken(JsonTokenType.STRING, read, startIndex, this.index);
            }
            case 't', 'f' -> {
                final String read = this.readPrimitive();
                if (!read.strip().equals("true") && !read.strip().equals("false")) {
                    throw new IllegalStateException("Unexpected character: " + c);
                }
                return new JsonToken(JsonTokenType.BOOLEAN, read, startIndex, this.index);
            }
            case 'n' -> {
                final String read = this.readPrimitive();
                if (!read.strip().equals("null")) {
                    throw new IllegalStateException("Unexpected character: " + c);
                }
                return new JsonToken(JsonTokenType.NULL, read, startIndex, this.index);
            }
            case '.', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                final String read = this.readPrimitive();
                return new JsonToken(JsonTokenType.NUMBER, read, startIndex, this.index);
            }
            case '@' -> {
                this.index++;
                return new JsonToken(JsonTokenType.BEGIN_FUNCTION, "@", startIndex, this.index);
            }
            case '(' -> {
                this.index++;
                return new JsonToken(JsonTokenType.LPAREN, "(", startIndex, this.index);
            }
            case ')' -> {
                this.index++;
                return new JsonToken(JsonTokenType.RPAREN, ")", startIndex, this.index);
            }
            default -> throw new IllegalStateException("Unexpected character: " + c);
        }
    }

    /**
     * Read a function name from the tokenizer
     *
     * @return the function name read from the tokenizer
     */
    public String readFunctionName() {
        final StringBuilder bob = new StringBuilder();
        while (this.index < this.json.length()) {
            final char currentChar = this.json.charAt(this.index);
            if (currentChar == '(') {
                return bob.toString();
            }
            if (currentChar == ' ') {
                throw new IllegalStateException("Unexpected whitespace in function name");
            }
            bob.append(currentChar);
            this.index++;
        }
        throw new IllegalStateException("Unterminated function name");
    }

    /**
     * Read a string from the tokenizer
     *
     * @return the string read from the tokenizer
     */
    public String readString() {
        final StringBuilder bob = new StringBuilder("\"");
        this.index++;

        while (this.index < this.json.length()) {
            final char currentChar = this.json.charAt(this.index);

            // append the character
            bob.append(currentChar);

            // check if closing quote and NOT escaped
            if (currentChar == '"' && this.json.charAt(this.index - 1) != '\\') {
                // skip the closing quote
                this.index++;
                return bob.toString();
            }

            this.index++;
        }

        // document ended without closing quote
        throw new IllegalStateException("Unterminated string");
    }

    /**
     * Read a primitive from the tokenizer
     *
     * @return the primitive read from the tokenizer
     */
    public String readPrimitive() {
        final StringBuilder bob = new StringBuilder();

        while (this.index < this.json.length()) {
            final char currentChar = this.json.charAt(this.index);
            if (currentChar == ',' || currentChar == '}' || currentChar == ']' || currentChar == ')') {
                break;
            }
            bob.append(currentChar);
            this.index++;
        }

        return bob.toString();
    }

    /**
     * Get the current index of the tokenizer
     *
     * @return the current index
     */
    public int getIndex() {
        return index;
    }

}
