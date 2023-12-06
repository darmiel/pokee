package com.github.pokee.canvas.font;

import org.jetbrains.annotations.Nullable;

public interface Font {

    boolean[] @Nullable [] getCharacterMap(final String text);

    int getWidth(final String text);

}
