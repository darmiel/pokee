package com.github.pokee.stick.util;

public abstract class ProxyWrapper<T> {

    private T proxy;

    public abstract T createProxy();

    protected T get() {
        if (proxy == null) {
            proxy = createProxy();
        }
        return proxy;
    }

}
