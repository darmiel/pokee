package com.github.pokee.pswf.util.router;

import com.github.pokee.pswf.annotation.data.Param;
import com.github.pokee.pswf.annotation.data.Query;
import com.github.pokee.pswf.annotation.generator.ContentType;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.response.ResponseBuilder;
import com.github.pokee.pswf.response.ResponseLike;
import com.github.pokee.pswf.router.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Helper class to transform HTTP context into method calls and build responses.
 */
public record MethodTransformer(
        java.lang.reflect.Method method,
        List<ContextTransformer> parameterTransformers,
        List<Consumer<ResponseBuilder>> steps
) {

    public static MethodTransformer fromMethod(final java.lang.reflect.Method method) {
        final List<Consumer<ResponseBuilder>> steps = extractResponseSteps(method);
        final List<ContextTransformer> parameterTransformers = extractParameterTransformers(method);
        return new MethodTransformer(method, parameterTransformers, steps);
    }

    /**
     * Extracts response modification steps from the method's annotations.
     *
     * @param method The method to inspect.
     * @return A list of response builder operations.
     */
    private static List<Consumer<ResponseBuilder>> extractResponseSteps(final java.lang.reflect.Method method) {
        final List<Consumer<ResponseBuilder>> steps = new ArrayList<>();
        if (method.isAnnotationPresent(ContentType.class)) {
            final String contentType = method.getAnnotation(ContentType.class).value();
            steps.add(builder -> builder.contentType(contentType));
        }
        return steps;
    }

    /**
     * Extracts parameter transformers from the method's parameters based on annotations.
     *
     * @param method The method to inspect.
     * @return A list of context to parameter transformers.
     */
    private static List<ContextTransformer> extractParameterTransformers(final java.lang.reflect.Method method) {
        final List<ContextTransformer> parameterTransformers = new ArrayList<>();
        for (final Parameter parameter : method.getParameters()) {
            parameterTransformers.add(MethodTransformer.createTransformerForParameter(parameter));
        }
        return parameterTransformers;
    }

    /**
     * Creates a context transformer based on parameter annotations.
     *
     * @param parameter The method parameter to create a transformer for.
     * @return A context transformer.
     */
    private static ContextTransformer createTransformerForParameter(final Parameter parameter) {
        final Class<?> parameterType = parameter.getType();
        boolean isInteger = Integer.class.equals(parameterType) || int.class.equals(parameterType);

        if (Context.class.equals(parameterType)) {
            return context -> context;
        }

        if (parameter.isAnnotationPresent(Param.class)) {
            if (String.class.equals(parameterType)) {
                return context -> context.param(parameter.getAnnotation(Param.class).value());
            }
            if (isInteger) {
                return context -> context.paramInt(parameter.getAnnotation(Param.class).value(), 0);
            }
            throw new IllegalArgumentException("Unsupported parameter type: " + parameterType);
        }
        if (parameter.isAnnotationPresent(Query.class)) {
            final Query query = parameter.getAnnotation(Query.class);
            if (String.class.equals(parameterType)) {
                if (query.fallback() != null && !query.fallback().isEmpty()) {
                    return context -> context.query(query.value(), query.fallback());
                }
                return context -> context.query(query.value());
            }
            if (isInteger) {
                if (query.fallback() != null && !query.fallback().isEmpty()) {
                    return context -> context.queryInt(query.value(), Integer.parseInt(query.fallback()));
                }
                return context -> context.queryInt(query.value(), 0);
            }
            throw new IllegalArgumentException("Unsupported query type: " + parameterType);
        }
        throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getType());
    }

    /**
     * Executes the method associated with this transformer using the provided instance and context.
     *
     * @param instance The class instance on which to invoke the method.
     * @param context  The current request context.
     * @return A Response generated by the method invocation.
     * @throws InvocationTargetException If the method throws an exception.
     * @throws IllegalAccessException    If the method access is illegal or inappropriate.
     */
    public Response run(final Object instance, final Context context) throws InvocationTargetException, IllegalAccessException {
        final List<Object> parameters = new ArrayList<>();
        for (final ContextTransformer transformer : this.parameterTransformers) {
            parameters.add(transformer.transform(context));
        }

        final Object methodReturnValue = this.method.invoke(instance, parameters.toArray());
        if (methodReturnValue instanceof final ResponseLike responseLike) {
            return responseLike.extractResponse();
        }

        return new ResponseBuilder()
                .all(this.applyReturnProcessing(methodReturnValue))
                .all(this.steps)
                .build();
    }

    /**
     * Creates a response builder step based on the method's return value.
     *
     * @param methodReturnValue The value returned by the method invocation.
     * @return A consumer that modifies a response builder according to the return type.
     */
    private Consumer<ResponseBuilder> applyReturnProcessing(final Object methodReturnValue) {
        final Class<?> returnType = this.method.getReturnType();
        if (String.class.equals(returnType)) {
            return builder -> builder.text((String) methodReturnValue);
        } else if (byte[].class.equals(returnType)) {
            return builder -> builder.body((byte[]) methodReturnValue);
        } else if (!void.class.equals(returnType) && !Void.class.equals(returnType)) {
            return builder -> builder.json(methodReturnValue);
        }
        return null;
    }
}
