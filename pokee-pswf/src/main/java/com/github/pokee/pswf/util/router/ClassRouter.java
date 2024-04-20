package com.github.pokee.pswf.util.router;

import com.github.pokee.pswf.annotation.RoutePrefix;
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pswf.annotation.method.POST;
import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.router.Router;

import java.lang.reflect.InvocationTargetException;

/**
 * A router that dynamically registers HTTP route handlers based on method annotations within a specified class.
 */
public class ClassRouter {

    /**
     * Registers all annotated methods from the given handler instance as routes.
     *
     * @param handlerInstance The instance of the class that contains annotated methods to be registered as routes.
     */
    public static void registerRoutes(final Router router, final Object handlerInstance) {
        final RoutePrefix routePrefixAnnotation = handlerInstance.getClass().getAnnotation(RoutePrefix.class);
        final String prefix = routePrefixAnnotation != null ? routePrefixAnnotation.value() : "";

        for (final java.lang.reflect.Method method : handlerInstance.getClass().getDeclaredMethods()) {
            ClassRouter.registerMethodAsRoute(router, method, handlerInstance, prefix);
        }
    }

    /**
     * Registers a single method as a route, if applicable, based on its annotations.
     *
     * @param method          The method to register.
     * @param handlerInstance The instance of the handler class.
     * @param prefix          The route prefix to prepend to the path.
     */
    private static void registerMethodAsRoute(final Router router,
                                              final java.lang.reflect.Method method,
                                              final Object handlerInstance,
                                              final String prefix) {
        final Method requestMethod = ClassRouter.extractRequestMethod(method);
        if (requestMethod == null) return;

        final String path = ClassRouter.extractPath(method, prefix);
        final MethodTransformer transformer = MethodTransformer.fromMethod(method);

        System.out.println("Registering route: " + requestMethod + " " + path + " with transformer: " + transformer);
        router.registerRoute(requestMethod, path, context -> {
            try {
                context.response = transformer.run(handlerInstance, context);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Extracts the HTTP method from the given method's annotations.
     *
     * @param method The method to check.
     * @return The request method if annotated, or null if not.
     */
    private static Method extractRequestMethod(java.lang.reflect.Method method) {
        if (method.isAnnotationPresent(GET.class)) {
            return Method.GET;
        } else if (method.isAnnotationPresent(POST.class)) {
            return Method.POST;
        }
        return null; // Extend with more HTTP methods if necessary
    }

    /**
     * Builds the complete path for the route from the method's annotation and prefix.
     *
     * @param method The method whose path is to be extracted.
     * @param prefix The route prefix to prepend.
     * @return The complete path for the route.
     */
    private static String extractPath(java.lang.reflect.Method method, String prefix) {
        String path = method.isAnnotationPresent(GET.class)
                ? method.getAnnotation(GET.class).value()
                : method.getAnnotation(POST.class).value();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return prefix + path;
    }

}
