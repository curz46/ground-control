package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.SpriteSheet;

import java.util.Optional;

public interface TileTypes {

    SpriteSheet SHEET = SpriteSheet.loadSheet("spritesheet.png");
    TileType UNLOADED = new TileType(0, "Unloaded", SHEET.getSprite(0, 0));
    TileType GRASS = new TileType(1, "Grass", SHEET.getSprite(1, 0));
    TileType STONE = new TileType(2, "Stone", SHEET.getSprite(2, 0));

    @NotNull
    static Optional<TileType> getType(@NotNull final int id) {
        final TileType[] types = getTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].getId() == id) return Optional.of(types[i]);
        }
        return Optional.empty();
    }

    @NotNull
    static TileType[] getTypes() {
        return new TileType[] {
            UNLOADED
        };
    }

}
