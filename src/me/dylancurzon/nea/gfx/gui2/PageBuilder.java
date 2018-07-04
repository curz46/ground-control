package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * - GUIs
 * > dynamic elements (e.g. graphs based on in-game data)
 * > proper layout
 * > clickable elements
 * > pagination
 * > scrollable
 *
 * ===============================================
 * -----------------------------------------------
 * |                Some Header                  | // margined element; centered
 * ----------------------------------------------- // and uses entire width
 * //...
 * Content                                         // by default, renders directly
 * Content                                         // below, requires bounds
 * //...
 * -------------------------
 * -------------------------
 * ||Some Margined Content||                       // margined; uncentered, minimal
 * -------------------------                       // width
 * -------------------------
 * //...
 * ----------------------------------------------- // inline container
 * | Content ------------------ Content          | // provides padding and takes
 * |         |Margined Element|                  | // into account margin of elements
 * |         ------------------                  | // will be expanded to account
 * ----------------------------------------------- // for contained element bounds
 */
public class PageBuilder {

    @NotNull
    private final List<Element> elements = new ArrayList<>();

    @NotNull
    public PageBuilder addElement(final Element element) {
        this.elements.add(element);
        return this;
    }

    @NotNull
    public Page build() {
        return null;
    }

}
