package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Benchmark;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.entity.ComputerCapsule;
import me.dylancurzon.nea.world.tile.Tile;

/**
 * This {@link Camera} class represents the view of the world that is drawn by the game's window.
 * {@link Camera#boundA} determines the position. This is because a centered-position enforces
 * restrictions on the possible window sizes - a size where either dimension is not divisible by
 * two would cause rendering inconsistent with the actual window size, given that in the current
 * implementation of the game equates one pixel in the window with one pixel, and positional unit,
 * in the game's world. Note that a Camera is attached to a {@link World}, and this insatnce should
 * be destroyed and re-created when unloading the game world or loading another one.
 */
public class Camera implements Renderable {

    private final Benchmark render = new Benchmark("Camera#render_pixels", 20, false);

    // Given the current implementation, this should just be the size of the window.
    @NotNull
    private final Vector2i size;
    // This Camera object should be re-created when changing the World.
    @NotNull
    private final World world;
    // The upper-left bound of the Camera. This determines the position of the world view.
    @NotNull
    private Vector2d boundA = Vector2d.of(0, 0);

    private final ComputerCapsule computer;

    public Camera(final Vector2i size, final World world) {
        this.size = size;
        this.world = world;
        this.computer = new ComputerCapsule(world, Vector2i.of(0, 0));
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
//        final Vector2i tileMin = this.boundA
//            .div(World.CHUNK_WIDTH)
//            .floor()
//            .toInt();
//        final Vector2i tileMax = this.getBoundB()
//            .div(World.CHUNK_WIDTH)
//            .ceil()
//            .toInt();

//        System.out.println(this.boundA);
//        System.out.println(this.getBoundB());
//        System.out.println(tileMax.sub(tileMin));

        // ratio * pixel = texel
//        final double ratioX =
//            ((double) ((tileMax.getX() - tileMin.getY())) * Tile.TILE_WIDTH) / width;
//        final double ratioY =
//            ((double) ((tileMax.getY() - tileMin.getY())) * Tile.TILE_WIDTH) / height;

        // for every pixel, find containing Tile, use NEAREST_NEIGHBOUR min/mag filter
        // TODO: potential optimization; use Tiles per screen and work in blocks to avoid many
        // ...hash map calls

//        this.render.start();
//        for (int px = 0; px < width; px++) {
//            final int texelX = (int) ratioX * (px + this.boundA.getX());
//            final int tileX = (int) (((double) texelX) / Tile.TILE_WIDTH);
//            final int texelXRel = texelX & (Tile.TILE_WIDTH - 1);
////            final int texelX = (int) Math.floor((tileX - Math.floor(tileX)) * Tile.TILE_WIDTH);
//            for (int py = 0; py < height; py++) {
//                final int texelY = (int) ratioY * py + this.boundA.getX() * Tile.TILE_WIDTH;
//                final int tileY = (int) (((double) texelY) / Tile.TILE_WIDTH);
//                final int texelYRel = texelY & (Tile.TILE_WIDTH - 1);
//                final Tile tile = this.world.getTile(Vector2i.of(tileX, tileY));
//                final Sprite sprite = tile.getType().getSprite();
////                final int texelY = (int) Math.floor((tileY - Math.floor(tileY)) * Tile.TILE_WIDTH);
//                final int[] texels = sprite.getPixels();
////                window.setPixel(
////                    px,
////                    window.getHeight() - 1 - py,
////                    texels[texelXRel + texelYRel * Tile.TILE_WIDTH]
////                );
//                pixels[px + (height - 1 - py) * width] =
//                    texels[texelXRel + texelYRel * Tile.TILE_WIDTH];
//            }
//        }
//        this.render.end();

        final Vector2i pixelA = this.boundA.mul(Tile.TILE_WIDTH).toInt();
        final Vector2d boundB = this.getBoundB();
        for (int tileX = (int) this.boundA.getX() - 1; tileX < boundB.getX(); tileX++) {
            for (int tileY = (int) this.boundA.getY(); tileY < boundB.getY() + 1; tileY++) {
                final Tile tile = this.world.getTile(Vector2i.of(tileX, tileY));
                final int xa = tileX * Tile.TILE_WIDTH - pixelA.getX();
                final int ya = tileY * Tile.TILE_WIDTH - pixelA.getY();
                tile.render(window, xa, window.getHeight() - 1 - ya);
            }
        }

        final Vector2i pos = this.computer.getPosition();
        this.computer.render(
            window,
            pos.getX() - (int) (this.boundA.getX() * Tile.TILE_WIDTH),
             window.getHeight() - 1 - (pos.getY() - (int) (this.boundA.getY() * Tile.TILE_WIDTH))
        );
    }

    /**
     * Move this {@link Camera} by the specified transform. This affects {@link #boundA}.
     * @param delta The transform to move by.
     */
    public void transform(final Vector2d delta) {
        this.boundA = this.boundA.add(delta);
    }

    /**
     * Reset this Camera to (0, 0). This affects {@link #boundA}.
     */
    public void reset() {
        this.boundA = Vector2d.of(0, 0);
    }

    @NotNull
    public Vector2d getBoundA() {
        return this.boundA;
    }

    @NotNull
    public Vector2d getBoundB() {
        return this.boundA.add(this.size.toDouble());
    }

}
