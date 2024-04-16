package com.github.pokee.stick.headers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ImmutableHeaders is a wrapper class for Headers that does not support modify operations such as `add`.
 */
public class ImmutableHeaders extends Headers {

    public ImmutableHeaders(Headers headers) {
        super(headers.headers);
    }

    public ImmutableHeaders(final Map<String, List<String>> headers) {
        super(headers);
    }

    public ImmutableHeaders() {
        super();
    }

    @Override
    public void add(String key, String value) {
        throw new UnsupportedOperationException("ImmutableHeaders does not support add operation");
    }

    @Override
    public Set<Map.Entry<String, List<String>>> entries() {
        return new HashSet<>(this.headers.entrySet());
    }

}
