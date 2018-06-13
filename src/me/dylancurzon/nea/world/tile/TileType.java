package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class TileType {

    @NotNull
    private final int id;
    @NotNull
    private final String displayName;

    public TileType(@NotNull final int id, @NotNull final String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @NotNull
    public int getId() {
        return this.id;
    }

    @NotNull
    public String getDisplayName() {
        return this.displayName;
    }

}
