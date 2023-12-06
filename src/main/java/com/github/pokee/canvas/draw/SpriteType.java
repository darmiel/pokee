package com.github.pokee.canvas.draw;

public enum SpriteType {

    BACK("back"),
    FRONT("front");

    private final String dirName;
    SpriteType(final String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }
}
