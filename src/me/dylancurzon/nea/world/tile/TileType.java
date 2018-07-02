package me.dylancurzon.nea.world.tile;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.Sprite;
import me.dylancurzon.nea.gfx.StaticSprite;

@Immutable
public class TileType {

    @NotNull
    private final int id;
    @NotNull
    private final String displayName;
    @NotNull
    private final Sprite sprite;

    public TileType(@NotNull final int id, @NotNull final String displayName, @NotNull final Sprite sprite) {
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
    public Sprite getSprite() {
        return this.sprite;
    }

}
