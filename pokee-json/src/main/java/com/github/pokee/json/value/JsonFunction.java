package com.github.pokee.json.value;

import java.util.ArrayList;
import java.util.List;

public class JsonFunction implements JsonElement {

    private final String functionName;
    private final List<JsonElement> parameters;

    public JsonFunction(
            final String functionName,
            final List<JsonElement> parameters
    ) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    /**
     * Get the name of the function
     *
     * @return the name of the function
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Get the parameters of the function
     *
     * @return the parameters of the function
     */
    public JsonElement getParameter(final int index) {
        return parameters.get(index);
    }

    /**
     * Get the number of parameters
     *
     * @return the number of parameters
     */
    public int getParameterCount() {
        return parameters.size();
    }

    /**
     * Get the parameters of the function
     *
     * @return the parameters of the function
     */
    public JsonObject getOptions() {
        return this.getParameterCount() > 1
                ? this.getParameter(1).asObject()
                : new JsonObject();
    }

    /**
     * Copy the function and replace the parameter at the given index
     *
     * @param index       the index of the parameter to replace
     * @param replacement the replacement
     * @return the copy of the function with the replaced parameter
     */
    public JsonFunction copyExceptReplace(final int index, final JsonElement replacement) {
        final List<JsonElement> newParameters = new ArrayList<>(this.parameters);
        newParameters.set(index, replacement);
        return new JsonFunction(functionName, newParameters);
    }

    @Override
    public String toString() {
        return "JsonFunction{" +
                "functionName='" + functionName + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
