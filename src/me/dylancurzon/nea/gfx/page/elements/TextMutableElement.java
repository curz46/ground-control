package me.dylancurzon.nea.gfx.page.elements;

import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.text.TextType;
import me.dylancurzon.nea.util.Vector2i;

import java.util.function.Consumer;

public class TextMutableElement extends MutableElement {

    private final TextImmutableElement immutableElement;
    private TextType.TextSprite sprite;

    protected TextMutableElement(final Spacing margin, final TextImmutableElement immutableElement) {
        super(margin);
        this.immutableElement = immutableElement;
        this.sprite = this.immutableElement.getSprite();
    }

    public void setSprite(final TextType.TextSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public Vector2i getSize() {
        return this.sprite.getSize();
    }

    @Override
    public void tick() {
        final Consumer<MutableElement> consumer = this.immutableElement.getTickConsumer();
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    @Override
    public void render(final PixelContainer container) {
        this.sprite.render(container, 0, 0);
    }

}
