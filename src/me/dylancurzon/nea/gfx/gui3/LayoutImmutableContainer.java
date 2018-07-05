package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import javafx.util.Pair;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LayoutImmutableContainer extends ImmutableElement {

    private final List<Pair<Integer, ImmutableElement>> elements;
    private final Vector2i size;
    private final Spacing padding;
    private final boolean inline;
    private final boolean centering;

    private LayoutImmutableContainer(final Spacing margin, final List<Pair<Integer, ImmutableElement>> elements,
                                     final Vector2i size, final Spacing padding, final boolean inline,
                                     final boolean centering) {
        super(margin);
        this.elements = elements;
        this.size = size;
        this.inline = inline;
        this.centering = centering;
        if (padding == null) {
            this.padding = Spacing.ZERO;
        } else {
            this.padding = padding;
        }
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        final List<Pair<Integer, MutableElement>> mutableElements = this.elements.stream()
            .map(pair -> new Pair<>(pair.getKey(), pair.getValue().asMutable()))
            .collect(Collectors.toList());
        return new MutableElement(super.margin) {
            @Override
            @NotNull
            public Vector2i getSize() {
                return LayoutImmutableContainer.this.size;
            }

            @NotNull
            public Vector2i getPaddedSize() {
                final Spacing padding = LayoutImmutableContainer.this.padding;
                return this.getSize().sub(Vector2i.of(
                    padding.getLeft() + padding.getRight(),
                    padding.getTop() + padding.getBottom()
                ));
            }

            @Override
            public void render(@NotNull final PixelContainer container) {
                final int ratioTotal = mutableElements.stream()
                    .map(Pair::getKey).mapToInt(Integer::intValue).sum();
                final Spacing padding = LayoutImmutableContainer.this.padding;
                Vector2i pos = Vector2i.of(padding.getLeft(), padding.getTop());
                for (final Pair<Integer, MutableElement> pair : mutableElements) {
                    final int ratio = pair.getKey();
                    final MutableElement mut = pair.getValue();

                    final Vector2i elementSize = mut.getSize();
                    final PixelContainer elementContainer = new PixelContainer(
                        new int[elementSize.getX() * elementSize.getY()],
                        elementSize.getX(),
                        elementSize.getY()
                    );
                    mut.render(elementContainer);

                    final Vector2i paddedSize = this.getPaddedSize();
                    final Vector2d delta = (LayoutImmutableContainer.this.inline
                        ? Vector2i.of(paddedSize.getX(), 0)
                        : Vector2i.of(0, paddedSize.getY()))
                        .toDouble()
                        .mul(((double) ratio) / ratioTotal);

                    Vector2i correctedPos = pos;
                    if (LayoutImmutableContainer.this.centering) {
                        final Vector2i affectingSize = LayoutImmutableContainer.this.inline
                            ? Vector2i.of(elementSize.getX(), 0)
                            : Vector2i.of(0, elementSize.getY());
                        correctedPos = pos.toDouble()
                            .add(delta.div(2))
                            .sub(affectingSize.div(2))
                            .floor().toInt();
                    }
                    container.copyPixels(
                        correctedPos.getX(),
                        correctedPos.getY(),
                        elementSize.getX(),
                        elementContainer.getPixels()
                    );

                    pos = pos.add(delta.floor().toInt());
                }
            }
        };
    }

    public static class Builder extends ImmutableElement.Builder<LayoutImmutableContainer, Builder> {

        private final List<Pair<Integer, ImmutableElement>> elements = new ArrayList<>();
        private Vector2i size;
        private Spacing padding;
        private boolean inline;
        private boolean centering;

        @NotNull
        public Builder add(final int ratio, final ImmutableElement element) {
            this.elements.add(new Pair<>(ratio, element));
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
        public Builder setInline(final boolean inline) {
            this.inline = inline;
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
        public LayoutImmutableContainer build() {
            return new LayoutImmutableContainer(
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
