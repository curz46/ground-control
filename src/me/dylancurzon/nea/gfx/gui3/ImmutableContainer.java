package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class ImmutableContainer extends ImmutableElement {

    protected final List<ImmutableElement> elements;
    protected final Vector2i size;
    protected final Spacing padding;
    protected final boolean centering;

    protected ImmutableContainer(final Spacing margin, final List<ImmutableElement> elements,
        final Vector2i size, final Spacing padding, final boolean centering) {
        super(margin);
        this.elements = elements;
        this.size = size;
        if (padding == null) {
            this.padding = Spacing.ZERO;
        } else {
            this.padding = padding;
        }
        this.centering = centering;
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        final List<MutableElement> mutableElements = this.elements.stream()
            .map(ImmutableElement::asMutable)
            .collect(Collectors.toList());
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
//                Vector2i size = Vector2i.of(0, 0);
//                for (final MutableElement mut : mutableElements) {
//                    final Vector2i elementSize = mut.getSize();
//                    size = size.add(Vector2i.of(0, elementSize.getY()));
//                }
                return ImmutableContainer.this.size;
            }

            @Override
            public void render(final PixelContainer container) {
                if (ImmutableContainer.this.centering) {
                    // standard draw logic
                    final MutableElement mut = mutableElements.get(0);
                    final Vector2i elementSize = mut.getSize();
                    final PixelContainer elementContainer = new PixelContainer(
                        new int[elementSize.getX() * elementSize.getY()],
                        elementSize.getX(),
                        elementSize.getY()
                    );
                    mut.render(elementContainer);

                    // find centered position based on this container's size
                    final Vector2i centered = ImmutableContainer.this.size
                        .div(2)
                        .sub(elementSize.div(2))
                        .floor().toInt();
                    container.copyPixels(
                        centered.getX(),
                        centered.getY(),
                        elementSize.getX(),
                        elementContainer.getPixels()
                    );
                } else {
                    final Spacing padding = ImmutableContainer.this.padding;
                    Vector2i pos = Vector2i.of(padding.getLeft(), padding.getTop());
                    for (final MutableElement mut : mutableElements) {
                        final Vector2i elementSize = mut.getSize();
                        final PixelContainer elementContainer = new PixelContainer(
                            new int[elementSize.getX() * elementSize.getY()],
                            elementSize.getX(),
                            elementSize.getY()
                        );
                        mut.render(elementContainer);
                        container.copyPixels(
                            pos.getX(),
                            pos.getY(),
                            elementSize.getX(),
                            elementContainer.getPixels()
                        );
                        pos = pos.add(Vector2i.of(0, mut.getEffectiveSize().getY()));
                    }
                }
            }
        };
    }

    public static class Builder extends ImmutableElement.Builder<ImmutableContainer, Builder> {

        protected final List<ImmutableElement> elements = new ArrayList<>();
        protected Vector2i size;
        protected Spacing padding;
        protected boolean centering;

        @NotNull
        public Builder add(final ImmutableElement element) {
            this.elements.add(element);
            return this;
        }

        @NotNull
        public Builder add(final ImmutableElement... elements) {
            this.elements.addAll(Arrays.asList(elements));
            return this;
        }

        @NotNull
        public Builder add(final List<ImmutableElement> elements) {
            this.elements.addAll(elements);
            return this;
        }

        @NotNull
        public Builder setSize(final Vector2i size) {
            this.size = size;
            return this;
        }

        @NotNull
        public Builder setPadding(final Spacing padding) {
            this.padding = padding;
            return this;
        }

        @NotNull
        public Builder setCentering(final boolean centering) {
            this.centering = centering;
            return this;
        }

        @Override
        @NotNull
        public Builder self() {
            return this;
        }

        @Override
        @NotNull
        public ImmutableContainer build() {
            if (this.centering && this.elements.size() > 1) {
                throw new RuntimeException(
                    "A centering ImmutableContainer may only contain a single ImmutableElement!"
                );
            }
            if (this.elements.size() == 0) {
                throw new RuntimeException("Empty ImmutableContainer is not permitted!");
            }
            return new ImmutableContainer(
                super.margin,
                this.elements,
                this.size,
                this.padding,
                this.centering
            );
        }

    }

}
