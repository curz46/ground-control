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
                this.pixels[xa + ( ya) * this.width] = pixels[xd + yd * width];
            }
        }
    }

    public void setPixel(final int x, final int y, final int value) {
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
