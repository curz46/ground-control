package me.dylancurzon.nea.gfx.sprite;

import com.sun.istack.internal.NotNull;
import java.awt.image.BufferedImage;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.ImageUtil;

@Immutable
public class StaticSprite implements Sprite {

//    public static final int SPRITE_WIDTH = 16;
    private final int[] content;
    private final int width;
    private final int height;

    public StaticSprite(@NotNull final int[] content, final int width, final int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    @NotNull
    public static StaticSprite loadSprite(final String resourceName) {
        final BufferedImage image = ImageUtil.loadResource(resourceName);
        final int width = image.getWidth();
        final int height = image.getHeight();

        final int[] data = image.getRGB(0, 0, width, height, null, 0, width);
        return new StaticSprite(data, width, height);
    }

    public void render(@NotNull final PixelContainer window, final int offsetX, final int offsetY) {
        window.copyPixels(offsetX, offsetY, this.width, this.content);
    }

    public int[] getPixels() {
        return this.content;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

}
