package com.github.pokee.stick.router;

import com.github.pokee.stick.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteNode {

    private final Map<String, RouteNode> children;
    private final Map<Method, List<Handler>> handlers;

    private final String part;
    private final boolean isParameter;

    public RouteNode(final String part, final boolean isParameter) {
        this.children = new HashMap<>();
        this.handlers = new HashMap<>();

        this.part = part;
        this.isParameter = isParameter;
    }

    /**
     * Add a child to the node.
     *
     * @param part The part of the URL to add as a child.
     * @return The child node.
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
     * Get the child node for the given part.
     *
     * @param part The part to get the child node for.
     * @return The child node for the given part.
     */
    public RouteNode getChild(final String part) {
        return this.children.get(part);
    }

    /**
     * Add a handler to the node.
     *
     * @param method  The method to add the handler for.
     * @param handler The handler to add.
     */
    public void addHandler(final Method method, final Handler handler) {
        this.handlers.computeIfAbsent(method, k -> new ArrayList<>()).add(handler);
    }

    /**
     * Get the handlers for the given method.
     *
     * @param method The method to get the handlers for.
     * @return The handlers for the given method.
     */
    public List<Handler> getHandlers(final Method method) {
        return this.handlers.get(method);
    }

    /**
     * Get the children of the node.
     *
     * @return The children of the node.
     */
    Map<String, RouteNode> getChildren() {
        return this.children;
    }

    /**
     * Check if the node is a parameter.
     *
     * @return Whether the node is a parameter.
     */
    public boolean isParameter() {
        return this.isParameter;
    }

    /**
     * Get the part of the node.
     *
     * @return The part of the node.
     */
    public String getPart() {
        return this.part;
    }

}
