package me.dylancurzon.nea.gfx.page.elements.container;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Pos;
import me.dylancurzon.nea.gfx.page.InteractOptions;
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
    boolean isCentering();

    @NotNull
    Positioning getPositioning();

    @NotNull
    boolean isScrollable();

    @NotNull
    InteractOptions getInteractOptions();

}
