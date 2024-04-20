package com.github.pokee.pswf.util.router;

import com.github.pokee.pswf.router.Context;

public interface ContextTransformer {
    Object transform(final Context context);
}