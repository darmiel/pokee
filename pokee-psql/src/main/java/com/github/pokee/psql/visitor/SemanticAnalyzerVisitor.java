package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProjectionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseStatementContext;
import com.github.pokee.psql.exception.SemanticException;

import java.util.*;

public class SemanticAnalyzerVisitor extends BasePsqlVisitor<Void> {

    public static final Map<String, Map<String, Class<?>>> EXAMPLE_NAMESPACE_PROJECTIONS = new HashMap<>() {
        {
            put("Pokemon", new HashMap<>() {
                {
                    put("id", Integer.class);
                    put("name", String.class);
                    put("type", String.class);
                    put("hp", Integer.class);
                }
            });
        }
    };

    private final Map<String, String> namespaceImports;
    private final Set<String> queryNames;

    private final Map<String, Map<String, Class<?>>> namespaceProjections;
    private final List<String> availableLanguages;

    public SemanticAnalyzerVisitor(final Map<String, Map<String, Class<?>>> namespaceProjections,
                                   final List<String> availableLanguages) {
        super();

        this.namespaceProjections = namespaceProjections;
        this.availableLanguages = availableLanguages;

        this.namespaceImports = new HashMap<>();
        this.queryNames = new HashSet<>();
    }

    @Override
    public Void visitUseStatement(final UseStatementContext useStatementContext) {
        // check if the imported namespace is valid
        final String originalNamespace = useStatementContext.getOriginal().getText();
        if (!this.namespaceProjections.containsKey(originalNamespace)) {
            throw new SemanticException("Namespace " + originalNamespace + " is not valid");
        }

        // check if the alias is already used
        final String importName = useStatementContext.getAlias() != null
                ? useStatementContext.getAlias().getText()
                : originalNamespace;
        if (this.namespaceImports.containsKey(importName)) {
            throw new SemanticException("Alias " + importName + " is already used");
        }
        this.namespaceImports.put(importName, originalNamespace);

        return super.visitUseStatement(useStatementContext);
    }

    public Map<String, Class<?>> getFieldTypes(final String namespace) {
        // map alias to original namespace
        final String targetNamespace = this.namespaceImports.get(namespace);
        if (targetNamespace == null) {
            throw new SemanticException("Namespace " + namespace + " is not imported");
        }
        // check if the field is valid
        final Map<String, Class<?>> validFieldNames = this.namespaceProjections.get(targetNamespace);
        if (validFieldNames == null) {
            throw new SemanticException("Namespace " + namespace + " is not valid");
        }
        return validFieldNames;
    }

    public Class<?> getFieldType(final Map<String, Class<?>> fieldTypes, final String namespace, final String field) {
        // get field type
        final Class<?> fieldType = fieldTypes.get(field);
        if (fieldType == null) {
            throw new SemanticException("Field " + field + " is not valid for namespace " + namespace);
        }
        return fieldType;
    }

    @Override
    public Void visitQuery(final QueryContext queryContext) {
        // check if query name was not already defined
        final String queryName = queryContext.getQueryName().getText();
        if (this.queryNames.contains(queryName)) {
            throw new SemanticException("Query name " + queryName + " is already used");
        }
        this.queryNames.add(queryName);

        // make sure projections are valid
        for (final ProjectionNode projectionNode : queryContext.getProjectionNodes()) {
            final String namespace = projectionNode.getNamespace().getText();

            final Map<String, Class<?>> fieldTypes = this.getFieldTypes(namespace);

            // skip if projection is *
            if (projectionNode.isAll()) {
                continue;
            }

            final String fieldName = projectionNode.getField().getText();
            if (fieldName == null) {
                throw new SemanticException("Field name is null");
            }

            this.getFieldType(fieldTypes, namespace, fieldName);
        }

        return super.visitQuery(queryContext);
    }

    @Override
    public Void visitFunctionCallExpressionNode(final FunctionCallExpressionNode functionCallExpressionNode) {
        final String namespace = functionCallExpressionNode.getTargetNamespace().getText();
        final Map<String, Class<?>> fieldTypes = this.getFieldTypes(namespace);

        final String fieldName = functionCallExpressionNode.getTarget().getText();
        final Class<?> fieldType = this.getFieldType(fieldTypes, namespace, fieldName);

        final String functionName = functionCallExpressionNode.getFunctionName();

        // check if function is valid
        final QueryFunctions function = QueryFunctions.getFunction(functionName);
        if (function == null) {
            throw new SemanticException("Function " + functionName + " not found");
        }

        // check if function is valid for field type
        if (!function.canBeUsedOn(fieldType)) {
            throw new SemanticException("Function " + functionName + " cannot be used on field " + fieldName);
        }

        if (function.getArgumentTypes().length != functionCallExpressionNode.getArguments().size()) {
            throw new SemanticException("Function " + functionName + " expects " + function.getArgumentTypes().length + " arguments");
        }

        return super.visitFunctionCallExpressionNode(functionCallExpressionNode);
    }

    @Override
    public Void visitLanguage(final LanguageContext languageContext) {
        // check if language is valid
        if (!this.availableLanguages.contains(languageContext.getLanguage().getText())) {
            throw new SemanticException("Language " + languageContext.getLanguage().getText() + " is not available");
        }
        return super.visitLanguage(languageContext);
    }
}
