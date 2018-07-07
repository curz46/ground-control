package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite.TickContainer;
import me.dylancurzon.nea.gfx.sprite.Sprite;
import me.dylancurzon.nea.gfx.sprite.StaticSprite;
import me.dylancurzon.nea.util.Vector2i;

import java.util.function.Consumer;

@Immutable
public abstract class SpriteImmutableElement extends ImmutableElement {

    public SpriteImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer) {
        super(margin, tickConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class StaticSpriteImmutableElement extends SpriteImmutableElement {

        private final StaticSprite sprite;

        public StaticSpriteImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer,
                                            final StaticSprite sprite) {
            super(margin, tickConsumer);
            this.sprite = sprite;
        }

        @Override
        public MutableElement asMutable() {
            return new MutableElement(super.margin) {
                @Override
                public Vector2i getSize() {
                    final Sprite sprite = StaticSpriteImmutableElement.this.sprite;
                    return Vector2i.of(
                        sprite.getWidth(),
                        sprite.getHeight()
                    );
                }

                @Override
                public void tick() {
                    final Consumer<MutableElement> consumer = StaticSpriteImmutableElement.super.getTickConsumer();
                    if (consumer != null) {
                        consumer.accept(this);
                    }
                }

                @Override
                public void render(final PixelContainer pixelContainer) {
                    final Sprite sprite = StaticSpriteImmutableElement.this.sprite;
                    sprite.render(pixelContainer, 0, 0);
                }
            };
        }

    }

    public static class AnimatedSpriteImmutableElement extends SpriteImmutableElement {

        private final AnimatedSprite sprite;

        public AnimatedSpriteImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer,
                                              final AnimatedSprite sprite) {
            super(margin, tickConsumer);
            this.sprite = sprite;
        }

        @Override
        public MutableElement asMutable() {
            final TickContainer container = this.sprite.createContainer();
            return new MutableElement(super.margin) {
                @Override
                public void tick() {
                    final Consumer<MutableElement> consumer = AnimatedSpriteImmutableElement.super.getTickConsumer();
                    if (consumer != null) {
                        consumer.accept(this);
                    }
                    container.tick();
                }

                @Override
                public Vector2i getSize() {
                    return Vector2i.of(
                        container.getWidth(),
                        container.getHeight()
                    );
                }

                @Override
                public void render(final PixelContainer pixelContainer) {
                    container.render(pixelContainer, 0, 0);
                }
            };
        }

    }

    public static class Builder extends ImmutableElement.Builder<SpriteImmutableElement, Builder> {

        protected StaticSprite sprite;
        protected AnimatedSprite animatedSprite;

        @NotNull
        public Builder setSprite(final StaticSprite sprite) {
            if (this.animatedSprite != null) {
                throw new RuntimeException(
                    "SpriteImmutableElement.Builder: invalid usage, Sprite and AnimatedSprite "
                    + "should not both be set."
                );
            }
            this.sprite = sprite;
            return this;
        }

        @NotNull
        public Builder setAnimatedSprite(final AnimatedSprite animatedSprite) {
            if (this.sprite != null) {
                throw new RuntimeException(
                    "SpriteImmutableElement.Builder: invalid usage, Sprite and AnimatedSprite "
                        + "should not both be set."
                );
            }
            this.animatedSprite = animatedSprite;
            return this;
        }

        @Override
        public Builder self() {
            return this;
        }

        @Override
        @NotNull
        public SpriteImmutableElement build() {
            if (this.animatedSprite != null) {
                return new AnimatedSpriteImmutableElement(super.margin, super.tickConsumer, this.animatedSprite);
            }
            if (this.sprite == null) {
                throw new RuntimeException(
                    "SpriteImmutableElement.Builder requires a Sprite or AnimatedSprite to build!"
                );
            }
            return new StaticSpriteImmutableElement(super.margin, super.tickConsumer, this.sprite);
        }

    }

}
