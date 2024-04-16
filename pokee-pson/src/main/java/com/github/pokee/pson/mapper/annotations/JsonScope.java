package com.github.pokee.pson.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as optional
 * If the field is not present in the JSON object, the field will be set to null
 * If the annotation is not present and the field is not present in the JSON object, an exception will be thrown
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonScope {

    boolean DEFAULT_PRIVATE_FIELDS = true;
    boolean DEFAULT_PUBLIC_FIELDS = true;
    boolean DEFAULT_TRANSIENT_FIELDS = false;
    boolean DEFAULT_INHERITED_FIELDS = true;

    boolean privateFields() default DEFAULT_PRIVATE_FIELDS;

    boolean publicFields() default DEFAULT_PUBLIC_FIELDS;

    boolean transientFields() default DEFAULT_TRANSIENT_FIELDS;

    // TODO:
    // boolean inheritedFields() default DEFAULT_INHERITED_FIELDS;

}
