package me.dylancurzon.nea.gfx;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;

@Immutable
public class SpriteSheet {

    private final int[] pixels;
    private final int width;
    private final int height;

    public SpriteSheet(final int[] pixels, final int width, final int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public static SpriteSheet loadSheet(final String resourceName) {
        final InputStream stream = SpriteSheet.class.getClassLoader().getResourceAsStream(resourceName);
        final BufferedImage image;
        try {
            image = ImageIO.read(stream);
        } catch (final IOException|IllegalArgumentException e) {
            throw new RuntimeException("Failed to load SpriteSheet: " + resourceName, e);
        }
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        return new SpriteSheet(pixels, width, height);
    }

    public Sprite getSprite(final int x, final int y) {
        final int[] spritePixels = new int[Sprite.SPRITE_WIDTH * Sprite.SPRITE_WIDTH];
        for (int xd = 0; xd < Sprite.SPRITE_WIDTH; xd++) {
            for (int yd = 0; yd < Sprite.SPRITE_WIDTH; yd++) {
                spritePixels[xd + yd * Sprite.SPRITE_WIDTH] =
                    this.pixels[(x * Sprite.SPRITE_WIDTH) + xd + (y * Sprite.SPRITE_WIDTH  + yd)* this.width];
            }
        }
        return new Sprite(spritePixels);
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

}
