package com.github.pokee.json.value;

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

    @Override
    public String toString() {
        return "JsonFunction{" +
                "functionName='" + functionName + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
