package com.github.pokee.psql.visitor;

import com.github.pokee.psql.exception.SemanticException;

import java.util.Map;

public class AliasNamespacedBasePsqlVisitor<T> extends BasePsqlVisitor<T> {

    protected final Map<String, String> importedNamespaces;

    public AliasNamespacedBasePsqlVisitor(final Map<String, String> importedNamespaces) {
        this.importedNamespaces = importedNamespaces;
    }

    public String resolveNamespace(final String namespace) {
        if (!this.importedNamespaces.containsKey(namespace)) {
            throw new SemanticException("Namespace not found: " + namespace);
        }
        return importedNamespaces.get(namespace);
    }

}
