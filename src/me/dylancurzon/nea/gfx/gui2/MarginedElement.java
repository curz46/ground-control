package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class MarginedElement extends Element {

    @NotNull
    private final Element wrappedElement;
    private final int marginTop;
    private final int marginRight;
    private final int marginBottom;
    private final int marginLeft;

    public MarginedElement(@NotNull final Element element, final int marginTop, final int marginRight,
        final int marginBottom, final int marginLeft) {
        this.wrappedElement = element;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
    }

    @Override
    @NotNull
    public Vector2i getSize() {
        return this.wrappedElement.getSize()
            .add(Vector2i.of(
                this.marginLeft + this.marginRight,
                this.marginBottom + this.marginTop
            ));
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    @Override
    public void render(@NotNull final PixelContainer window, final int offsetX, final int offsetY) {
        this.wrappedElement.render(
            window,
            offsetX + this.marginLeft,
            offsetY + this.marginTop
        );
    }

}
