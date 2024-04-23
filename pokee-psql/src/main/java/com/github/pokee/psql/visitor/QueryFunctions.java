package com.github.pokee.psql.visitor;

public enum QueryFunctions {

    STARTS_WITH("starts_with", Boolean.class, new Class[]{String.class}, new Class[]{String.class}),
    ENDS_WITH("ends_with", Boolean.class, new Class[]{String.class}, new Class[]{String.class}),
    CONTAINS("contains", Boolean.class, new Class[]{String.class}, new Class[]{String.class}),
    LENGTH("length", Integer.class, new Class[]{String.class}, new Class[]{}),
    LOWER("lower", String.class, new Class[]{String.class}, new Class[]{}),
    UPPER("upper", String.class, new Class[]{String.class}, new Class[]{}),
    TRIM("trim", String.class, new Class[]{String.class}, new Class[]{}),
    LTRIM("ltrim", String.class, new Class[]{String.class}, new Class[]{}),
    RTRIM("rtrim", String.class, new Class[]{String.class}, new Class[]{});


    private final String functionName;
    private final Class<?> returnType;
    private final Class<?>[] targetTypes;
    private final Class<?>[] argumentTypes;

    QueryFunctions(final String functionName,
                   final Class<?> returnType,
                   final Class<?>[] targetTypes,
                   final Class<?>[] argumentTypes) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.targetTypes = targetTypes;
        this.argumentTypes = argumentTypes;
    }

    public static QueryFunctions getFunction(final String functionName) {
        for (final QueryFunctions function : QueryFunctions.values()) {
            if (function.functionName.equals(functionName)) {
                return function;
            }
        }
        return null;
    }

    public boolean canBeUsedOn(final Class<?> targetType) {
        for (final Class<?> type : targetTypes) {
            if (type == targetType) {
                return true;
            }
        }
        return false;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

}
