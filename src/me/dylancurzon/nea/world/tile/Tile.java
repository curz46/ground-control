package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.Game;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.Sprite;
import me.dylancurzon.nea.world.World;

public class Tile implements Renderable {

    public static final int TILE_WIDTH = Sprite.SPRITE_WIDTH;

    @NotNull
    private final World world;
    @NotNull
    private TileType type;

    public Tile(final World world, final TileType type) {
        this.world = world;
        this.type = type;
    }

    @Override
    public void render(final Window window, final int offsetX, final int offsetY) {
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
