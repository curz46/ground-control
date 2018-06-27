package me.dylancurzon.nea.gfx;

import me.dylancurzon.nea.Window;

public class Sprite implements Renderable {

//    public static final int SPRITE_WIDTH = 16;
    private final int[] content;
    private final int width;
    private final int height;

    public Sprite(final int[] content, final int width, final int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    public void render(final Window window, final int offsetX, final int offsetY) {
        window.copyPixels(offsetX, offsetY, this.width, this.content);
    }

    public int[] getPixels() {
        return this.content;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

}
