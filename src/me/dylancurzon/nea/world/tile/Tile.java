package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.Game;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.world.World;

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
    public void render(final int[] pixels, final int offsetX, final int offsetY) {
        switch (this.type.getId()) {
            case 0:
                for (int x = 0; x < TILE_WIDTH; x++) {
                    for (int y = 0; y < TILE_WIDTH; y++) {
                        final int xp = x + offsetX;
                        final int yp = y + offsetY;
                        // TODO: this is God awful, think of a better solution
                        final int i = xp + (yp * Game.WIDTH);
                        if (i >= pixels.length) continue;
                        pixels[i] = i % 3 == 0 ? 0xA020F0 : 0xFFFFFF;
                    }
                }
                break;
            case 1:
                for (int x = 0; x < TILE_WIDTH; x++) {
                    for (int y = 0; y < TILE_WIDTH; y++) {
                        final int xp = x + offsetX;
                        final int yp = y + offsetY;
                        final int i = xp + (yp * Game.WIDTH);
                        if (i < 0 || i >= pixels.length) continue;
                        pixels[i] = i % 2 == 0 ? 0xEEEEEE : 0;
                    }
                }
                break;
            case 2:
                for (int x = 0; x < TILE_WIDTH; x++) {
                    for (int y = 0; y < TILE_WIDTH; y++) {
                        final int xp = x + offsetX;
                        final int yp = y + offsetY;
                        final int i = xp + (yp * Game.WIDTH);
                        if (i < 0 || i >= pixels.length) continue;
                        pixels[i] = 0xFF0000;
                    }
                }
        }
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
