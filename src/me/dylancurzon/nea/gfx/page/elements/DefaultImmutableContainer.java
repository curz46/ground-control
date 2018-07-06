package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class DefaultImmutableContainer extends ImmutableElement implements ImmutableContainer {

    protected final List<Function<ImmutableContainer, ImmutableElement>> elements;
    protected final Vector2i size;
    protected final Spacing padding;
    protected final boolean inline;
    protected final boolean centering;

    protected DefaultImmutableContainer(final Spacing margin,
                                 final List<Function<ImmutableContainer, ImmutableElement>> elements,
                                 final Vector2i size, final Spacing padding, final boolean inline,
                                 final boolean centering) {
        super(margin);
        this.elements = elements;
        this.size = size;
        if (padding == null) {
            this.padding = Spacing.ZERO;
        } else {
            this.padding = padding;
        }
        this.inline = inline;
        this.centering = centering;
    }

    @NotNull
    public static Builder builder() {
        return new ContainerBuilder();
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        final List<MutableElement> mutableElements = this.elements.stream()
            .map(fn -> fn.apply(this))
            .map(ImmutableElement::asMutable)
            .collect(Collectors.toList());
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
                if (DefaultImmutableContainer.this.size == null) {
                    Vector2i size = Vector2i.of(0, 0);
                    for (final MutableElement mut : mutableElements) {
                        final Vector2i elementSize = mut.getSize();
                        size = size.add(Vector2i.of(0, elementSize.getY()));
                    }
                    return size;
                } else {
                    return DefaultImmutableContainer.this.size;
                }
            }

            @Override
            public void render(final PixelContainer container) {
                if (DefaultImmutableContainer.this.centering) {
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
                    final Vector2i centered = DefaultImmutableContainer.this.size
                        .div(2)
                        .sub(elementSize.div(2))
                        .floor().toInt();
                    System.out.println(DefaultImmutableContainer.this.size);
                    System.out.println("Centered: " + centered);
                    container.copyPixels(
                        centered.getX(),
                        centered.getY(),
                        elementSize.getX(),
                        elementContainer.getPixels()
                    );
                } else {
                    final Spacing padding = DefaultImmutableContainer.this.padding;
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
                        if (DefaultImmutableContainer.this.inline) {
                            pos = pos.add(Vector2i.of(mut.getMarginedSize().getX(), 0));
                        } else {
                            pos = pos.add(Vector2i.of(0, mut.getMarginedSize().getY()));
                        }
                    }
                }
            }
        };
    }

    @NotNull
    public List<Function<ImmutableContainer, ImmutableElement>> getElements() {
        return this.elements;
    }

    @NotNull
    public Vector2i getSize() {
        return this.size;
    }

    @Override
    public Vector2i getMarginedSize() {
        return this.size.add(
            Vector2i.of(
                super.margin.getLeft() + super.margin.getRight(),
                super.margin.getBottom() + super.margin.getTop()
            )
        );
    }

    @Override
    public Vector2i getPaddedSize() {
        return this.size.sub(
            Vector2i.of(
                this.padding.getLeft() + this.padding.getRight(),
                this.padding.getBottom() + this.padding.getTop()
            )
        );
    }

    @NotNull
    public Spacing getPadding() {
        return this.padding;
    }

    public boolean isInline() {
        return this.inline;
    }

    public boolean isCentering() {
        return this.centering;
    }

    public static class ContainerBuilder extends Builder<ContainerBuilder> {

        @Override
        public ContainerBuilder self() {
            return this;
        }

    }

    public static abstract class Builder<T extends Builder> extends ImmutableElement.Builder<DefaultImmutableContainer, T> {

        protected final List<Function<ImmutableContainer, ImmutableElement>> elements = new ArrayList<>();
        protected Vector2i size;
        protected Spacing padding;
        protected boolean inline;
        protected boolean centering;

        @NotNull
        public T add(final ImmutableElement element) {
            this.elements.add(page -> element);
            return this.self();
        }

        @NotNull
        public T add(final ImmutableElement... elements) {
            for (final ImmutableElement el : elements) {
                this.add(el);
            }
            return this.self();
        }

        @NotNull
        public T add(final List<ImmutableElement> elements) {
            elements.forEach(this::add);
            return this.self();
        }

        @NotNull
        public T add(final Function<ImmutableContainer, ImmutableElement> fn) {
            this.elements.add(fn);
            return this.self();
        }

        @NotNull
        public T setSize(final Vector2i size) {
            this.size = size;
            return this.self();
        }

        @NotNull
        public Builder setPadding(final Spacing padding) {
            this.padding = padding;
            return this;
        }

        @NotNull
        public T setInline(final boolean inline) {
            this.inline = inline;
            return this.self();
        }

        @NotNull
        public T setCentering(final boolean centering) {
            this.centering = centering;
            return this.self();
        }

        @Override
        @NotNull
        public DefaultImmutableContainer build() {
            if (this.centering && this.elements.size() > 1) {
                throw new RuntimeException(
                    "A centering ImmutableContainer may only contain a single ImmutableElement!"
                );
            }
            if (this.elements.size() == 0) {
                throw new RuntimeException("Empty ImmutableContainer is not permitted!");
            }
            return new DefaultImmutableContainer(
                super.margin,
                this.elements,
                this.size,
                this.padding,
                this.inline,
                this.centering
            );
        }

    }

}
