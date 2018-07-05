package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.text.TextType.TextSprite;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class TextImmutableElement extends ImmutableElement {

    private final TextSprite sprite;

    protected TextImmutableElement(final Spacing margin, final TextSprite sprite) {
        super(margin);
        this.sprite = sprite;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
                return TextImmutableElement.this.sprite.getSize();
            }

            @Override
            public void render(final PixelContainer container) {
                TextImmutableElement.this.sprite.render(container, 0, 0);
            }
        };
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
            return new TextImmutableElement(super.margin, this.sprite);
        }

    }

}
