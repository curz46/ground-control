package me.dylancurzon.nea.gfx.page.elements.container;

import static me.dylancurzon.nea.gfx.page.elements.container.Positioning.INLINE;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Pos;
import javafx.util.Pair;
import me.dylancurzon.nea.gfx.page.InteractOptions;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.page.elements.ImmutableElement;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableContainer;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;
import me.dylancurzon.nea.gfx.page.elements.mutable.WrappingMutableElement;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LayoutImmutableContainer extends ImmutableElement implements ImmutableContainer {

    private final List<Pair<Integer, Function<ImmutableContainer, ImmutableElement>>> elements;
    private final Vector2i size;
    private final Spacing padding;
    private final Positioning positioning;
    private final boolean centering;
    private final boolean scrollable;

    private LayoutImmutableContainer(final Spacing margin, final Consumer<MutableElement> tickConsumer,
                                     final List<Pair<Integer, Function<ImmutableContainer, ImmutableElement>>> elements,
                                     final Vector2i size, final Spacing padding,
                                     final Positioning positioning, final boolean centering,
                                     final boolean scrollable,
                                     final Function<MutableElement, WrappingMutableElement> mutator,
                                     final InteractOptions interactOptions) {
        super(margin, tickConsumer, mutator, interactOptions);
        this.elements = elements;
        this.size = size;
        this.positioning = positioning;
        this.centering = centering;
        if (padding == null) {
            this.padding = Spacing.ZERO;
        } else {
            this.padding = padding;
        }
        this.scrollable = scrollable;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    @NotNull
    public MutableContainer asMutable() {
        final int total = this.elements.stream()
            .map(Pair::getKey).mapToInt(Integer::intValue).sum();
        final List<ImmutableElement> wrappedElements = this.elements.stream()
            .map(pair -> ImmutableContainer.builder()
                .setCentering(this.centering)
                .setSize((this.positioning == INLINE
                    ? this.size.toDouble().mul(Vector2d.of(((double) pair.getKey()) / total, 1))
                    : this.size.toDouble().mul(Vector2d.of(1, ((double) pair.getKey()) / total)))
                    .ceil().toInt())
                .add(pair.getValue())
                .build())
            .collect(Collectors.toList());
        return ImmutableContainer.builder()
            .setSize(this.size)
            .setPadding(this.padding)
            .setPositioning(this.positioning)
            .setScrollable(this.scrollable)
            .add(wrappedElements)
            .build()
            .asMutable();
            /*
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



//                final int ratioTotal = mutableElements.stream()
//                    .map(Pair::getKey).mapToInt(Integer::intValue).sum();
//                final Spacing padding = LayoutImmutableContainer.this.padding;
//                Vector2i pos = Vector2i.of(padding.getLeft(), padding.getTop());
//                for (final Pair<Integer, MutableElement> pair : mutableElements) {
//                    final int ratio = pair.getKey();
//                    final MutableElement mut = pair.getValue();
//
//                    final Vector2i elementSize = mut.getSize();
//                    final PixelContainer elementContainer = new PixelContainer(
//                        new int[elementSize.getX() * elementSize.getY()],
//                        elementSize.getX(),
//                        elementSize.getY()
//                    );
//                    mut.render(elementContainer);
//
//                    final Vector2i paddedSize = this.getPaddedSize();
//                    final Vector2d delta = paddedSize
//                        .toDouble()
//                        .mul(((double) ratio) / ratioTotal);
//
//                    Vector2i correctedPos = pos;
//                    if (LayoutImmutableContainer.this.centering) {
//                        correctedPos = pos.toDouble()
//                            .add(LayoutImmutableContainer.this.inline
//                                ? Vector2d.of(delta.div(2).getX(), paddedSize.div(2).getY())
//                                : Vector2d.of(paddedSize.div(2).getX(), delta.div(2).getY()))
//                            .sub(elementSize.div(2))
//                            .floor().toInt();
//                    }
//                    container.copyPixels(
//                        correctedPos.getX(),
//                        correctedPos.getY(),
//                        elementSize.getX(),
//                        elementContainer.getPixels()
//                    );
//
//                    pos = pos.add(
//                        delta
//                            .mul(LayoutImmutableContainer.this.inline
//                                ? Vector2d.of(1, 0)
//                                : Vector2d.of(0, 1))
//                            .floor().toInt()
//                    );
//                }
            }
        };
        */
    }

    @Override
    public Spacing getPadding() {
        return this.padding;
    }

    @Override
    public Vector2i getSize() {
        return this.size;
    }

    @Override
    public Vector2i getMarginedSize() {
        return this.getSize().add(
            Vector2i.of(
                super.margin.getLeft() + super.margin.getRight(),
                super.margin.getBottom() + super.margin.getTop()
            )
        );
    }

    @Override
    public Vector2i getPaddedSize() {
        return this.getSize().sub(
            Vector2i.of(
                this.padding.getLeft() + this.padding.getRight(),
                this.padding.getBottom() + this.padding.getTop()
            )
        );
    }

    @Override
    public boolean isCentering() {
        return this.centering;
    }

    @Override
    public Positioning getPositioning() {
        return this.positioning;
    }

    @Override
    @NotNull
    public boolean isScrollable() {
        return this.scrollable;
    }

    public static class Builder extends ImmutableElement.Builder<LayoutImmutableContainer, Builder> {

        private final List<Pair<Integer, Function<ImmutableContainer, ImmutableElement>>> elements = new ArrayList<>();
        private Vector2i size;
        private Spacing padding;
        private Positioning positioning;
        private boolean centering;
        private boolean scrollable;

        @NotNull
        public Builder add(final int ratio, final ImmutableElement element) {
            this.elements.add(new Pair<>(ratio, page -> element));
            return this;
        }

        @NotNull
        public Builder add(final int ratio,
                           final Function<ImmutableContainer, ImmutableElement> fn) {
            this.elements.add(new Pair<>(ratio, fn));
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
        public Builder setPositioning(final Positioning positioning) {
            this.positioning = positioning;
            return this;
        }

        @NotNull
        public Builder setCentering(final boolean centering) {
            this.centering = centering;
            return this;
        }

        @NotNull
        public Builder setScrollable(final boolean scrollable) {
            this.scrollable = scrollable;
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
                super.tickConsumer,
                this.elements,
                this.size,
                this.padding,
                this.positioning,
                this.centering,
                this.scrollable,
                super.mutator,
                super.interactOptions
            );
        }

    }

}
