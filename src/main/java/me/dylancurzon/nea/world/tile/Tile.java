package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.OffsetRenderable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.world.World;

public class Tile implements OffsetRenderable {

    public static final int TILE_WIDTH = 16;

    @NotNull
    private final World world;
    @NotNull
    private TileType type;

    public Tile(final World world, final TileType type) {
        this.world = world;
        this.type = type;
    }

    @Override
    public void render(final PixelContainer window, final int offsetX, final int offsetY) {
        this.type.getSprite().render(window, offsetX, offsetY);
    }

    public void setType(@NotNull final TileType type) {
        if (type == null) {
            throw new RuntimeException("Cannot set TileType to null on Tile!");
        }
        this.type = type;
    }

    @NotNull
    public World getWorld() {
        return this.world;
    }

    @NotNull
    public TileType getType() {
        return this.type;
    }

}