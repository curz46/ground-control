package me.dylancurzon.nea.world;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.Renderable;

public class Tile implements Renderable {

    public static final int TILE_WIDTH = 16;

    @NotNull
    private final World world;
    @NotNull
    private TileType type = TileTypes.UNLOADED;

    public Tile(final World world) {
        this.world = world;
    }

    @Override
    public void render(final int[] pixels) {
        //
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
