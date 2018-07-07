package me.dylancurzon.nea.gfx.page;

import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.animation.Animation;
import me.dylancurzon.nea.gfx.page.elements.MutableElement;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.Tickable;

public class Page extends MutableElement implements Tickable {

    private final PageTemplate template;
    private final MutableElement container;

    private Vector2i position;
    private TransformHandler transform;

    protected Page(final PageTemplate template, final MutableElement container) {
        super(template.getMargin());
        this.template = template;
        this.container = container;

        this.position = this.template.getPosition();
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
