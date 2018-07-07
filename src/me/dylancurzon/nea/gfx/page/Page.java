package me.dylancurzon.nea.gfx.page;

import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.animation.Animation;
import me.dylancurzon.nea.gfx.page.elements.container.ImmutableContainer;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableContainer;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.Tickable;

import java.util.Collection;
import java.util.Collections;

public class Page extends MutableContainer implements Tickable {

    private final PageTemplate template;
    private final MutableContainer container;

    private Vector2i position;
    private TransformHandler transform;

    protected Page(final PageTemplate template, final MutableContainer container) {
        super(template.getMargin(), template, Collections.emptyList());
        this.template = template;
        this.container = container;

        this.position = this.template.getPosition();
    }

    @Override
    public void scroll(final double amount) {
        this.container.scroll(amount);
    }

    public void transform(final Vector2i position) {
        this.position = position;
    }

    public void transform(final Vector2i destination, final Animation animation) {
        this.transform = new TransformHandler(this.position, destination, animation);
    }

    @Override
    public Vector2i getSize() {
        return this.template.getSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.transform != null) {
            this.transform.tick();
            this.position = this.transform.getPosition();
            if (this.transform.isCompleted()) {
                this.transform = null;
            }
        }
        this.container.tick();
    }

    @Override
    public void render(final PixelContainer window) {
        final Vector2i size = this.getSize();
        this.template.getBackgroundSprite().render(
            window,
            this.position.getX(),
            this.position.getY()
        );
        final PixelContainer pixelContainer = new PixelContainer(
            new int[size.getX() * size.getY()],
            size.getX(),
            size.getY()
        );
        this.container.render(pixelContainer);
        window.copyPixels(
            this.position.getX(),
            this.position.getY(),
            size.getX(),
            pixelContainer.getPixels()
        );
    }

    public static class TransformHandler {

        private final Vector2i initialPosition;
        private final Vector2i destination;
        private final Animation animation;

        public TransformHandler(final Vector2i initialPosition, final Vector2i destination,
                                final Animation animation) {
            this.initialPosition = initialPosition;
            this.destination = destination;
            this.animation = animation;
        }

        public void tick() {
            this.animation.tick();
        }

        public Vector2i getPosition() {
            final double progress = this.animation.determineValue();
            final Vector2i delta =
                this.destination.sub(this.initialPosition)
                    .toDouble()
                    .mul(progress)
                    .floor().toInt();
            return this.initialPosition.add(delta);
        }

        public boolean isCompleted() {
            return this.animation.isCompleted();
        }

    }

}
