package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import java.util.List;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.sprite.Sprite;
import me.dylancurzon.nea.util.Vector2i;

public class PageTemplate extends ImmutableContainer {

    private final Sprite backgroundSprite;
    private final Vector2i position;

    protected PageTemplate(final Spacing margin, final List<ImmutableElement> elements,
        final Vector2i size, final Spacing padding, final boolean centering, final Sprite backgroundSprite,
        final Vector2i position) {
        super(margin, elements, size, padding, centering);
        this.backgroundSprite = backgroundSprite;
        this.position = position;
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        final MutableElement container = super.asMutable();
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
                return PageTemplate.this.size;
            }

            @Override
            public void render(final PixelContainer window) {
                final Vector2i pos = PageTemplate.this.position;
                final Vector2i size = PageTemplate.super.size;
                PageTemplate.this.backgroundSprite.render(
                    window,
                    PageTemplate.this.position.getX(),
                    PageTemplate.this.position.getY()
                );
                final PixelContainer pixelContainer = new PixelContainer(
                    new int[size.getX() * size.getY()],
                    size.getX(),
                    size.getY()
                );
                container.render(pixelContainer);
                window.copyPixels(
                    pos.getX(),
                    pos.getY(),
                    size.getX(),
                    pixelContainer.getPixels()
                );
            }
        };
    }

    public static class Builder extends ImmutableContainer.Builder {

        private Sprite backgroundSprite;
        private Vector2i position;

        @NotNull
        public Builder setBackground(final Sprite backgroundSprite) {
            this.backgroundSprite = backgroundSprite;
            return this;
        }

        @NotNull
        public Builder setPosition(final Vector2i position) {
            this.position = position;
            return this;
        }

        @Override
        @NotNull
        public PageTemplate build() {
            if (super.centering && super.elements.size() > 1) {
                throw new RuntimeException(
                    "A centering PageTemplate may only contain a single ImmutableElement!"
                );
            }
            if (super.elements.size() == 0) {
                throw new RuntimeException("Empty PageTemplate is not permitted!");
            }
            if (this.backgroundSprite == null || this.position == null) {
                throw new RuntimeException("BackgroundSprite and Position are required attributes!");
            }

            return new PageTemplate(
                super.margin,
                super.elements,
                Vector2i.of(
                    this.backgroundSprite.getWidth(),
                    this.backgroundSprite.getHeight()
                ),
                super.padding,
                super.centering,
                this.backgroundSprite,
                this.position
            );
        }

    }

}
