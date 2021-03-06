package me.dylancurzon.nea.gfx.page;

import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.animation.Animation;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableContainer;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.Tickable;

public class Page extends MutableContainer implements Tickable {

    private final PageTemplate template;
    private final MutableContainer container;

    private Vector2i position;
    private TransformHandler transform;

    private Vector2i mousePosition = Vector2i.of(0, 0);

    protected Page(final PageTemplate template, final MutableContainer container) {
        super(template.getMargin(), template, container.getElements());
        this.template = template;
        this.container = container;

        this.position = this.template.getPosition();
    }

    public void setMousePosition(final Vector2i position) {
        this.mousePosition = position;
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
    public Vector2i calculateSize() {
        return this.template.getSize();
    }

    /**
     * @param position The position of the click event on the screen. Will only fire if within this Page's bounds.
     */
    @Override
    public void click(final Vector2i position) {
        final Vector2i relative = position.sub(this.position);
        this.container.click(relative);
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
    public Vector2i getMousePosition(final MutableElement element) {
        // A page is a wrapper for a container. The container has the same position.
        // final Vector2i position = this.calculatePositions().get(element);
        if (this.mousePosition == null) return null;
        return this.mousePosition.sub(this.position);
    }

    @Override
    public int[] getInteractMask() {
        final int[] mask = new int[this.getSize().getX() * this.getSize().getY()];
        for (int i = 0; i < mask.length; i++) {
            mask[i] = 1;
        }
        return mask;
    }

    @Override
    public void render(final PixelContainer window) {
        final Vector2i size = this.getSize();
        if (this.template.getBackgroundSprite() != null) {
            this.template.getBackgroundSprite().render(
                window,
                this.position.getX(),
                this.position.getY()
            );
        }
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
