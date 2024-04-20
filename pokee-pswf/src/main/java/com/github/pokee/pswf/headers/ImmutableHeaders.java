package com.github.pokee.pswf.headers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extends the {@link Headers} class to create an immutable version of headers.
 * This class is designed to prevent any modifications to the headers, making it suitable for use
 * where header information needs to remain unchanged.
 */
public class ImmutableHeaders extends Headers {

    /**
     * Constructs an ImmutableHeaders instance copying the headers from an existing Headers object.
     *
     * @param headers The Headers object whose header entries are to be copied.
     */
    public ImmutableHeaders(Headers headers) {
        super(headers.headers);
    }

    /**
     * Constructs an ImmutableHeaders instance initialized with the provided headers map.
     *
     * @param headers a map containing initial header values.
     */
    public ImmutableHeaders(final Map<String, List<String>> headers) {
        super(headers);
    }

    /**
     * Constructs an empty ImmutableHeaders instance.
     */
    public ImmutableHeaders() {
        super();
    }

    /**
     * Overrides the add method to prevent modifications by throwing an UnsupportedOperationException.
     *
     * @param key   the header key
     * @param value the header value to add
     * @throws UnsupportedOperationException because ImmutableHeaders does not support add operation
     */
    @Override
    public void add(String key, String value) {
        throw new UnsupportedOperationException("ImmutableHeaders does not support add operation");
    }


    /**
     * Provides access to the header entries as a set, wrapped in a new HashSet to prevent modifications.
     *
     * @return a set of map entries representing the headers, enclosed in an unmodifiable set
     */
    @Override
    public Set<Map.Entry<String, List<String>>> entries() {
        return new HashSet<>(this.headers.entrySet());
    }

}
