package com.github.pokee.json.token;

public enum JsonTokenType {

    // document
    END_DOCUMENT,

    // objects
    BEGIN_OBJECT,
    END_OBJECT,

    // arrays
    BEGIN_ARRAY,
    END_ARRAY,

    // values
    NAME_SEPARATOR,
    VALUE_SEPARATOR,

    // primitives
    STRING,
    NUMBER,
    BOOLEAN,
    NULL

}
