package me.dylancurzon.nea.gfx;

import java.awt.image.BufferedImage;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.util.ImageUtil;

@Immutable
public class SpriteSheet {

    public static SpriteSheet PRIMARY_SHEET =
        SpriteSheet.loadSheet("spritesheet.png", 16);

    private final int[] pixels;
    private final int width;
    private final int height;
    private final int spriteWidth;

    public SpriteSheet(final int[] pixels, final int width, final int height, final int spriteWidth) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.spriteWidth = spriteWidth;
    }

    public static SpriteSheet loadSheet(final String resourceName, final int spriteWidth) {
        final BufferedImage image = ImageUtil.loadResource(resourceName);
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        return new SpriteSheet(pixels, width, height, spriteWidth);
    }

    public Sprite getSprite(final int x, final int y, final int width, final int height) {
        final int[] spritePixels = new int[width * height];
        for (int xd = 0; xd < width; xd++) {
            for (int yd = 0; yd < height; yd++) {
                spritePixels[xd + yd * width] =
                    this.pixels[(x * this.spriteWidth) + xd + (y * this.spriteWidth + yd) * this.width];
            }
        }
        return new Sprite(spritePixels, width, height);
    }

    public Sprite getSprite(final int x, final int y, final int width) {
        return this.getSprite(x, y, width, width);
    }

    public Sprite getSprite(final int x, final int y) {
        return this.getSprite(x, y, this.spriteWidth, this.spriteWidth);
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
