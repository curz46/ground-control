package me.dylancurzon.nea.gfx.page.elements.mutable;

import com.sun.istack.internal.NotNull;
import java.util.function.Consumer;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.page.InteractOptions;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.util.Cached;
import me.dylancurzon.nea.util.Vector2i;

public abstract class MutableElement implements Renderable {

    @NotNull
    protected final Spacing margin;
    @NotNull
    protected final InteractOptions interactOptions;

    protected MutableContainer parent;

    private final Cached<Vector2i> cachedSize = new Cached<>();

    protected MutableElement(@NotNull final Spacing margin, final InteractOptions interactOptions) {
        this.margin = margin;
        this.interactOptions = interactOptions;
    }

    @NotNull
    public Spacing getMargin() {
        return this.margin;
    }

    @NotNull
    public InteractOptions getInteractOptions() {
        return this.interactOptions;
    }

    @NotNull
    public Vector2i getMarginedSize() {
        return this.getSize().add(
            Vector2i.of(
                this.margin.getLeft() + this.margin.getRight(),
                this.margin.getBottom() + this.margin.getTop()
            )
        );
    }

    public void setParent(final MutableContainer parent) {
        this.parent = parent;
    }

    public MutableContainer getParent() {
        return this.parent;
    }

    /**
     * If this element has a {@link InteractOptions#clickConsumer} this method will check if the {@param position} is
     * within the bounds of {@link this#getInteractMask()} and fire it if this is the case.
     * Note: if this {@link MutableElement} is a {@link MutableContainer}, it will also propagate the click event
     * through the hierarchy, such that any MutableElements it is responsible for rendering are able to handle it
     * themselves.
     * @param position A position relative to this MutableElement, such that the top-left corner of this element's
     *                 rendering bounds are (0, 0).
     * @param mask The interact mask to use to determine whether or not to fire the click consumer.
     *             If the value at the position's index is not zero it will fire.
     */
    public void click(@NotNull final Vector2i position, @NotNull final int[] mask) {
        if (position.getX() < 0 || position.getX() >= this.getSize().getX() ||
            position.getY() < 0 || position.getY() >= this.getSize().getY()) {
            return;
        }
        final int value = mask[position.getX() + position.getY() * this.getSize().getX()];
        // is value is zero, then the interact mask does not include this point; click event should not be fired
        if (value == 0) return;
        final Consumer<MutableElement> consumer = this.interactOptions.getClickConsumer();
        if (consumer == null) return;
        // fire click
        consumer.accept(this);
    }


    public void click(@NotNull final Vector2i position) {
        this.click(position, this.getInteractMask());
    }

    public Vector2i getMousePosition(final MutableElement element) {
        return this.parent.getMousePosition(element);
    }

    public Vector2i getMousePosition() {
        if (this.parent == null) return null;
        return this.parent.getMousePosition(this);
    }

    public void tick() {}

    public abstract int[] getInteractMask();

    public Vector2i getSize() {
        return this.cachedSize.get()
            .orElseGet(() -> {
                final Vector2i size = this.calculateSize();
                this.cachedSize.set(size);
                return size;
            });
    }

    @NotNull
    public abstract Vector2i calculateSize();

    public abstract void render(@NotNull final PixelContainer container);

}
