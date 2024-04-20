package com.github.pokee.pswf.router;

import com.github.pokee.pswf.request.Request;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.util.UrlSearchParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains all the relevant information for a single HTTP request handling session.
 * This includes the request itself, any associated handlers, parameters extracted from the URL,
 * and tools for managing response creation.
 */
public class Context {

    private final Request request;
    private final Map<String, Object> locals;
    private final Map<String, String> parameters;
    private final List<Handler> handlers;

    /**
     * The response object to send back to the client.
     * Handlers may modify this as necessary to complete the response lifecycle.
     */
    public Response response = null;

    /**
     * the query parameters
     * only available if a query was requested using `query(key: String)`
     */
    private UrlSearchParams query = null;

    /**
     * Constructs a new Context with specified request details, handlers, and parameters.
     *
     * @param request    The HTTP request associated with this context.
     * @param handlers   The list of handlers responsible for processing the request.
     * @param parameters A map of route parameters extracted from the URL.
     */
    public Context(final Request request,
                   final List<Handler> handlers,
                   final Map<String, String> parameters) {
        this.request = request;
        this.locals = new HashMap<>();
        this.handlers = handlers;
        this.parameters = parameters;
    }

    /**
     * Returns the list of handlers associated with this context.
     *
     * @return The handlers
     */
    public List<Handler> getHandlers() {
        return this.handlers;
    }

    /**
     * Returns the current response object.
     *
     * @return The response
     */
    public Response getResponse() {
        return this.response;
    }

    ///

    /**
     * Retrieves a parameter by key from the route parameters.
     *
     * @param key The key for the parameter.
     * @return The value of the parameter, or null if not found.
     */
    public String param(final String key) {
        return this.parameters.get(key);
    }

    /**
     * Retrieves a parameter by key, returning a default value if the parameter is not found.
     *
     * @param key          The key for the parameter.
     * @param defaultValue The default value to return if the parameter is not found.
     * @return The parameter value or the default value if not found.
     */
    public String param(final String key, final String defaultValue) {
        return this.parameters.getOrDefault(key, defaultValue);
    }

    /**
     * Checks if a particular parameter is present in this context.
     *
     * @param key The key of the parameter to check.
     * @return true if the parameter exists, false otherwise.
     */
    public boolean hasParam(final String key) {
        return this.parameters.containsKey(key);
    }

    /**
     * Lazy-loads and returns the query parameters from the request.
     *
     * @return The URL search parameters parsed from the request query string.
     */
    protected UrlSearchParams getQuery() {
        if (this.query == null) {
            this.query = new UrlSearchParams(this.request.query());
        }
        return this.query;
    }

    /**
     * Retrieves a query parameter by key from the request's query string.
     *
     * @param key The key for the query parameter.
     * @return The value of the query parameter, or null if not found.
     */
    public String query(final String key) {
        return this.getQuery().get(key);
    }

    /**
     * Retrieves a query parameter by key, returning a default value if the parameter is not found.
     *
     * @param key          The key for the query parameter.
     * @param defaultValue The default value to return if the parameter is not found.
     * @return The query parameter value or the default value if not found.
     */
    public String query(final String key, final String defaultValue) {
        if (!this.getQuery().has(key)) {
            return defaultValue;
        }
        return this.getQuery().get(key);
    }

    /**
     * Checks if a particular query parameter is present in the request's query string.
     *
     * @param key The key of the query parameter to check.
     * @return true if the query parameter exists, false otherwise.
     */
    public boolean hasQuery(final String key) {
        return this.getQuery().has(key);
    }

}