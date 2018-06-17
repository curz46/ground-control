package me.dylancurzon.nea.gfx;

import me.dylancurzon.nea.Window;

public class Sprite implements Renderable {

    public static final int SPRITE_WIDTH = 16;
    private final int[] content;

    public Sprite(final int[] content) {
        this.content = content;
    }

    public void render(final Window window, final int offsetX, final int offsetY) {
        window.copyPixels(offsetX, offsetY, SPRITE_WIDTH, this.content);
    }

}
