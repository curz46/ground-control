package me.dylancurzon.nea.gfx.gui2;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.text.TextType.TextSprite;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class TextElement extends MarginableElement {

    private final TextSprite sprite;

    private TextElement(@NotNull final TextSprite sprite) {
        this.sprite = sprite;
    }

    public static TextElement of(@NotNull final TextSprite sprite) {
        return new TextElement(sprite);
    }

    @Override
    public Vector2i getSize() {
        return this.sprite.getSize();
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
        this.sprite.render(window, offsetX, offsetY);
    }

}
