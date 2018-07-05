package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import java.util.Arrays;
import java.util.List;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Vector2i;

public class InlineContainer extends MarginableElement {

    @NotNull
    private final List<Element> elements;

    private InlineContainer(@NotNull final List<Element> elements) {
        this.elements = elements;
    }

    public static InlineContainer of(@NotNull final Element... elements) {
        return new InlineContainer(Arrays.asList(elements));
    }

    @Override
    public Vector2i getSize() {
        int offsetX = 0;
        int offsetY = 0;
        for (final Element element : this.elements) {
            final Vector2i size = element.getSize();
            offsetX += size.getX();
            if (size.getY() > offsetY) {
                offsetY = size.getY();
            }
        }
        return Vector2i.of(offsetX, offsetY);
    }

    @Override
    public void render(@NotNull final PixelContainer window, int offsetX, final int offsetY) {
        for (final Element element : this.elements) {
            element.render(window, offsetX, offsetY);

            final Vector2i size = element.getSize();
            offsetX += size.getX();
        }
    }

}
