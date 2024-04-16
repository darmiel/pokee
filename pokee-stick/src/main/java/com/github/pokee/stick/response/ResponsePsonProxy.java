package com.github.pokee.stick.response;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.mappers.UUIDMapper;

import java.util.UUID;

public class ResponsePsonProxy {

    private static Pson proxyInstance = null;

    public static Pson get() {
        if (ResponsePsonProxy.proxyInstance == null) {
            ResponsePsonProxy.proxyInstance = Pson.create()
                    .prettyPrint()
                    .expandFunctions(false)
                    .registerMapper(UUID.class, UUIDMapper.INSTANCE)
                    .build();
        }
        return ResponsePsonProxy.proxyInstance;
    }

    public String marshal(final Object object) {
        return ResponsePsonProxy.get().marshal(object);
    }

}
