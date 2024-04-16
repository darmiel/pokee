package com.github.pokee.stick.response;

import com.github.pokee.pson.Pson;
import com.github.pokee.stick.util.ProxyWrapper;

public class ResponsePsonProxy extends ProxyWrapper<Pson> {

    @Override
    public Pson createProxy() {
        return Pson.create()
                .prettyPrint()
                .expandFunctions(false)
                .build();
    }

    public String marshal(final Object object) {
        return this.get().marshal(object);
    }

}
