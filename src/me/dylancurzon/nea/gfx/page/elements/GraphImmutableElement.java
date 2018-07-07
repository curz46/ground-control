package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.util.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GraphImmutableElement extends ImmutableElement {

    private final Vector2i size;
    private final Supplier<Double> valueSupplier;
    private int ticks = 0;

    private GraphImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer,
                                  final Vector2i size, final Supplier<Double> valueSupplier) {
        super(margin, tickConsumer);
        this.size = size;
        this.valueSupplier = valueSupplier;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public MutableElement asMutable() {
        final List<Double> values = new ArrayList<>();
        final int resolutionX = 1;
        return new MutableElement(super.margin) {
            @Override
            public Vector2i getSize() {
                return GraphImmutableElement.this.size;
            }

            @Override
            public void tick() {
                final double value = GraphImmutableElement.this.valueSupplier.get();
                values.add(value);
                final Consumer<MutableElement> consumer = GraphImmutableElement.super.getTickConsumer();
                if (consumer != null) {
                    consumer.accept(this);
                }
            }

            @Override
            public void render(final PixelContainer container) {
                final int ticks = GraphImmutableElement.this.ticks;
                for (int dt = 0; dt < container.getWidth(); dt++) {
                    final int index = (int) Math.floor(values.size() - (dt * resolutionX)) - (ticks % resolutionX);
                    for (int y = 0; y < container.getHeight(); y++) {
                        if ((container.getWidth() - 1 - dt) == 0 || y == (container.getHeight() - 1)) {
                            container.setPixel(container.getWidth() - dt, y, 0xFFBBBBBB);
                        } else if (index % 5 == 0 && y % 5 == 0) {
                            container.setPixel(container.getWidth() - dt, y, 0xFFAAAAAA);
                        }
                    }
                    if (index < 0 || index >= values.size()) continue;
                    final double value = values.get(index);
                    final int y = container.getHeight() - (int) Math.floor(value * container.getHeight());
                    container.setPixel(container.getWidth() - dt, y, 0xFFFFFFFF);
                }
            }
        };
    }

    public static class Builder extends ImmutableElement.Builder<GraphImmutableElement, Builder> {

        private Vector2i size = Vector2i.of(50, 50);
        private Supplier<Double> valueFunction;

        @NotNull
        public Builder setSize(final Vector2i size) {
            this.size = size;
            return this;
        }

        @NotNull
        public Builder setSupplier(final Supplier<Double> valueFunction) {
            this.valueFunction = valueFunction;
            return this;
        }

        @Override
        @NotNull
        public Builder self() {
            return this;
        }

        @Override
        @NotNull
        public GraphImmutableElement build() {
            if (this.valueFunction == null) {
                throw new RuntimeException("GraphImmutableElement.Builder requires ValueSupplier!");
            }
            return new GraphImmutableElement(super.margin, super.tickConsumer, this.size, this.valueFunction);
        }

    }


}
