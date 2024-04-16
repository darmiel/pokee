package com.github.pokee.canvas.util;

import lombok.Getter;

@Getter
public enum SpriteType {

    BACK("back"),
    FRONT("front");

    private final String dirName;
    SpriteType(final String dirName) {
        this.dirName = dirName;
    }

}
