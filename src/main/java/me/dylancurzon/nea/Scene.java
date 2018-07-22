package me.dylancurzon.nea;

import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.Tickable;

public interface Scene extends Tickable, Renderable {

    void setMousePosition(final Vector2i pos);

    void click(final Vector2i pos);

    void scroll(final double amount);

}
