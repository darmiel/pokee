package com.github.pokee.bootstrap.json;

import com.github.pokee.json.Pson;
import com.github.pokee.json.PsonBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

public class JsonMarshalBootstrap {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Always {
        String value();
    }

    public enum TestEnum {
        A, B, C
    }

    public record Test(
            @Always("hehe") String name,
            @Always("haaaaaaaaa") String name2,
            int age,
            List<Test> inner,
            TestEnum testEnum
    ) {
    }

    public static void main(String[] args) {
        final Test test = new Test("Daniel", "Daniel2", 21, Arrays.asList(
                new Test("Jan", "Jan2", 21, null, TestEnum.C),
                new Test("Kevin", "Kevin2", 12, null, TestEnum.B)
        ), TestEnum.B);

        final Pson pson = Pson.create()
                .prettyPrint()
                .serializeNulls()
                .registerValueWriterMapper(
                        String.class,
                        PsonBuilder.hasAnnotation(Always.class, a -> a.value().length() > 5),
                        (writer, bob, field, value) -> writer.writeString(bob, field.getAnnotation(Always.class).value())
                )
                .build();

        final StringBuilder bob = new StringBuilder();
        pson.getJsonWriter().write(bob, null, test, 0);
        System.out.println(bob);
    }

}
