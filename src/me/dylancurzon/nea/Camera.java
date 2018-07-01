package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Benchmark;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.entity.ComputerCapsule;
import me.dylancurzon.nea.world.entity.Worker;
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
    private final Worker worker;

    public Camera(final Vector2i size, final World world) {
        this.size = size;
        this.world = world;
        this.computer = new ComputerCapsule(world, Vector2i.of(0, 0));
        this.worker = new Worker(world, Vector2i.of(3, 3));
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
        // reset pixels
        for (int i = 0; i < window.getWidth() * window.getHeight(); i++) {
            window.getPixels()[i] = 0;
        }

        // render tiles
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

        // render entities
        final Vector2i pos1 = this.computer.getPosition();
        this.computer.render(
            window,
            pos1.getX() - (int) (this.boundA.getX() * Tile.TILE_WIDTH),
             window.getHeight() - 1 - (pos1.getY() - (int) (this.boundA.getY() * Tile.TILE_WIDTH))
        );
        final Vector2i pos2 = this.worker.getPosition();
        this.worker.render(
            window,
            (int) ((pos2.getX() - this.boundA.getX()) * Tile.TILE_WIDTH),
            window.getHeight() - 1 - ((int) ((pos2.getY() - this.boundA.getY()) * Tile.TILE_WIDTH))
        );

        // render GUI
//        final int minX = pos1.getX() * Tile.TILE_WIDTH - 3;
//        final int minY = pos1.getY() * Tile.TILE_WIDTH - 3;
//        final int maxX = (minX + 3) + (2 * Tile.TILE_WIDTH) + 3;
//        final int maxY = (minY + 3) + (2 * Tile.TILE_WIDTH) + 3;
//        for (int x = minX; x <= maxX; x++) {
//            for (int y = minY; y <= maxY; y++) {
//                if (x != minX && x != maxX && y != minY && y != maxY) continue;
//                window.setPixel(
//                    x - (int) (this.boundA.getX() * Tile.TILE_WIDTH),
//                    window.getHeight() - 1 - (y - (int) (this.boundA.getY() * Tile.TILE_WIDTH)),
//                    0xDDAAAA
//                );
//            }
//        }
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
