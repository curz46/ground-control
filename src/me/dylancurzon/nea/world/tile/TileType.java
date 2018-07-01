package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.StaticSprite;

@Immutable
public class TileType {

    @NotNull
    private final int id;
    @NotNull
    private final String displayName;
    @NotNull
    private final StaticSprite sprite;

    public TileType(@NotNull final int id, @NotNull final String displayName, @NotNull final StaticSprite sprite) {
        this.id = id;
        this.displayName = displayName;
        this.sprite = sprite;
    }

    @NotNull
    public int getId() {
        return this.id;
    }

    @NotNull
    public String getDisplayName() {
        return this.displayName;
    }

    @NotNull
    public StaticSprite getSprite() {
        return this.sprite;
    }

}
