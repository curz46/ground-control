package me.dylancurzon.nea.gfx.page.elements.mutable;

import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.page.animation.Animation;
import me.dylancurzon.nea.gfx.page.animation.QuarticEaseInAnimation;
import me.dylancurzon.nea.gfx.page.elements.container.ImmutableContainer;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

import java.util.List;

public abstract class MutableContainer extends MutableElement {

    private static final double SCROLL_FACTOR = 8;

    private final ImmutableContainer container;

    private final List<MutableElement> elements;
    protected double scroll;
    protected double scrollVelocity;

    private TransformHandler transform;

    protected MutableContainer(final Spacing margin, final ImmutableContainer container,
                               final List<MutableElement> elements) {
        super(margin);
        this.container = container;
        this.elements = elements;
    }

    @Override
    public void tick() {
        if (!this.container.isScrollable()) return;
        this.scroll += this.scrollVelocity;
        this.scrollVelocity *= 0.8;
        this.checkBounds();
    }

    public void scroll(final double amount) {
        if (!this.container.isScrollable()) return;
        if (this.transform != null) {
            this.transform = null;
        }
        this.scrollVelocity += amount * SCROLL_FACTOR;
        this.checkBounds();
    }

    private void checkBounds() {
        if ((this.scroll + 5) < 0) {
            this.transform = new TransformHandler(
                Vector2d.of(0, this.scroll),
                Vector2d.of(0, 0),
                new QuarticEaseInAnimation(0, 1, 20)
            );
        }
        final double max = this.getMaxScroll();
        if ((this.scroll - 5) > max) {
            this.transform = new TransformHandler(
                Vector2d.of(0, this.scroll),
                Vector2d.of(0, max),
                new QuarticEaseInAnimation(0, 1, 20)
            );
        }

        if (this.transform != null) {
            this.transform.tick();
            this.scroll = this.transform.getPosition().getY();
        }
    }

    private double getMaxScroll() {
        Vector2i size = Vector2i.of(
            0,
            0
        );
        for (final MutableElement mut : this.elements) {
            final Vector2i elementSize = mut.getSize()
                .add(Vector2i.of(
                    mut.getMargin().getLeft() + mut.getMargin().getRight(),
                    mut.getMargin().getBottom() + mut.getMargin().getTop()
                ));
            size = size.add(Vector2i.of(0, elementSize.getY()));
        }
        return size.getY() - this.container.getPaddedSize().getY();
    }

    public static class TransformHandler {

        private final Vector2d initialPosition;
        private final Vector2d destination;
        private final Animation animation;

        public TransformHandler(final Vector2d initialPosition, final Vector2d destination,
                                final Animation animation) {
            this.initialPosition = initialPosition;
            this.destination = destination;
            this.animation = animation;
        }

        public void tick() {
            this.animation.tick();
        }

        public Vector2d getPosition() {
            final double progress = this.animation.determineValue();
            final Vector2d delta = this.destination.sub(this.initialPosition).mul(progress);
            return this.initialPosition.add(delta);
        }

        public boolean isCompleted() {
            return this.animation.isCompleted();
        }

    }

}
