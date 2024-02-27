package com.github.pokee.json.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as optional
 * If the field is not present in the JSON object, the field will be set to null
 * If the annotation is not present and the field is not present in the JSON object, an exception will be thrown
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonOptional {
}
