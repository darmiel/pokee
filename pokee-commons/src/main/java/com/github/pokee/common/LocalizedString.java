package com.github.pokee.common;

import java.util.HashMap;
import java.util.Map;

public class LocalizedString implements QueryValueConverter {

    private final String defaultLanguage;
    private final Map<String, String> languages;

    public LocalizedString() {
        this.defaultLanguage = "en";
        this.languages = new HashMap<>();
    }

    public LocalizedString(final String defaultLanguage,
                           final Map<String, String> languages) {
        this.defaultLanguage = defaultLanguage;
        this.languages = languages;
    }

    public static LocalizedString fromString(final String value) {
        return new LocalizedString("en", Map.of("en", value));
    }

    public static LocalizedString fromString(final String... strings) {
        if (strings.length == 0 || (strings.length & 1) != 0) {
            throw new IllegalArgumentException("Strings must be in pairs");
        }
        final Map<String, String> languages = new HashMap<>();
        for (int i = 0; i < strings.length; i += 2) {
            languages.put(strings[i], strings[i + 1]);
        }
        return new LocalizedString(strings[0], languages);
    }

    public String get(final String language) {
        if (this.languages.containsKey(language)) {
            return this.languages.get(language);
        }
        return this.languages.getOrDefault(this.defaultLanguage, "");
    }

    public void put(final String language, final String value) {
        this.languages.put(language, value);
    }

    @Override
    public Object convertValue(final String language) {
        return this.get(language);
    }

}
