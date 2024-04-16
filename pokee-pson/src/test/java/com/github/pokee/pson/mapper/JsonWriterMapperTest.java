package com.github.pokee.pson.mapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriterMapperTest {

    @Test
    public void escapeString() {
        record Case(String message, String input, String expected) {
        }
        for (final Case testCase : new Case[]{
                new Case(
                        "Basic alphanumeric characters, no escaping needed.",
                        "Hello, World!",
                        "Hello, World!"
                ),
                new Case(
                        "Double quotes need to be escaped.",
                        "Hello, \"World\"!",
                        "Hello, \\\"World\\\"!"
                ),
                new Case(
                        "Backslashes need to be escaped.",
                        "C:\\Program Files",
                        "C:\\\\Program Files"
                ),
                new Case(
                        "Newline need to be escaped.",
                        "Hello,\nWorld!",
                        "Hello,\\nWorld!"
                ),
                new Case(
                        "Carriage return need to be escaped.",
                        "Hello,\rWorld!",
                        "Hello,\\rWorld!"
                ),
                new Case(
                        "Tab need to be escaped.",
                        "Hello,\tWorld!",
                        "Hello,\\tWorld!"
                ),
                new Case(
                        "Form feed need to be escaped.",
                        "Hello,\fWorld!",
                        "Hello,\\fWorld!"
                ),
                new Case(
                        "Backspace need to be escaped.",
                        "Hello,\bWorld!",
                        "Hello,\\bWorld!"
                ),
                new Case(
                        "Unicode characters should be properly represented.",
                        "Hello, 世界!",
                        "Hello, 世界!"
                ),
                new Case(
                        "Control characters should be escaped.",
                        "Hello,\u0001World!",
                        "Hello,\\u0001World!"
                ),
                new Case(
                        "Slash should be left unescaped.",
                        "https://example.com",
                        "https://example.com"
                ),
                new Case(
                        "Escaping lash is optional",
                        "Hello, /World!",
                        "Hello, /World!"
                ),
                new Case(
                        "Multi-line strings should be handled.",
                        "Hello,\nWorld!\nGoodbye, World!",
                        "Hello,\\nWorld!\\nGoodbye, World!"),
                new Case(
                        "Mixed special characters",
                        "Tab:\tNewline:\nCarriage Return:\r",
                        "Tab:\\tNewline:\\nCarriage Return:\\r"
                ),
                new Case(
                        "JSON special characters mixed with text",
                        "{\"key\": \"value\"}",
                        "{\\\"key\\\": \\\"value\\\"}"
                ),
                new Case(
                        "Empty string should remain empty",
                        "",
                        ""
                ),
                new Case(
                        "String with only a space should be left unescaped",
                        " ",
                        " "
                ),
                new Case(
                        "String with special JSON structure characters",
                        "[{()}]",
                        "[{()}]"
                ),
                new Case(
                        "A long strings combining various cases",
                        "Hello, \"World\"! Path: C:\\Files\nNew Tab:\tEnd",
                        "Hello, \\\"World\\\"! Path: C:\\\\Files\\nNew Tab:\\tEnd"
                )

        }) {
            final String escaped = JsonWriterMapper.escapeString(testCase.input);
            assertEquals(testCase.message + " (escape)", testCase.expected, escaped);

            try {
                final String unescaped = JsonReaderMapper.unescapeString(escaped);
                assertEquals(testCase.message + " (unescape)", testCase.input, unescaped);
            } catch (final Exception e) {
                fail(testCase.message + " (unescape): " + e.getMessage());
            }
        }

        record FailCase(String escaped, boolean fail) {
        }
        for (final FailCase failCase : new FailCase[]{
                new FailCase("Hello,\\\\", false),
                new FailCase("Hello,\\", true),
                new FailCase("Hello,\\\"", false),
                new FailCase("Hello,\\u", true),
                new FailCase("Hello,\\u0", true),
                new FailCase("Hello,\\u00", true),
                new FailCase("Hello,\\u000", true),
                new FailCase("Hello,\\u000g", true),
                new FailCase("Hello,\\u0001", false),
                new FailCase("Hello,\\u00A7", false),
        }) {
            try {
                final String unescaped = JsonReaderMapper.unescapeString(failCase.escaped);
                if (failCase.fail) {
                    fail("Expected to fail: " + failCase.escaped + ", but got: " + unescaped);
                }
            } catch (final Exception e) {
                if (!failCase.fail) {
                    fail("Expected to pass: " + failCase.escaped + ", got: " + e.getMessage());
                }
            }
        }
    }

}