package com.github.pokee.stick.headers;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Headers {

    protected final Map<String, List<String>> headers;

    public Headers(final Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Headers() {
        this.headers = new HashMap<>();
    }

    protected String getKey(final String key) {
        for (String k : this.headers.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return k;
            }
        }
        return null;
    }

    protected String getKey(final String key, final String defaultKey) {
        final String mapKey = this.getKey(key);
        return mapKey != null ? mapKey : defaultKey;
    }

    protected List<String> mutableSingleton(final String value) {
        final List<String> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    /**
     * Get the value of a header
     *
     * @param key the header key
     * @return the header value
     */
    public boolean contains(final String key) {
        return this.getKey(key) != null;
    }

    /**
     * Add a header
     *
     * @param key   the header key
     * @param value the header value
     */
    public void add(final String key, final String value) {
        headers.computeIfAbsent(this.getKey(key, key), k -> new ArrayList<>()).add(value);
    }

    /**
     * Set a header
     *
     * @param key   the header key
     * @param value the header value
     */
    public void set(final String key, final String value) {
        this.headers.put(this.getKey(key, key), this.mutableSingleton(value));
    }

    @Override
    public String toString() {
        return headers.toString();
    }

    /**
     * Pretty print the headers
     *
     * @return the pretty printed headers
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
     * Return an immutable version of the headers
     *
     * @return the immutable headers
     */
    public ImmutableHeaders toImmutable() {
        return new ImmutableHeaders(this);
    }

    public Set<Map.Entry<String, List<String>>> entries() {
        return this.headers.entrySet();
    }

}
