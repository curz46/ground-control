package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

public abstract class MutableElement implements Renderable {

    @NotNull
    protected final Spacing margin;

    protected MutableElement(@NotNull final Spacing margin) {
        this.margin = margin;
    }

    @NotNull
    public Spacing getMargin() {
        return this.margin;
    }

    @NotNull
    public Vector2i getMarginedSize() {
        return this.getSize().add(
            Vector2i.of(
                this.margin.getLeft() + this.margin.getRight(),
                this.margin.getBottom() + this.margin.getTop()
            )
        );
    }

    @NotNull
    public abstract Vector2i getSize();

    public void tick() {}

    public abstract void render(@NotNull final PixelContainer container);

}
