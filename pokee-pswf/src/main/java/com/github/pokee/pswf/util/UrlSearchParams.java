package com.github.pokee.pswf.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class to parse URL query strings into a map of parameters and their values.
 */
public class UrlSearchParams {

    private final Map<String, String> params;

    /**
     * Constructs an instance of {@code UrlSearchParams} initialized with parsed parameters from the provided query string.
     *
     * @param query the URL query string to parse
     */
    public UrlSearchParams(final String query) {
        this.params = parse(query);
    }

    /**
     * Parses the provided URL query string into a map of parameter keys and values.
     * <p>
     * Parameters and values are separated by '=', and each pair is separated by '&'.
     * Empty values result in the key being stored with an empty string as its value.
     *
     * @param query the URL query string to parse
     * @return a map containing the parameters and their respective values
     */
    private static Map<String, String> parse(final String query) {
        final Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }

        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx != -1) {
                // ?abc=def&ghi=jkl
                final String key = pair.substring(0, idx);
                final String value = (idx < pair.length() - 1) ? pair.substring(idx + 1) : "";
                result.put(key, value);
            } else {
                // ?abc&def=ghi
                result.put(pair, "");
            }
        }

        return result;
    }

    /**
     * Escapes a URL parameter value.
     *
     * @param value the value to escape
     * @return the escaped value
     */
    public static String escape(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Unescapes a URL parameter value.
     *
     * @param value the value to unescape
     * @return the unescaped value
     */
    public static String unescape(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    /**
     * Retrieves the value associated with the given parameter key.
     *
     * @param key the parameter key to look up
     * @return the value associated with the key, or {@code null} if the key does not exist
     */
    public String get(String key) {
        return params.get(key);
    }

    /**
     * Sets or updates the value associated with the given parameter key.
     *
     * @param key   the parameter key to set or update
     * @param value the value to associate with the key
     */
    public void set(String key, String value) {
        params.put(key, value);
    }

    /**
     * Removes the parameter associated with the given key.
     *
     * @param key the parameter key to remove
     */
    public void remove(String key) {
        params.remove(key);
    }

    /**
     * Returns the number of parameters stored.
     *
     * @return the number of parameters
     */
    public int size() {
        return params.size();
    }

    /**
     * Returns if a parameter with the given key exists.
     *
     * @param key the parameter key to check
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public boolean has(String key) {
        return params.containsKey(key);
    }

    /**
     * Returns a URL-encoded query string representing the stored parameters and their values.
     *
     * @return a URL-encoded string
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!result.isEmpty()) {
                result.append("&");
            }
            result.append(UrlSearchParams.escape(entry.getKey()))
                    .append("=")
                    .append(UrlSearchParams.escape(entry.getValue()));
        }
        return result.toString();
    }

}
