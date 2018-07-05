package me.dylancurzon.nea.gfx.gui3;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public abstract class ImmutableElement {

    protected final Spacing margin;

    protected ImmutableElement(final Spacing margin) {
        if (margin == null) {
            this.margin = Spacing.ZERO;
        } else {
            this.margin = margin;
        }
    }

    @NotNull
    public abstract MutableElement asMutable();

    public static abstract class Builder<T extends ImmutableElement, B extends Builder> {

        protected Spacing margin;

        @NotNull
        public B setMargin(final Spacing margin) {
            this.margin = margin;
            return this.self();
        }

        @NotNull
        public abstract B self();

        @NotNull
        public abstract T build();

    }

}
