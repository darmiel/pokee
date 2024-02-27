package com.github.pokee.json;

import com.github.pokee.json.mapper.FieldMapper;
import com.github.pokee.json.mapper.ValueReaderMapper;
import com.github.pokee.json.mapper.ValueWriterMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * Builder for PsonImpl
 */
public class PsonBuilder {

    public static final String DEFAULT_PRETTY_PRINT_INDENT = "  ";

    public static final Map<Class<?>, ValueReaderMapper> DEFAULT_VALUE_READER_MAPPERS = Map.of(

    );

    public static final Map<Class<?>, ValueWriterMapper> DEFAULT_VALUE_WRITER_MAPPERS = Map.of(

    );

    // null = no pretty print
    private String prettyPrintIndent = null;
    private boolean serializeNulls = false;

    private final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMappers = new HashMap<>();
    private final Map<Class<?>, ValueReaderMapper> valueReaderMappers = new HashMap<>(DEFAULT_VALUE_READER_MAPPERS);


    public static PsonBuilder create() {
        return new PsonBuilder();
    }

    PsonBuilder() {
    }

    /**
     * Pretty print the JSON
     *
     * @return this
     */
    public PsonBuilder prettyPrint() {
        return this.prettyPrint(DEFAULT_PRETTY_PRINT_INDENT);
    }

    /**
     * Pretty print the JSON with the given indent
     *
     * @param indent the indent
     * @return this
     */
    public PsonBuilder prettyPrint(final String indent) {
        this.prettyPrintIndent = indent;
        return this;
    }

    /**
     * Pretty print the JSON with the given number of spaces
     *
     * @param spaces the number of spaces
     * @return this
     */
    public PsonBuilder prettyPrint(final int spaces) {
        this.prettyPrintIndent = " ".repeat(spaces);
        return this;
    }

    /**
     * Serialize nulls
     *
     * @return this
     */
    public PsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    /**
     * Register a value reader mapper.
     * This mapper is used to map a JSON value to an object
     *
     * @param targetClass       the class to map to
     * @param valueReaderMapper the mapper
     * @return this
     */
    public PsonBuilder registerValueReaderMapper(final Class<?> targetClass, final ValueReaderMapper valueReaderMapper) {
        if (this.valueReaderMappers.containsKey(targetClass)) {
            throw new IllegalArgumentException("A value mapper for " + targetClass + " is already registered");
        }
        this.valueReaderMappers.put(targetClass, valueReaderMapper);
        return this;
    }

    /**
     * Register a value writer mapper.
     * This mapper is used to map an object to a JSON value
     *
     * @param targetClass       the class to map from
     * @param fieldPredicate    the predicate to match the field
     * @param valueWriterMapper the mapper
     * @return this
     */
    public PsonBuilder registerValueWriterMapper(
            final Class<?> targetClass,
            final Predicate<Field> fieldPredicate,
            final ValueWriterMapper valueWriterMapper
    ) {
        final List<FieldMapper<ValueWriterMapper>> writeMappers;
        if (this.valueWriterMappers.containsKey(targetClass)) {
            writeMappers = this.valueWriterMappers.get(targetClass);
        } else {
            writeMappers = new ArrayList<>();
            this.valueWriterMappers.put(targetClass, writeMappers);
        }
        // check if a mapper for the field is already registered
        final FieldMapper<ValueWriterMapper> fieldMapper = new FieldMapper<>(valueWriterMapper, fieldPredicate);
        if (writeMappers.contains(fieldMapper)) {
            throw new IllegalArgumentException("A value mapper for " + targetClass + " is already registered");
        }
        writeMappers.add(0, fieldMapper);
        return this;
    }

    public PsonBuilder registerValueWriterMapper(
            final Class<?> targetClass,
            final ValueWriterMapper valueWriterMapper
    ) {
        return this.registerValueWriterMapper(targetClass, null, valueWriterMapper);
    }

    /**
     * Predicate to check if a field is not null
     *
     * @return the predicate
     */
    @SuppressWarnings("unused")
    public static Predicate<Field> notNull() {
        return Objects::nonNull;
    }

    /**
     * Predicate to check if a field has a specific annotation
     *
     * @param annotation the annotation
     * @return the predicate
     */
    @SuppressWarnings("unused")
    public static Predicate<Field> hasAnnotation(final Class<? extends Annotation> annotation) {
        return PsonBuilder.notNull().and(field -> field.isAnnotationPresent(annotation));
    }

    /**
     * Predicate to check if a field has a specific annotation and the annotation matches the predicate
     *
     * @param annotation          the annotation
     * @param annotationPredicate the predicate for the annotation
     * @param <A>                 the annotation type
     * @return the predicate
     */
    @SuppressWarnings("unused")
    public static <A extends Annotation> Predicate<Field> hasAnnotation(
            final Class<A> annotation,
            final Predicate<A> annotationPredicate
    ) {
        return PsonBuilder.hasAnnotation(annotation)
                .and(field -> annotationPredicate.test(field.getAnnotation(annotation)));
    }

    /**
     * Build the PsonImpl
     *
     * @return PsonImpl
     */
    public Pson build() {
        return new Pson(
                this.serializeNulls,
                this.prettyPrintIndent,
                this.valueWriterMappers,
                this.valueReaderMappers
        );
    }

}
