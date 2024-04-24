package com.github.pokee.pson;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PsonTest {

    private final Pson pson = Pson.createWithDefaults().build();

    @Test
    void testMarshal() {
        class Test {
            private final String string;
            private final int integer;
            private final double aDouble;
            private final float aFloat;
            private final boolean aBoolean;

            public Test(String string, int integer, double aDouble, float aFloat, boolean aBoolean) {
                this.string = string;
                this.integer = integer;
                this.aDouble = aDouble;
                this.aFloat = aFloat;
                this.aBoolean = aBoolean;
            }

            @Override
            public boolean equals(Object object) {
                if (this == object) return true;
                if (object == null || getClass() != object.getClass()) return false;
                Test test = (Test) object;
                return integer == test.integer && Double.compare(aDouble, test.aDouble) == 0 && Float.compare(aFloat, test.aFloat) == 0 && aBoolean == test.aBoolean && Objects.equals(string, test.string);
            }

            @Override
            public int hashCode() {
                return Objects.hash(string, integer, aDouble, aFloat, aBoolean);
            }

            @Override
            public String toString() {
                return "Test{" +
                        "string='" + string + '\'' +
                        ", integer=" + integer +
                        ", aDouble=" + aDouble +
                        ", aFloat=" + aFloat +
                        ", aBoolean=" + aBoolean +
                        '}';
            }
        }

        System.out.println(Arrays.toString(Test.class.getDeclaredFields()));

        final Test input = new Test("test", 10, 1.5, 1.5f, true);
        System.out.println("Before: " + input);
        final Test output = pson.unmarshalObject(pson.marshal(input), Test.class);
        System.out.println("After: " + output);
        assertEquals(input, output);
    }

}