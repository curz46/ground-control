package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Optional;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Benchmark;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.entity.ComputerCapsule;
import me.dylancurzon.nea.world.tile.Tile;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.tile.TileTypes;

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

    // Given the current implementation, this should just be the size of the window.
    @NotNull
    private final Vector2i size;
    // This Camera object should be re-created when changing the World.
    @NotNull
    private final World world;
    // The upper-left bound of the Camera. This determines the position of the world view.
    @NotNull
    private Vector2i boundA = Vector2i.of(0, 0);

    private final ComputerCapsule computer;

    public Camera(final Vector2i size, final World world) {
        this.size = size;
        this.world = world;
        this.computer = new ComputerCapsule(world, Vector2i.of(0, 0));
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
        final Vector2i tileMin = this.boundA
            .div(World.CHUNK_WIDTH)
            .floor()
            .toInt();
        final Vector2i tileMax = this.getBoundB()
            .div(World.CHUNK_WIDTH)
            .ceil()
            .toInt();

        for (int tileX = tileMin.getX(); tileX < tileMax.getX(); tileX++) {
            for (int tileY = tileMin.getY(); tileY < tileMax.getY(); tileY++) {
                final Tile tile = this.world.getTile(Vector2i.of(tileX, tileY));
                final int xa = (tileX * Tile.TILE_WIDTH) - this.boundA.getX();
                final int ya = window.getHeight() - ((tileY * Tile.TILE_WIDTH) - this.boundA.getY());
                tile.render(window, xa, ya);
            }
        }

        final Vector2i pos = this.computer.getPosition();
        this.computer.render(
            window,
            pos.getX() - this.boundA.getX(),
            window.getHeight() - (pos.getY() - this.boundA.getY())
        );
    }

    /**
     * Move this {@link Camera} by the specified transform. This affects {@link #boundA}.
     * @param delta The transform to move by.
     */
    public void transform(final Vector2i delta) {
        this.boundA = this.boundA.add(delta);
    }

    /**
     * Reset this Camera to (0, 0). This affects {@link #boundA}.
     */
    public void reset() {
        this.boundA = Vector2i.of(0, 0);
    }

    @NotNull
    public Vector2i getBoundA() {
        return this.boundA;
    }

    @NotNull
    public Vector2i getBoundB() {
        return this.boundA.add(this.size);
    }

}
