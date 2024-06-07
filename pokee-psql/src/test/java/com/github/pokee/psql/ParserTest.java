package com.github.pokee.psql;

import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.expression.BinaryExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.ExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.*;
import com.github.pokee.psql.exception.LexerException;
import com.github.pokee.psql.exception.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    public static Parser createParser(final String input) {
        return createParser(input, true);
    }

    public static Parser createParser(final String input, final boolean advance) {
        final Lexer lexer = new Lexer(input);
        if (advance) {
            lexer.nextToken();
        }
        return new Parser(lexer);
    }

    @Test
    void parseProgram() throws ParseException {
        final String program = """
                USE orig AS alias;
                LANG de;
                QUERY all a::*;
                QUERY filtered a::* FILTER a::name.lt(1);
                """;
        final Parser parser = new Parser(new Lexer(program));
        final ProgramContext programContext = parser.parseProgram();

        assertNotNull(programContext);
        assertEquals(4, programContext.getStatements().size());

        // check alias node
        final UseStatementContext useStatementContext = programContext.getStatements().get(0).getUseAliasContext();
        assertNotNull(useStatementContext);
        assertEquals("orig", useStatementContext.getOriginal().getText());
        assertEquals("alias", useStatementContext.getAlias().getText());

        // check lang node
        final LanguageContext languageContext = programContext.getStatements().get(1).getLanguageContext();
        assertEquals("de", languageContext.getLanguage().getText());

        // check query node
        final QueryContext queryContext1 = programContext.getStatements().get(2).getQueryContext();
        assertEquals("all", queryContext1.getQueryName().getText());
        assertEquals(1, queryContext1.getProjectionNodes().size());
        assertEquals(0, queryContext1.getFilters().size());

        // check second query node
        final QueryContext queryContext2 = programContext.getStatements().get(3).getQueryContext();
        assertEquals("filtered", queryContext2.getQueryName().getText());
        assertEquals(1, queryContext2.getProjectionNodes().size());
        assertEquals(1, queryContext2.getFilters().size());
    }

    @Test
    void parseStatement() throws ParseException {
        // Test USE statement
        final StatementContext statementContext = createParser("USE orig AS alias;")
                .parseStatement();
        assertNotNull(statementContext);
        assertNotNull(statementContext.getUseAliasContext());

        // Test USE statement
        final UseStatementContext useStatementContext = createParser("USE orig AS alias;")
                .parseUseAliasContext();
        assertNotNull(useStatementContext);
        assertEquals("orig", useStatementContext.getOriginal().getText());
        assertEquals("alias", useStatementContext.getAlias().getText());

        // Test QUERY statement
        final QueryContext queryContext = createParser("QUERY all a::*;")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("all", queryContext.getQueryName().getText());
        assertEquals(1, queryContext.getProjectionNodes().size());
        assertEquals(0, queryContext.getFilters().size());

        // Test LANGUAGE statement
        final LanguageContext languageContext = createParser("LANG de;").parseLanguage();
        assertNotNull(languageContext);
        assertEquals("de", languageContext.getLanguage().getText());

        // Test empty input
        try {
            createParser("").parseStatement();
            fail("Expected ParseException for empty input");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test invalid statement
        try {
            createParser("INVALID STATEMENT;").parseStatement();
            fail("Expected ParseException for invalid statement");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    void parseUseAliasContext() throws ParseException {
        // Test valid USE statement with an alias
        UseStatementContext useStatementContext = createParser("USE original AS alias;")
                .parseUseAliasContext();
        assertNotNull(useStatementContext);
        assertEquals("original", useStatementContext.getOriginal().getText());
        assertEquals("alias", useStatementContext.getAlias().getText());

        // Test valid USE statement without an alias
        useStatementContext = createParser("USE original;")
                .parseUseAliasContext();
        assertNotNull(useStatementContext);
        assertEquals("original", useStatementContext.getOriginal().getText());
        assertNull(useStatementContext.getAlias());

        // Test valid USE statement without an alias
        useStatementContext = createParser("USE original;")
                .parseUseAliasContext();
        assertNotNull(useStatementContext);
        assertEquals("original", useStatementContext.getOriginal().getText());
        assertNull(useStatementContext.getAlias());

        // Test invalid USE statement with missing alias after AS
        try {
            createParser("USE original AS ;").parseUseAliasContext();
            fail("Expected ParseException for missing alias after AS");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    void parseQueryContext() throws ParseException {
        // Test valid QUERY statement with a wildcard *
        QueryContext queryContext = createParser("QUERY all a::*;")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("all", queryContext.getQueryName().getText());
        assertEquals(1, queryContext.getProjectionNodes().size());
        assertEquals(0, queryContext.getFilters().size());

        // Test valid QUERY statement with a specific field
        queryContext = createParser("QUERY single a::field;")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("single", queryContext.getQueryName().getText());
        assertEquals(1, queryContext.getProjectionNodes().size());
        assertEquals(0, queryContext.getFilters().size());

        // Test valid QUERY statement with a filter
        queryContext = createParser("QUERY filtered a::* FILTER a::name.lt(1);")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("filtered", queryContext.getQueryName().getText());
        assertEquals(1, queryContext.getProjectionNodes().size());
        assertEquals(1, queryContext.getFilters().size());

        // Test invalid QUERY statement with missing namespace
        try {
            createParser("QUERY ;").parseQueryContext();
            fail("Expected ParseException for missing namespace");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test invalid QUERY statement with missing field or wildcard after ::
        try {
            createParser("QUERY missing a::;").parseQueryContext();
            fail("Expected ParseException for missing field or wildcard after ::");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test valid QUERY statement with multiple projections in {}
        queryContext = createParser("QUERY multiple a::{name AS AliasName, description};")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("multiple", queryContext.getQueryName().getText());
        assertEquals(2, queryContext.getProjectionNodes().size());
        assertEquals("a", queryContext.getProjectionNodes().get(0).getField().getNamespace().getText());
        assertEquals("name", queryContext.getProjectionNodes().get(0).getField().getField().getText());
        assertEquals("AliasName", queryContext.getProjectionNodes().get(0).getAlias().getText());
        assertEquals("a", queryContext.getProjectionNodes().get(1).getField().getNamespace().getText());
        assertEquals("description", queryContext.getProjectionNodes().get(1).getField().getField().getText());
        assertNull(queryContext.getProjectionNodes().get(1).getAlias());
        assertEquals(0, queryContext.getFilters().size());

        // Test valid QUERY statement with asterisk in the projection
        queryContext = createParser("QUERY all a::{*};")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("all", queryContext.getQueryName().getText());
        assertEquals(1, queryContext.getProjectionNodes().size());
        assertEquals("a", queryContext.getProjectionNodes().get(0).getField().getNamespace().getText());
        assertTrue(queryContext.getProjectionNodes().get(0).isAll());
        assertEquals(0, queryContext.getFilters().size());

        // Test invalid QUERY statement with name and asterisk in the projection
        try {
            createParser("QUERY invalid a::{name, *};").parseQueryContext();
            fail("Expected ParseException for having name and asterisk in the projection");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test valid QUERY statement with multiple projections separated by comma
        queryContext = createParser("QUERY multiple a::name, b::description AS eee;")
                .parseQueryContext();
        assertNotNull(queryContext);
        assertEquals("multiple", queryContext.getQueryName().getText());
        assertEquals(2, queryContext.getProjectionNodes().size());
        assertEquals("a", queryContext.getProjectionNodes().get(0).getField().getNamespace().getText());
        assertEquals("name", queryContext.getProjectionNodes().get(0).getField().getField().getText());
        assertNull(queryContext.getProjectionNodes().get(0).getAlias());
        assertEquals("b", queryContext.getProjectionNodes().get(1).getField().getNamespace().getText());
        assertEquals("description", queryContext.getProjectionNodes().get(1).getField().getField().getText());
        assertEquals("eee", queryContext.getProjectionNodes().get(1).getAlias().getText());
        assertEquals(0, queryContext.getFilters().size());

        // Test invalid QUERY statement with name and asterisk separated by comma
        try {
            createParser("QUERY invalid a::name, a::*;").parseQueryContext();
            fail("Expected ParseException for having name and asterisk separated by comma");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    void parseLanguage() throws ParseException {
        // Test valid LANG statement
        final LanguageContext languageContext = createParser("LANG de;").parseLanguage();
        assertNotNull(languageContext);
        assertEquals("de", languageContext.getLanguage().getText());

        // Test invalid LANG statement with missing language code
        try {
            createParser("LANG ;").parseLanguage();
            fail("Expected ParseException for missing language code");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    void parseExpressionNode() throws ParseException {
        // Test valid EXPRESSION statement with a function call
        final ExpressionNode expressionNode = createParser("P::name.startsWith(\"Pika\");")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(FunctionCallExpressionNode.class, expressionNode);

        FunctionCallExpressionNode funNode = (FunctionCallExpressionNode) expressionNode;
        assertEquals("startsWith", funNode.getFunctionName());
        assertEquals(1, funNode.getArguments().size());
        assertEquals("Pika", funNode.getArguments().get(0).getText());
        assertEquals("P", funNode.getTarget().getNamespace().getText());
        assertEquals("name", funNode.getTarget().getField().getText());

        // Test invalid expression without
        try {
            createParser("age.gt(10);")
                    .parseExpressionNode();
            fail("Expected ParseException for missing namespace in the expression");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    void parseLogicalAndExpression() throws ParseException {
        // Test valid EXPRESSION statement with logical AND operation
        ExpressionNode expressionNode = createParser("P::name.eq(\"Pika\") AND P::hp.gt(50);")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.AND, ((BinaryExpressionNode) expressionNode).getOperator());

        // Test valid nested EXPRESSION statement
        expressionNode = createParser("P::name.eq(\"Pika\") AND P::hp.gt(50) AND P::type.eq(\"Electric\");")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.AND, ((BinaryExpressionNode) expressionNode).getOperator());
        assertInstanceOf(BinaryExpressionNode.class, ((BinaryExpressionNode) expressionNode).getLhs());
        assertEquals(TokenType.AND, ((BinaryExpressionNode) ((BinaryExpressionNode) expressionNode).getLhs()).getOperator());

        // Test valid nested EXPRESSION statement with parentheses
        expressionNode = createParser("P::name.eq(\"Pika\") AND (P::hp.gt(50) OR P::type.eq(\"Electric\"));")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.AND, ((BinaryExpressionNode) expressionNode).getOperator());
        assertInstanceOf(BinaryExpressionNode.class, ((BinaryExpressionNode) expressionNode).getRhs());
        assertEquals(TokenType.OR, ((BinaryExpressionNode) ((BinaryExpressionNode) expressionNode).getRhs()).getOperator());
    }

    @Test
    void parseLogicalOrExpression() throws ParseException {
        // Test valid EXPRESSION statement with logical OR operation
        ExpressionNode expressionNode = createParser("P::name.eq(\"Pika\") OR P::hp.gt(50);")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.OR, ((BinaryExpressionNode) expressionNode).getOperator());

        // Test valid nested EXPRESSION statement
        expressionNode = createParser("P::name.eq(\"Pika\") OR P::hp.gt(50) OR P::type.eq(\"Electric\");")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.OR, ((BinaryExpressionNode) expressionNode).getOperator());
        assertInstanceOf(BinaryExpressionNode.class, ((BinaryExpressionNode) expressionNode).getLhs());
        assertEquals(TokenType.OR, ((BinaryExpressionNode) ((BinaryExpressionNode) expressionNode).getLhs()).getOperator());

        // Test valid nested EXPRESSION statement with parentheses
        expressionNode = createParser("P::name.eq(\"Pika\") OR (P::hp.gt(50) AND P::type.eq(\"Electric\"));")
                .parseExpressionNode();
        assertNotNull(expressionNode);
        assertInstanceOf(BinaryExpressionNode.class, expressionNode);
        assertEquals(TokenType.OR, ((BinaryExpressionNode) expressionNode).getOperator());
        assertInstanceOf(BinaryExpressionNode.class, ((BinaryExpressionNode) expressionNode).getRhs());
        assertEquals(TokenType.AND, ((BinaryExpressionNode) ((BinaryExpressionNode) expressionNode).getRhs()).getOperator());
    }

    @Test
    void parseNamespacedField() throws ParseException {
        // Test valid namespaced field
        NamespacedFieldNode namespacedFieldContext = createParser("P::name;")
                .parseNamespacedField();
        assertNotNull(namespacedFieldContext);
        assertEquals("P", namespacedFieldContext.getNamespace().getText());
        assertEquals("name", namespacedFieldContext.getField().getText());

        // Test invalid namespaced field with missing field
        try {
            createParser("P::;")
                    .parseNamespacedField();
            fail("Expected ParseException for missing field in the namespaced field");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test invalid namespaced field with missing namespace
        try {
            createParser("::name;")
                    .parseNamespacedField();
            fail("Expected ParseException for missing namespace in the namespaced field");
        } catch (LexerException e) {
            // Expected exception
        }

        // Test invalid namespaced field with missing namespace and field
        try {
            createParser("::;")
                    .parseNamespacedField();
            fail("Expected ParseException for missing namespace and field in the namespaced field");
        } catch (LexerException e) {
            // Expected exception
        }

        // Test invalid namespaced field with missing ::
        try {
            createParser("Pname;")
                    .parseNamespacedField();
            fail("Expected ParseException for missing :: in the namespaced field");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test invalid namespaced field with only one :
        try {
            createParser("P:name;")
                    .parseNamespacedField();
            fail("Expected ParseException for missing :: in the namespaced field");
        } catch (LexerException e) {
            // Expected exception
        }
    }

}