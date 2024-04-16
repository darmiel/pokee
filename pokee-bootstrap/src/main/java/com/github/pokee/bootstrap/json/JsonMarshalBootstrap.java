package com.github.pokee.bootstrap.json;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.PsonBuilder;

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
        B, C
    }

    public record Test(
            @Always("uu") String name,
            String name2,
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

        final Pson pson = Pson.createWithDefaults()
                .registerValueWriterMapper(
                        String.class,
                        PsonBuilder.hasAnnotation(Always.class, a -> a.value().length() > 5),
                        (writer, bob, field, value) -> writer.writeString(bob, field.getAnnotation(Always.class).value())
                )
                .build();

        System.out.println(pson.marshal(test));
    }

}
