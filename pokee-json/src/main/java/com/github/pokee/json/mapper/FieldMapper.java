package com.github.pokee.json.mapper;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * A field mapper is a combination of a predicate and a mapper. The predicate is used to determine if a field should be
 * mapped by the mapper. This allows for easy filtering of different mappers using e.g. annotations or name-based fields
 *
 * @param mapper    The mapper to use for the field
 * @param predicate The predicate to determine if the field should be mapped by the mapper
 * @param <T>       The type of the mapper
 */
public record FieldMapper<T extends Mapper>(T mapper, Predicate<Field> predicate) {

}