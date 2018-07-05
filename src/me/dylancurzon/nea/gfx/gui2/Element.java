package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.OffsetRenderable;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public abstract class Element implements OffsetRenderable {

    @NotNull
    public abstract Vector2i getSize();

}
