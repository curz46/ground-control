package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;
import me.dylancurzon.nea.gfx.page.elements.mutable.TextMutableElement;
import me.dylancurzon.nea.gfx.text.TextType.TextSprite;

import java.util.function.Consumer;

@Immutable
public class TextImmutableElement extends ImmutableElement {

    private final TextSprite sprite;

    protected TextImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer,
                                   final TextSprite sprite) {
        super(margin, tickConsumer);
        this.sprite = sprite;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        return new TextMutableElement(super.margin, this);
    }

    @NotNull
    public TextSprite getSprite() {
        return this.sprite;
    }

    public static class Builder extends ImmutableElement.Builder<TextImmutableElement, Builder> {

        private TextSprite sprite;

        @NotNull
        public Builder setText(final TextSprite sprite) {
            this.sprite = sprite;
            return this;
        }

        @Override
        public Builder self() {
            return this;
        }

        @Override
        @NotNull
        public TextImmutableElement build() {
            return new TextImmutableElement(super.margin, super.tickConsumer, this.sprite);
        }

    }

}
