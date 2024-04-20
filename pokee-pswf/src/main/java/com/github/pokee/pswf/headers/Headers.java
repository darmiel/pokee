package com.github.pokee.pswf.headers;

import java.util.*;

/**
 * Represents the collection of HTTP headers, allowing for modification and retrieval of header values.
 * This class provides methods to add, set, and query headers in a case-insensitive manner.
 */
public class Headers {

    protected final Map<String, List<String>> headers;

    /**
     * Constructs a new Headers instance initialized with the provided headers.
     *
     * @param headers a map containing initial header values.
     */
    public Headers(final Map<String, List<String>> headers) {
        this.headers = headers;
    }

    /**
     * Constructs an empty Headers instance.
     */
    public Headers() {
        this.headers = new HashMap<>();
    }

    /**
     * Retrieves a header key adjusted for case sensitivity.
     *
     * @param key the header key to find
     * @return the actual key from the map if present, regardless of case; otherwise, null
     */
    protected String getKey(final String key) {
        for (String k : this.headers.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return k;
            }
        }
        return null;
    }

    /**
     * Retrieves a header key adjusted for case sensitivity, or returns a default key if not found.
     *
     * @param key        the header key to find
     * @param defaultKey the default key to return if no matching key is found
     * @return the actual key from the map if present, regardless of case; otherwise, the default key
     */
    protected String getKey(final String key, final String defaultKey) {
        final String mapKey = this.getKey(key);
        return mapKey != null ? mapKey : defaultKey;
    }


    /**
     * Creates a mutable list containing a single value.
     *
     * @param value the value to be placed in the list
     * @return a new list containing the provided value
     */
    protected List<String> mutableSingleton(final String value) {
        final List<String> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    /**
     * Checks if a header is present.
     *
     * @param key the header key
     * @return true if the header is present, otherwise false
     */
    public boolean has(final String key) {
        return this.getKey(key) != null;
    }

    /**
     * Adds a value to a header. If the header does not exist, it is created.
     *
     * @param key   the header key
     * @param value the header value to add
     */
    public void add(final String key, final String value) {
        headers.computeIfAbsent(this.getKey(key, key), k -> new ArrayList<>()).add(value);
    }


    /**
     * Sets a header value, replacing any existing values for that header.
     *
     * @param key   the header key
     * @param value the new value for the header
     */
    public void set(final String key, final String value) {
        this.headers.put(this.getKey(key, key), this.mutableSingleton(value));
    }

    /**
     * Returns a string representation of the headers.
     *
     * @return a string representation of the headers
     */
    @Override
    public String toString() {
        return headers.toString();
    }

    /**
     * Provides a formatted string of all headers, enhancing readability.
     *
     * @return a formatted string representation of the headers
     */
    public String prettyPrint() {
        final StringBuilder bob = new StringBuilder();
        for (final Map.Entry<String, List<String>> entry : headers.entrySet()) {
            bob.append("\t+ ").append(entry.getKey()).append(": ");
            if (entry.getValue().size() > 1) {
                bob.append("[").append("\n");
                for (String value : entry.getValue()) {
                    bob.append("\t\t").append(value).append("\n");
                }
                bob.append("\t]").append("\n");
            } else {
                bob.append(entry.getValue().get(0)).append("\n");
            }
        }
        return bob.toString();
    }


    /**
     * Converts the headers to an immutable structure.
     *
     * @return an immutable headers object
     */
    public ImmutableHeaders toImmutable() {
        return new ImmutableHeaders(this);
    }

    /**
     * Provides access to the header entries as a set.
     *
     * @return a set of map entries representing the headers
     */
    public Set<Map.Entry<String, List<String>>> entries() {
        return this.headers.entrySet();
    }

}
