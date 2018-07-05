package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Vector2i;

public class LayoutImmutableContainer extends ImmutableContainer {

    private final Map<Integer, ImmutableElement> ratioElements;

    protected LayoutImmutableContainer(final Spacing margin, final Vector2i size,
        final Spacing padding, final boolean centering,
        final Map<Integer, ImmutableElement> ratioElements) {
        super(margin, new ArrayList<>(ratioElements.values()), size, padding, centering);
        this.ratioElements = ratioElements;
    }

    @Override
    @NotNull
    public MutableElement asMutable() {
        final Map<Integer, MutableElement> mutableElements = this.ratioElements.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().asMutable()
            ));
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
                return LayoutImmutableContainer.this.size;
            }

            @Override
            public void render(final PixelContainer container) {
                final Spacing padding = LayoutImmutableContainer.this.padding;
                Vector2i pos = Vector2i.of(padding.getLeft(), padding.getTop());
                for (final Map.Entry<Integer, MutableElement> entry : mutableElements.entrySet()) {
                    final int ratio = entry.getKey();
                    final MutableElement mut = entry.getValue();

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
        };
    }

    public static class Builder extends ImmutableElement.Builder<LayoutImmutableContainer, Builder> {

        private final Map<Integer, ImmutableElement> ratioElements = new HashMap<>();
        private Vector2i size;
        private Spacing padding;

        @NotNull
        public Builder add(final int ratio, final ImmutableElement element) {
            this.ratioElements.put(ratio, element);
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
                this.size,
                this.padding,
                false,
                this.ratioElements
            );
        }

    }

}
