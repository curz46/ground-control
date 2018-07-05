package me.dylancurzon.nea.gfx;

public interface Renderable extends OffsetRenderable {

//    void render(final PixelContainer window, final int offsetX, final int offsetY);

    default void render(final PixelContainer container, final int offsetX, final int offsetY) {
        this.render(container);
    }

    void render(final PixelContainer container);

}
