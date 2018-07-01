package me.dylancurzon.nea;

/**
 * This {@link Window} class is just a wrapper for the pixel array, with additional metadata for (x, y) -> index
 * calculations. It allows resizing the JFrame while the Game is running without needlessly polluting static.
 */
public class Window {

    private final int[] pixels;
    private int width;
    private int height;

    public Window(final int[] pixels, final int width, final int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public void copyPixels(final int x, final int y, final int width, final int[] pixels) {
        final int height = pixels.length / width;
        for (int xd = 0; xd < width; xd++) {
            for (int yd = 0; yd < height; yd++) {
                final int xa = x + xd;
                final int ya = y + yd;
                if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) continue;
                final int argb = pixels[xd + yd * width];
                if (argb >> 24 == 0) continue;
                this.pixels[xa + ya * this.width] = argb;
                // TODO: the below logic uses linear colour mixing to approximate transparency
                // currently unnecessary, hits performance hard
//                final int i = xa + ya * this.width;
//                final int oldValue = this.pixels[i] & 0x00FFFFFF;
//                final int spriteValue = pixels[xd + yd * width];
//                final int rgb = spriteValue & 0x00FFFFFF;
//                final double alpha = (spriteValue >>> 24) / 0xFF;
//
//                final int rOld = oldValue & 255;
//                final int gOld = (oldValue >> 8) & 255;
//                final int bOld = (oldValue >> 16) & 255;
//                final int rSprite = rgb & 255;
//                final int gSprite = (rgb >> 8) & 255;
//                final int bSprite = (rgb >> 16) & 255;
//
//                final int r = (int) (rSprite * alpha + rOld * (1 - alpha));
//                final int g = (int) (gSprite * alpha + gOld * (1 - alpha));
//                final int b = (int) (bSprite * alpha + bOld * (1 - alpha));
//                final int newValue = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
//
//                this.pixels[i] = newValue;
            }
        }
    }

    public void setPixel(final int x, final int y, final int value) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) return;
        this.pixels[x + y * this.width] = value;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public void setHeight(final int height) {
        this.height = height;
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
