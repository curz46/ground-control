package me.dylancurzon.nea.gfx.page;

import com.sun.istack.internal.NotNull;
import java.util.List;
import me.dylancurzon.nea.gfx.page.elements.ImmutableContainer;
import me.dylancurzon.nea.gfx.page.elements.ImmutableElement;
import me.dylancurzon.nea.gfx.page.elements.MutableElement;
import me.dylancurzon.nea.gfx.sprite.Sprite;
import me.dylancurzon.nea.util.Vector2i;

public class PageTemplate extends ImmutableContainer {

    private final Sprite backgroundSprite;
    private final Vector2i position;

    protected PageTemplate(final Spacing margin, final List<ImmutableElement> elements,
                           final Vector2i size, final Spacing padding, final boolean inline, final boolean centering,
                           final Sprite backgroundSprite, final Vector2i position) {
        super(margin, elements, size, padding, centering, inline);
        this.backgroundSprite = backgroundSprite;
        this.position = position;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    @NotNull
    public Page asMutable() {
        final MutableElement container = super.asMutable();
        return new Page(this, container);
    }

    @NotNull
    public Sprite getBackgroundSprite() {
        return this.backgroundSprite;
    }

    @NotNull
    public Vector2i getPosition() {
        return this.position;
    }

    public static class Builder extends ImmutableContainer.Builder<Builder> {

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
        public Builder self() {
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
                super.inline,
                super.centering,
                this.backgroundSprite,
                this.position
            );
        }

    }

}
