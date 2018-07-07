package me.dylancurzon.nea.gfx.page.elements;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.page.Spacing;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;

import java.util.function.Consumer;

@Immutable
public abstract class ImmutableElement {

    protected final Spacing margin;
    protected final Consumer<MutableElement> tickConsumer;

    protected ImmutableElement(final Spacing margin, final Consumer<MutableElement> tickConsumer) {
        if (margin == null) {
            this.margin = Spacing.ZERO;
        } else {
            this.margin = margin;
        }
        this.tickConsumer = tickConsumer;
    }

    @NotNull
    public abstract MutableElement asMutable();

    @NotNull
    public Spacing getMargin() {
        return this.margin;
    }

    public Consumer<MutableElement> getTickConsumer() {
        return this.tickConsumer;
    }

    public static abstract class Builder<T extends ImmutableElement, B extends Builder> {

        protected Spacing margin;
        protected Consumer<MutableElement> tickConsumer;

        @NotNull
        public B setMargin(final Spacing margin) {
            this.margin = margin;
            return this.self();
        }

        @NotNull
        public B tick(final Consumer<MutableElement> tickConsumer) {
            this.tickConsumer = tickConsumer;
            return this.self();
        }

        @NotNull
        public abstract B self();

        @NotNull
        public abstract T build();

    }

}
