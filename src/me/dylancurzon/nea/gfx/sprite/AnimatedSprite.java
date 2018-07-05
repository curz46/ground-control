package me.dylancurzon.nea.gfx.sprite;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.ImageUtil;

import java.awt.image.BufferedImage;

@Immutable
public class AnimatedSprite {

    private final int[][] frames;
    private final int width;
    private final int height;
    // A value of 1 here denotes a 1:1 correspondence between ticks and frames.
    private final int ticksPerFrame;

    public AnimatedSprite(@NotNull final int[][] frames, final int width, final int height, final int ticksPerFrame) {
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.ticksPerFrame = ticksPerFrame;
    }

    public static AnimatedSprite loadAnimatedSprite(final String resourceName, final int height, final int ticksPerFrame) {
        final BufferedImage image = ImageUtil.loadResource(resourceName);
        final int width = image.getWidth();
        final int frameCount = image.getHeight() / height;
        if (frameCount != ((double) image.getHeight()) / height) {
            throw new RuntimeException(
                "An animated image could not be loaded: the height does not match the expected frame height."
            );
        }

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("Frame count: " + frameCount);

        final int[] data = image.getRGB(0, 0, width, image.getHeight(), null, 0, width);
        final int[][] frames = new int[frameCount][width * height];

        for (int n = 0; n < frameCount; n++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    frames[n][x + y * width] = data[x + y * width + (n * width * height)];
                }
            }
        }

        return new AnimatedSprite(frames, width, height, ticksPerFrame);
    }

    public TickContainer createContainer() {
        return new TickContainer(this);
    }

    /**
     * @throws IndexOutOfBoundsException if {@link this#frames} {@code >} index
     */
    public int[] getFrame(final int index) {
        return this.frames[index];
    }

    public int getFrameCount() {
        return this.frames.length;
    }

    public int[][] getFrames() {
        return this.frames;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTicksPerFrame() {
        return this.ticksPerFrame;
    }

    public class TickContainer implements Sprite {

        private final AnimatedSprite sprite;
        private int ticks;

        public TickContainer(@NotNull final AnimatedSprite sprite) {
            this.sprite = sprite;
        }

        public void tick() {
            this.ticks++;
        }

        @Override
        public void render(@NotNull final PixelContainer window, final int offsetX, final int offsetY) {
            final int rate = this.sprite.getTicksPerFrame();
            // Calculate effective ticks by dividing by the number of ticks that should occur per frame.
            final int index = (this.ticks / rate) % this.sprite.getFrameCount();
            window.copyPixels(offsetX, offsetY, this.sprite.getWidth(), this.sprite.getFrame(index));
        }

        @Override
        public int getWidth() {
            return this.sprite.getWidth();
        }

        @Override
        public int getHeight() {
            return this.sprite.getHeight();
        }

    }

}
