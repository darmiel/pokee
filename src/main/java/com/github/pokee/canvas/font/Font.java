package com.github.pokee.canvas.font;

import org.jetbrains.annotations.Nullable;

public interface Font {

    boolean[] @Nullable [] getCharacterMap(final String text);

    default int getWidth(final String text) {
        final boolean[][] characterMap = this.getCharacterMap(text);
        if (characterMap == null) {
            return 0;
        }
        return characterMap.length;
    }

}
