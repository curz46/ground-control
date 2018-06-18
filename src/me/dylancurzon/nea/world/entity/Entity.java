package me.dylancurzon.nea.world.entity;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;

public abstract class Entity implements Renderable {

    // TODO: determine whether or not this should be final
    @NotNull
    protected final World world;
    // TODO: may want to make this a double in future, not sure
    @NotNull
    protected Vector2i position;

    public Entity(@NotNull final World world, @NotNull final Vector2i position) {
        if (world == null) {
            throw new RuntimeException("world == null");
        }
        if (position == null) {
            throw new RuntimeException("position == null");
        }
        this.world = world;
        this.position = position;
    }

    public void setPosition(@NotNull final Vector2i position) {
        if (position == null) {
            throw new RuntimeException("position == null");
        }
        this.position = position;
    }

    @NotNull
    public World getWorld() {
        return this.world;
    }

    @NotNull
    public Vector2i getPosition() {
        return this.position;
    }

}
