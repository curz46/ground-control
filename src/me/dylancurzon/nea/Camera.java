package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.Tile;
import me.dylancurzon.nea.world.World;

/**
 * This {@link Camera} class represents the view of the world that is drawn by the game's window.
 * {@link Camera#boundA} determines the position. This is because a centered-position enforces
 * restrictions on the possible window sizes - a size where either dimension is not divisible by
 * two would cause rendering inconsistent with the actual window size, given that in the current
 * implementation of the game equates one pixel in the window with one pixel, and positional unit,
 * in the game's world. Note that a Camera is attached to a {@link World}, and this insatnce should
 * be destroyed and re-created when unloading the game world or loading another one.
 */
public class Camera implements Renderable  {

    // Given the current implementation, this should just be the size of the window.
    @NotNull
    private final Vector2i size;
    // This Camera object should be re-created when changing the World.
    @NotNull
    private final World world;
    // The upper-left bound of the Camera. This determines the position of the world view.
    @NotNull
    private Vector2i boundA;

    public Camera(final Vector2i size, final World world) {
        this.size = size;
        this.world = world;
    }

    @Override
    public void render(@NotNull final int[] pixels) {
        // TODO:
        // - render world tiles which are currently visible
        final Vector2i tileMin = this.boundA
            .div(Tile.TILE_WIDTH)
            .floor().toInt();
        final Vector2i tileMax = this.getBoundB()
            .div(Tile.TILE_WIDTH)
            .ceil().toInt();
        for (int tileX = tileMin.getX(); tileX < tileMax.getX(); tileX++) {
            for (int tileY = tileMin.getY(); tileY < tileMax.getY(); tileY++) {
                // World.getTile(tileX, tileY).render(pixels);
            }
        }
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
