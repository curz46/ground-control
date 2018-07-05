package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;
import me.dylancurzon.nea.gfx.OffsetRenderable;
import me.dylancurzon.nea.gfx.PixelContainer;

public class Page implements OffsetRenderable {

    @NotNull
    private final List<Element> elements = new ArrayList<>();

    @Override
    public void render(final PixelContainer window, final int offsetX, final int offsetY) {

    }

}
