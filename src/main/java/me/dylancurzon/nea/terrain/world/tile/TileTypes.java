package me.dylancurzon.nea.terrain.world.tile;

import static me.dylancurzon.nea.gfx.sprite.SpriteSheet.PRIMARY_SHEET;

import com.sun.istack.internal.NotNull;
import java.util.Optional;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite;

public interface TileTypes {

    TileType UNLOADED = new TileType(0, "Unloaded", PRIMARY_SHEET.getSprite(0, 0, 16));
    TileType GRASS = new TileType(1, "Grass", PRIMARY_SHEET.getSprite(1, 0, 16));
    TileType STONE = new TileType(2, "Stone", PRIMARY_SHEET.getSprite(2, 0, 16));
    TileType WATER = new TileType(
        3,
        "Water",
        AnimatedSprite
            .loadAnimatedSprite("water.png", 16, 2)
            .createContainer()
    );

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
            UNLOADED,
            GRASS,
            STONE,
            WATER
        };
    }

}
