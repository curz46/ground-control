package me.dylancurzon.nea.gfx.page.elements.container;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.util.Vector2i;

public interface ImmutableContainer {

    static DefaultImmutableContainer.ContainerBuilder builder() {
        return new DefaultImmutableContainer.ContainerBuilder();
    }

    @NotNull
    Spacing getMargin();

    @NotNull
    Spacing getPadding();

    @NotNull
    Vector2i getSize();

    @NotNull
    Vector2i getMarginedSize();

    @NotNull
    Vector2i getPaddedSize();

    @NotNull
    boolean isScrollable();

}
