package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.Renderable;

public class Page implements Renderable {

    @NotNull
    private final List<Element> elements = new ArrayList<>();

    @Override
    public void render(final Window window, final int offsetX, final int offsetY) {

    }

}
