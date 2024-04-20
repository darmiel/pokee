package com.github.pokee.pswf.router;

import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.router.handler.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in a routing tree, storing mappings from URL parts to child nodes
 * and HTTP methods to handlers.
 */
public class RouteNode {

    private final Map<String, RouteNode> children;
    private final Map<Method, List<Handler>> handlers;

    private final String part;
    private final boolean isParameter;

    /**
     * Constructs a new RouteNode with the specified URL part and a flag indicating whether
     * it represents a parameterized segment.
     *
     * @param part        The part of the route associated with this node.
     * @param isParameter Indicates whether the part is a parameter.
     */
    public RouteNode(final String part, final boolean isParameter) {
        this.children = new HashMap<>();
        this.handlers = new HashMap<>();

        this.part = part;
        this.isParameter = isParameter;
    }

    /**
     * Adds or retrieves a child node for a given part of the URL. If the part already exists,
     * the existing node is returned.
     *
     * @param part The part of the URL to add as a child.
     * @return The child node corresponding to the part.
     */
    public RouteNode addChild(final String part) {
        if (this.children.containsKey(part)) {
            return this.children.get(part);
        }

        final boolean isParameter = part.startsWith(":");
        final String actualPart = isParameter ? part.substring(1) : part;

        final RouteNode node = new RouteNode(actualPart, isParameter);
        this.children.put(part, node);

        return node;
    }

    /**
     * Retrieves the child node corresponding to a given part of the URL.
     *
     * @param part The part to get the child node for.
     * @return The child node, or null if no such node exists.
     */
    public RouteNode getChild(final String part) {
        return this.children.get(part);
    }
    /**
     * Adds a handler for a specific HTTP method to this node.
     *
     * @param method  The HTTP method for which to add the handler.
     * @param handler The handler to process requests for the given method.
     */
    public void addHandler(final Method method, final Handler handler) {
        this.handlers.computeIfAbsent(method, k -> new ArrayList<>()).add(handler);
    }
    /**
     * Retrieves the list of handlers associated with a given HTTP method at this node.
     *
     * @param method The HTTP method for which handlers are requested.
     * @return A list of handlers, or null if no handlers are available for the method.
     */
    public List<Handler> getHandlers(final Method method) {
        return this.handlers.get(method);
    }

    /**
     * Returns a map of all children nodes.
     *
     * @return A map where each key is a part of the URL and each value is the corresponding RouteNode.
     */
    Map<String, RouteNode> getChildren() {
        return this.children;
    }

    /**
     * Checks if this node represents a parameterized part of the route.
     *
     * @return true if this node is parameterized, otherwise false.
     */
    public boolean isParameter() {
        return this.isParameter;
    }

    /**
     * Returns the part of the URL associated with this node.
     *
     * @return The part of the URL.
     */
    public String getPart() {
        return this.part;
    }

}
