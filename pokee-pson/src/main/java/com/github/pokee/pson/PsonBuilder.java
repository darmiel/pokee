package com.github.pokee.pson;

import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.mapper.*;
import com.github.pokee.pson.parser.JsonFunctionRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * Builder for Pson
 */
@SuppressWarnings("unused")
public class PsonBuilder {

    public static final String DEFAULT_PRETTY_PRINT_INDENT = "  ";

    public static final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> DEFAULT_VALUE_READER_MAPPERS = Map.of(
            String.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asString())),
            Integer.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asInteger())),
            int.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asInteger())),
            Double.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asDouble())),
            double.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asDouble())),
            Float.class, List.of(FieldMapper.wrap((element, field) -> (float) element.asPrimitive().asDouble())),
            float.class, List.of(FieldMapper.wrap((element, field) -> (float) element.asPrimitive().asDouble())),
            Boolean.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asBoolean())),
            boolean.class, List.of(FieldMapper.wrap((element, field) -> element.asPrimitive().asBoolean()))
    );

    public static final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> DEFAULT_VALUE_WRITER_MAPPERS = Map.of(
            String.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append('"').append(JsonWriterMapper.escapeString((String) value)).append('"'))),
            Integer.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            int.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            Double.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            double.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            Float.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            float.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            Boolean.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))),
            boolean.class, List.of(FieldMapper.wrap((writer, bob, field, value) -> bob.append(value))
            ));

    /// Writing
    // null = no pretty print
    private String prettyPrintIndent = null;
    private boolean serializeNulls = false;

    /// Reading
    // functions
    private final JsonFunctionRunner jsonFunctionRunner;
    private boolean expandFunctions = true;

    private final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMappers
            = new HashMap<>(DEFAULT_VALUE_WRITER_MAPPERS);
    private final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> valueReaderMappers
            = new HashMap<>(DEFAULT_VALUE_READER_MAPPERS);

    PsonBuilder() {
        this.jsonFunctionRunner = JsonFunctionRunner.defaultFunctionRunner();
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
     * Expand functions
     *
     * @param expandFunctions expand functions
     * @return this
     */
    public PsonBuilder expandFunctions(final boolean expandFunctions) {
        this.expandFunctions = expandFunctions;
        return this;
    }

    /**
     * Expand functions
     *
     * @return this
     */
    public PsonBuilder expandFunctions() {
        return this.expandFunctions(true);
    }

    /**
     * Register a value writer mapper.
     * This mapper is used to map an object to a JSON value
     *
     * @param mappers        the mappers
     * @param targetClass    the class to map from
     * @param fieldPredicate the predicate to match the field
     * @param mapper         the mapper
     * @param <T>            the type of the mapper
     * @return this
     */
    private <T extends Mapper> PsonBuilder registerGenericMapper(
            final Map<Class<?>, List<FieldMapper<T>>> mappers,
            final Class<?> targetClass,
            final Predicate<Field> fieldPredicate,
            final T mapper
    ) {
        final List<FieldMapper<T>> mapperList;
        if (mappers.containsKey(targetClass)) {
            mapperList = mappers.get(targetClass);
        } else {
            mapperList = new ArrayList<>();
            mappers.put(targetClass, mapperList);
        }
        // check if a mapper for the field is already registered
        final FieldMapper<T> fieldMapper = new FieldMapper<>(mapper, fieldPredicate);
        if (mapperList.contains(fieldMapper)) {
            throw new IllegalArgumentException("A value mapper for " + targetClass + " is already registered");
        }
        mapperList.add(0, fieldMapper);
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
    public PsonBuilder registerValueReaderMapper(
            final Class<?> targetClass,
            final Predicate<Field> fieldPredicate,
            final ValueReaderMapper valueReaderMapper
    ) {
        return this.registerGenericMapper(this.valueReaderMappers, targetClass, fieldPredicate, valueReaderMapper);
    }

    /**
     * Register a value reader mapper.
     *
     * @param targetClass       the class to map to
     * @param valueReaderMapper the mapper
     * @return this
     */
    public PsonBuilder registerValueReaderMapper(
            final Class<?> targetClass,
            final ValueReaderMapper valueReaderMapper
    ) {
        return this.registerValueReaderMapper(targetClass, null, valueReaderMapper);
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
        return this.registerGenericMapper(this.valueWriterMappers, targetClass, fieldPredicate, valueWriterMapper);
    }

    /**
     * Register a value writer mapper.
     * This mapper is used to map an object to a JSON value
     *
     * @param targetClass       the class to map from
     * @param valueWriterMapper the mapper
     * @return this
     */
    public PsonBuilder registerValueWriterMapper(
            final Class<?> targetClass,
            final ValueWriterMapper valueWriterMapper
    ) {
        return this.registerValueWriterMapper(targetClass, null, valueWriterMapper);
    }

    /**
     * Register a mapper for a specific class
     *
     * @param targetClass the class to map from
     * @param mapper      the mapper
     * @return this
     */
    public PsonBuilder registerMapper(
            final Class<?> targetClass,
            final Predicate<Field> fieldPredicate,
            final ValueReaderWriterMapper mapper
    ) {
        return this
                .registerValueReaderMapper(targetClass, fieldPredicate, mapper)
                .registerValueWriterMapper(targetClass, fieldPredicate, mapper);
    }

    /**
     * Register a mapper for a specific class
     *
     * @param targetClass the class to map from
     * @param mapper      the mapper
     * @return this
     */
    public PsonBuilder registerMapper(
            final Class<?> targetClass,
            final ValueReaderWriterMapper mapper
    ) {
        return this.registerMapper(targetClass, null, mapper);
    }

    /**
     * Register a function callback
     *
     * @param functionName     the name of the function
     * @param functionCallback the function callback
     * @return this
     */
    public PsonBuilder registerFunctionCallback(
            final String functionName,
            final FunctionCallback functionCallback
    ) {
        this.jsonFunctionRunner.registerFunctionCallback(functionName, functionCallback);
        return this;
    }

    /**
     * Predicate to check if a field is not null
     *
     * @return the predicate
     */
    public static Predicate<Field> notNull() {
        return Objects::nonNull;
    }

    /**
     * Predicate to check if a field has a specific annotation
     *
     * @param annotation the annotation
     * @return the predicate
     */
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
    public static <A extends Annotation> Predicate<Field> hasAnnotation(
            final Class<A> annotation,
            final Predicate<A> annotationPredicate
    ) {
        return PsonBuilder.hasAnnotation(annotation)
                .and(field -> annotationPredicate.test(field.getAnnotation(annotation)));
    }

    /**
     * Build the Pson instance
     *
     * @return the Pson instance
     */
    public Pson build() {
        return new Pson(
                this.serializeNulls,
                this.prettyPrintIndent,
                this.valueWriterMappers,
                this.valueReaderMappers,
                this.jsonFunctionRunner,
                this.expandFunctions
        );
    }

}
