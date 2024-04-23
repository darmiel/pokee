package com.github.pokee.psql;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.*;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.*;
import com.github.pokee.psql.exception.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private final Lexer lexer;

    public Parser(final Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Throws an exception detailing expected tokens at the current parse position using a custom error message.
     *
     * @param prefix        A string prefix used for formatting the error message, typically for indentation or highlighting.
     * @param customError   A custom error message to display. If null, a standard expected token message is generated.
     * @param expectedTypes An array of expected token types at the current parse position.
     */
    private void throwExpectedToken(final String prefix,
                                    final String customError,
                                    final TokenType... expectedTypes) throws ParseException {
        throw new ParseException(this.lexer.getCurrentIndex(), this.lexer.getCurrentToken(),
                this.getParseExceptionText(prefix, customError, expectedTypes));
    }

    /**
     * Throws an exception detailing expected tokens at the current parse position using a custom error message.
     *
     * @param customError   A custom error message to display. If null, a standard expected token message is generated.
     * @param expectedTypes An array of expected token types at the current parse position.
     */
    private void throwExpectedToken(final String customError,
                                    final TokenType... expectedTypes) throws ParseException {
        this.throwExpectedToken("> ", customError, expectedTypes);
    }

    /**
     * Throws an exception detailing expected tokens at the current parse position using a custom error message.
     *
     * @param expectedTypes An array of expected token types at the current parse position.
     */
    private void throwExpectedToken(final TokenType... expectedTypes) throws ParseException {
        this.throwExpectedToken(null, expectedTypes);
    }

    /**
     * Constructs a detailed error message showing the exact location of the parsing error within the current line,
     * what was expected, and what was actually found.
     *
     * @param prefix        The prefix to use for each line of the error message.
     * @param customError   Custom error message to append or null to use default expected token message.
     * @param expectedTypes An array of expected token types.
     * @return A formatted error message string indicating the error's nature and location.
     */
    private String getParseExceptionText(final String prefix,
                                         final String customError,
                                         final TokenType... expectedTypes) {
        final int index = this.lexer.getCurrentIndex();
        final TokenType actualType = this.lexer.getCurrentToken().type();
        final String actualValue = this.lexer.getCurrentToken().value();


        int lineStartIndex = 0;
        int lineEndIndex = this.lexer.getQuery().length();

        boolean first = true;

        if (!this.lexer.isEndOfQuery()) {
            for (int i = this.lexer.getCurrentIndex(); i >= 0; i--) {
                if (first) {
                    first = false;
                    continue;
                }
                if (this.lexer.getQuery().charAt(i) != '\n') {
                    continue;
                }
                lineStartIndex = i;
                break;
            }

            first = true;
            for (int i = this.lexer.getCurrentIndex(); i < this.lexer.getQuery().length(); i++) {
                if (first) {
                    first = false;
                    continue;
                }
                if (this.lexer.getQuery().charAt(i) != '\n') {
                    continue;
                }
                lineEndIndex = i;
                break;
            }
        }

        final String line = this.lexer.getQuery().substring(lineStartIndex, lineEndIndex).stripTrailing();
        final int errorIndex = index - lineStartIndex - actualValue.length();

        final StringBuilder bob = new StringBuilder();
        bob.append(prefix).append(line).append("\n")
                .append(prefix).append(" ".repeat(errorIndex)).append("^".repeat(actualValue.length())).append("\n");

        if (customError != null) {
            for (final String customErrorLine : customError.split("\n")) {
                bob.append(prefix).append(" ".repeat(errorIndex)).append("| ").append(customErrorLine).append("\n");
            }
        } else {
            bob.append(prefix).append(" ".repeat(errorIndex)).append("| Expected");
            if (expectedTypes.length > 1) {
                bob.append(" one of ");
            } else {
                bob.append(" ");
            }
            bob.append(Arrays.stream(expectedTypes)
                            .map(TokenType::name)
                            .collect(Collectors.joining()))
                    .append(" but got ").append(actualType).append(" with value ").append(actualValue).append("\n");
        }

        return bob.toString();
    }

    /**
     * Ensures the current token matches the expected type and advances the lexer if it does.
     * Throws an error if the token does not match.
     *
     * @param type        The expected token type.
     * @param customError A custom error message to display if the token does not match.
     * @return The current instance of the Parser for method chaining.
     */
    private Parser expect(final TokenType type, final String customError) throws ParseException {
        if (this.lexer.getCurrentToken() == null) { // the first token
            this.lexer.nextToken();
        }
        if (this.lexer.getCurrentToken().type() != type) {
            this.throwExpectedToken("> ", customError, type);
        }
        return this;
    }

    /**
     * Ensures the current token matches the expected type and advances the lexer if it does.
     * Throws an error if the token does not match.
     *
     * @param type The expected token type.
     * @return The current instance of the Parser for method chaining.
     */
    private Parser expect(final TokenType type) throws ParseException {
        return this.expect(type, null);
    }

    /**
     * Advances the lexer to the next token.
     *
     * @return The current instance of the Parser for method chaining.
     */
    private Parser advance() {
        this.lexer.nextToken();
        return this;
    }

    /**
     * Returns the next token from the lexer without advancing the position.
     *
     * @return The next token.
     */
    private Token peek() {
        return this.lexer.peekToken();
    }

    /**
     * Returns the current token from the lexer without advancing the position.
     *
     * @return The current token.
     */
    private Token current() {
        return this.lexer.getCurrentToken();
    }

    /**
     * Creates a TerminalNode from the current token. A TerminalNode is presumably a part of the parser's
     * syntax tree, representing leaf nodes with actual token values.
     *
     * @return A new TerminalNode initialized with the current token.
     */
    private TerminalNode createTerminalNodeFromCurrentToken() {
        final TerminalNode terminalNode = new TerminalNode();
        terminalNode.setSymbol(this.lexer.getCurrentToken());
        return terminalNode;
    }

    public ProgramContext parseProgram() throws ParseException {
        final List<StatementContext> statements = new ArrayList<>();
        while (lexer.nextToken()) {
            statements.add(this.parseStatement());
        }
        return new ProgramContext(statements);
    }

    /**
     * Parses a statement from the lexer and returns a StatementContext instance.
     *
     * @return A StatementContext instance representing the parsed statement.
     */
    public StatementContext parseStatement() throws ParseException {
        switch (this.lexer.getCurrentToken().type()) {
            case USE -> {
                final UseStatementContext useStatementContext = this.parseUseAliasContext();
                return new StatementContext(useStatementContext, null, null);
            }
            case QUERY -> {
                final QueryContext queryContext = this.parseQueryContext();
                return new StatementContext(null, queryContext, null);
            }
            case LANGUAGE -> {
                final LanguageContext languageContext = this.parseLanguage();
                return new StatementContext(null, null, languageContext);
            }
            default ->
                    this.throwExpectedToken("A statement should either be `use` or `query`. Current: " + this.current().type(),
                            TokenType.USE, TokenType.QUERY);
        }

        return null;
    }

    /**
     * Parses a `use` statement and returns a UseAliasContext instance.
     *
     * @return A UseAliasContext instance representing the parsed `use` statement.
     */
    public UseStatementContext parseUseAliasContext() throws ParseException {
        this.expect(TokenType.USE, "A use-statement should start with `use`.").advance()
                .expect(TokenType.IDENTIFIER, "You must specify a namespace to use.");
        final TerminalNode original = this.createTerminalNodeFromCurrentToken();

        switch (this.advance().current().type()) {
            case SEMICOLON -> {
                // if a semicolon is found, it means no alias is specified, the namespace is just imported.
                return new UseStatementContext(original, null);
            }
            case AS -> {
                // if an `as` is found, it means an alias is specified for the namespace.
                this.advance().expect(TokenType.IDENTIFIER, "You must specify an alias after `as`.");
                final TerminalNode alias = this.createTerminalNodeFromCurrentToken();
                this.advance().expect(TokenType.SEMICOLON, "You must end the statement with a semicolon.");
                return new UseStatementContext(original, alias);
            }
            default -> this.throwExpectedToken("Expected either `;` or `as` to specify an alias.",
                    TokenType.SEMICOLON, TokenType.AS);
        }

        return null;
    }

    /**
     * Parses the type of projection based on the current token and updates the list of projections.
     *
     * @param projectionNodeList The list to which new projection nodes are added.
     * @param namespace          The current namespace terminal node.
     * @throws ParseException if unexpected tokens are found.
     */
    private void parseProjectionType(final List<ProjectionNode> projectionNodeList,
                                     final TerminalNode namespace) throws ParseException {
        switch (this.current().type()) {
            case STAR:
                projectionNodeList.add(new ProjectionNode(namespace, null, null, true));
                this.advance();
                break;
            case LBRACE:
                this.advance();
                this.parseProjectionNodesInBraces(projectionNodeList, namespace);
                break;
            case IDENTIFIER:
                final ProjectionNode node = this.parseProjectionNode(namespace);
                projectionNodeList.add(node);
                break;
            default:
                this.throwExpectedToken(
                        "Expected either `*`, `{`, or an identifier after the namespace name.\n" +
                                "You can select all fields using " + namespace.getText() + "::* or specify multiple\n" +
                                "fields using " + namespace.getText() + "::{field1, field2 as f2} or select a single\n" +
                                "field using " + namespace.getText() + "::field1, " + namespace.getText() + "::field2 as f2.");
        }
    }

    /**
     * Parses field definitions enclosed in braces.
     *
     * @param projectionNodeList The list to which new projection nodes are added.
     * @param namespace          The current namespace terminal node.
     * @throws ParseException if unexpected tokens are found.
     */
    private void parseProjectionNodesInBraces(final List<ProjectionNode> projectionNodeList,
                                              final TerminalNode namespace) throws ParseException {
        while (this.current().type() != TokenType.RBRACE) {
            final ProjectionNode node = this.parseProjectionNode(namespace);
            projectionNodeList.add(node);

            if (this.current().type() == TokenType.COMMA) {
                this.advance();
            } else if (this.current().type() != TokenType.RBRACE) {
                this.throwExpectedToken("Expected either `,` or `}` after a field selection.");
            }
        }
        this.advance(); // Move past the RBRACE
    }

    /**
     * Parses projection nodes from a given query structure.
     * This method supports different formats of projections including:
     * - Wildcard projections (e.g., `P::*`)
     * - Listed fields with optional aliases in braces (e.g., `P::{name, hp}`)
     * - Multiple individual fields with optional aliases (e.g., `P::name, P::hp`)
     *
     * @return A list of ProjectionNode, each representing a parsed field or wildcard from the query.
     * @throws ParseException if the syntax of the projections is incorrect.
     */
    private List<ProjectionNode> parseProjectionNodes() throws ParseException {
        final List<ProjectionNode> projectionNodeList = new ArrayList<>();
        while (true) {
            this.expect(TokenType.NAMESPACE_NAME, """
                    Expected a selection namespace name after query identifier.
                    Use an identifier you have previously used in a `use` statement.
                    For example, `P::{name, hp}` selects the `name` and `hp` fields from the `Pokemon` namespace.""");
            final TerminalNode namespace = this.createTerminalNodeFromCurrentToken();
            this.advance();

            this.parseProjectionType(projectionNodeList, namespace);

            if (this.current().type() == TokenType.COMMA) {
                this.advance();
            } else {
                break;
            }
        }
        return projectionNodeList;
    }

    private ProjectionNode parseProjectionNode(final TerminalNode namespace) throws ParseException {
        if (this.current().type() == TokenType.STAR) {
            this.advance();
            return new ProjectionNode(namespace, null, null, true);
        }

        this.expect(TokenType.IDENTIFIER, "Expected a field name.");
        final TerminalNode field = this.createTerminalNodeFromCurrentToken();
        this.advance();

        if (this.current().type() == TokenType.AS) {
            this.advance().expect(TokenType.IDENTIFIER, "Expected an alias after `as`.");
            final TerminalNode alias = this.createTerminalNodeFromCurrentToken();
            this.advance();
            return new ProjectionNode(namespace, field, alias, false);
        }

        return new ProjectionNode(namespace, field, null, false);
    }

    private List<TerminalNode> parseFunctionArguments() {
        final List<TerminalNode> arguments = new ArrayList<>();

        while (this.current().type() != TokenType.RPAREN) {
            arguments.add(this.createTerminalNodeFromCurrentToken());
            this.advance();

            if (this.current().type() == TokenType.COMMA) {
                this.advance(); // move past ','
            } else if (this.current().type() == TokenType.EOF) {
                break;
            }
        }

        return arguments;
    }

    public ExpressionNode parseExpressionNode() throws ParseException {
        return this.parseLogicalOrExpression();
    }

    private ExpressionNode parseLogicalOrExpression() throws ParseException {
        ExpressionNode lhs = this.parseLogicalAndExpression();
        while (this.current().type() == TokenType.BOOL_OR) {
            this.advance(); // move past the operator

            final ExpressionNode rhs = this.parseLogicalAndExpression();
            lhs = new BinaryExpressionNode(lhs, rhs, TokenType.BOOL_OR);
        }
        return lhs;
    }

    private ExpressionNode parseLogicalAndExpression() throws ParseException {
        ExpressionNode lhs = this.parseEqualityExpression();
        while (this.current().type() == TokenType.BOOL_AND) {
            this.advance(); // move past the operator

            final ExpressionNode rhs = this.parseEqualityExpression();
            lhs = new BinaryExpressionNode(lhs, rhs, TokenType.BOOL_AND);
        }
        return lhs;
    }

    private ExpressionNode parseEqualityExpression() throws ParseException {
        ExpressionNode lhs = this.parseRelationalExpression();
        while (this.current().type() == TokenType.CMP_EQUALS || this.current().type() == TokenType.CMP_NOT_EQUALS) {
            final TokenType operator = this.current().type();
            this.advance(); // move past the operator

            final ExpressionNode rhs = this.parseRelationalExpression();
            lhs = new BinaryExpressionNode(lhs, rhs, operator);
        }
        return lhs;
    }

    private ExpressionNode parseRelationalExpression() throws ParseException {
        ExpressionNode lhs = this.parsePrimaryExpression();
        while (this.current().type().isCompareOperator()) {
            final TokenType operator = this.current().type();
            this.advance(); // move past the operator

            final ExpressionNode rhs = this.parsePrimaryExpression();
            lhs = new BinaryExpressionNode(lhs, rhs, operator);
        }
        return lhs;
    }

    private ExpressionNode parseLiteralExpression() throws ParseException {
        final TokenType type = this.current().type();
        final TerminalNode terminalNode = this.createTerminalNodeFromCurrentToken();
        this.advance();
        return new LiteralExpressionNode(type, terminalNode);
    }

    private ExpressionNode parseFunctionOrIdentifierExpression() throws ParseException {
        this.expect(TokenType.NAMESPACE_NAME, """
                Expected a namespace name to start an expression.
                Use an identifier you have previously used in a `use` statement.
                Example: P::name.startsWith("Pika")""");
        final TerminalNode namespace = this.createTerminalNodeFromCurrentToken();
        this.advance();

        this.expect(TokenType.IDENTIFIER, "Expected a target field after the namespace name.");
        final TerminalNode target = this.createTerminalNodeFromCurrentToken();

        final Token peek = this.peek();

        // a dot indicates a function call
        if (peek.type() == TokenType.DOT) {
            this.advance() // go to the dot
                    .advance() // go to the function name
                    .expect(TokenType.FUNCTION_NAME, "Expected a function name after the dot.");

            final TerminalNode functionName = this.createTerminalNodeFromCurrentToken();
            this.advance();

            this.expect(TokenType.LPAREN, "Expected an opening parenthesis after the function name.").advance();
            final List<TerminalNode> arguments = this.parseFunctionArguments();
            this.expect(TokenType.RPAREN, "Expected a closing parenthesis after the function arguments.").advance();

            return new FunctionCallExpressionNode(namespace, target, functionName.getText(), arguments);
        }

        // if there is a compare operator, treat the current expression as an identifier
        if (peek.type().isCompareOperator()) {
            this.advance(); // move to the compare operator
            return new IdentifierExpressionNode(namespace, target);
        }

        this.throwExpectedToken("""
                Expected either a function call or a comparison after the namespace and target field.
                You can call a function using `P::name.startsWith("Pika")` or compare fields using `P::name == "Pikachu`.""");
        return null;
    }

    public ExpressionNode parsePrimaryExpression() throws ParseException {
        switch (this.current().type()) {
            case LPAREN -> {
                this.advance();

                final ExpressionNode expression = this.parseExpressionNode();
                this.expect(TokenType.RPAREN, "Expected a closing parenthesis after the expression.");
                this.advance(); // move past the closing parenthesis

                return expression;
            }
            case NAMESPACE_NAME -> {
                return this.parseFunctionOrIdentifierExpression();
            }
        }
        if (this.current().type().isLiteral()) {
            return this.parseLiteralExpression();
        }
        this.throwExpectedToken("Expected either a namespace or an opening parenthesis to start an expression.");
        return null;
    }

    public QueryContext parseQueryContext() throws ParseException {
        this.expect(TokenType.QUERY, "Expected `query` to start a query statement.").advance();

        // read the query name
        this.expect(TokenType.IDENTIFIER, """
                Expected a query name after `query`.
                This name will be used to identify the result of the query in the output.""");
        final TerminalNode queryName = this.createTerminalNodeFromCurrentToken();
        this.advance();

        final List<ProjectionNode> projectionNodeList = this.parseProjectionNodes();
        System.out.println(projectionNodeList);

        // make sure if there is an `all` projection node, it is the only one
        if (projectionNodeList.stream().anyMatch(ProjectionNode::isAll) && projectionNodeList.size() > 1) {
            this.throwExpectedToken("You can only use `*` once in a query.");
        }

        final List<ExpressionNode> filterExpressions = new ArrayList<>();
        while (this.current().type() == TokenType.FILTER) {
            this.expect(TokenType.FILTER, "Expected `filter` to start a filter statement.").advance();
            final ExpressionNode filterExpression = this.parseExpressionNode();
            filterExpressions.add(filterExpression);
        }

        if (this.current().type() != TokenType.EOF && this.current().type() != TokenType.SEMICOLON) {
            this.throwExpectedToken("Expected either `;` or the end of the query.");
            return null;
        }

        return new QueryContext(queryName, projectionNodeList, filterExpressions);
    }

    public LanguageContext parseLanguage() throws ParseException {
        this.expect(TokenType.LANGUAGE, "Expected `language` to start a language statement.").advance();
        this.expect(TokenType.IDENTIFIER, "Expected a language name after `language`.");

        final TerminalNode language = this.createTerminalNodeFromCurrentToken();
        this.advance();

        if (this.current().type() != TokenType.SEMICOLON && this.current().type() != TokenType.EOF) {
            this.throwExpectedToken("Expected either `;` or the end of the language statement.");
            return null;
        }

        return new LanguageContext(language);
    }

}
