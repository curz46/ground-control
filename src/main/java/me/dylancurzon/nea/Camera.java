package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.terrain.Terrain;
import me.dylancurzon.nea.util.Benchmark;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.World;
import me.dylancurzon.nea.terrain.world.tile.Tile;

import java.util.Optional;

/**
 * This {@link Camera} class represents the view of the world that is drawn by the game's window.
 * {@link Camera#boundA} determines the position. This is because a centered-position enforces
 * restrictions on the possible window sizes - a size where either dimension is not divisible by
 * two would cause rendering inconsistent with the actual window size, given that in the current
 * implementation of the game equates one pixel in the window with one pixel, and positional unit,
 * in the game's world. Note that a Camera is attached to a {@link World}, and this instance should
 * be destroyed and re-created when unloading the game world or loading another one.
 */
public class Camera implements Renderable {

    // Given the current implementation, this should just be the size of the window.
    @NotNull
    private final Vector2i size;
    // This Camera object should be re-created when changing the World.
    @NotNull
    private final Terrain terrain;
    // The upper-left bound of the Camera. This determines the position of the world view.
    @NotNull
    private Vector2d boundA = Vector2d.of(0, 0);

    public Camera(final Vector2i size, final Terrain terrain) {
        this.size = size;
        this.terrain = terrain;
    }

    @Override
    public void render(@NotNull final PixelContainer window) {
        // render tiles
        final Vector2i pixelA = this.boundA.mul(Tile.TILE_WIDTH).toInt();
        final Vector2d boundB = this.getBoundB();
        for (int tileX = (int) this.boundA.getX() - 1; tileX < boundB.getX(); tileX++) {
            for (int tileY = (int) this.boundA.getY(); tileY < boundB.getY() + 1; tileY++) {
                final Optional<Tile> tile = this.terrain.getTile(Vector2i.of(tileX, tileY));
                final int xa = tileX * Tile.TILE_WIDTH - pixelA.getX();
                final int ya = tileY * Tile.TILE_WIDTH - pixelA.getY();

                // if no Tile is present, render an alternating shade of grey
                if (!tile.isPresent()) {
                    for (int dx = 0; dx < Tile.TILE_WIDTH; dx++) {
                        for (int dy = 0; dy < Tile.TILE_WIDTH; dy++) {
                            window.setPixel(xa + dx, window.getHeight() - 1 - ya + dy, (tileX + tileY) % 2 == 0 ? 0xFFE7E7E7 : 0xFFEEEEEE);
                        }
                    }
                    continue;
                }

                tile.get().render(window, xa, window.getHeight() - 1 - ya);
            }
        }
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

    public Vector2d getTilePosition(@NotNull final PixelContainer window, @NotNull final Vector2i screenPosition) {
        final Vector2i pixelPos = screenPosition
            .sub(Vector2i.of(0, window.getHeight() - 2))
            .mul(Vector2i.of(1, -1));
        return this.boundA.add(pixelPos.div(Tile.TILE_WIDTH));
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
