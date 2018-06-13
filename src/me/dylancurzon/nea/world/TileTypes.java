package me.dylancurzon.nea.world;

import com.sun.istack.internal.NotNull;
import java.util.Optional;

public interface TileTypes {

    TileType UNLOADED = new TileType(0, "UNLOADED");

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
