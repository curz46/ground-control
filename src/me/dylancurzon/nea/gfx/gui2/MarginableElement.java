package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;

public abstract class MarginableElement extends Element {

    @NotNull
    public MarginedElement margined(final int margin) {
        return this.margined(margin, margin);
    }

    @NotNull
    public MarginedElement margined(final int marginX, final int marginY) {
        return this.margined(marginY, marginX, marginY, marginX);
    }

    @NotNull
    public MarginedElement margined(final int marginTop, final int marginRight,
        final int marginBottom, final int marginLeft) {
        return new MarginedElement(this, marginTop, marginRight, marginBottom, marginLeft);
    }

}
