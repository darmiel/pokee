package com.github.pokee.pson.mapper.annotations;

import com.github.pokee.pson.mapper.ValueReaderMapper;
import com.github.pokee.pson.mapper.ValueWriterMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonMappper {

    Class<? extends ValueReaderMapper> read() default ValueReaderMapper.class;

    Class<? extends ValueWriterMapper> write() default ValueWriterMapper.class;

}
