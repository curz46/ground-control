package me.dylancurzon.nea.gfx.gui;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.sprite.StaticSprite;
import me.dylancurzon.nea.gfx.text.TextTypes;
import me.dylancurzon.nea.util.Vector2i;

import java.util.ArrayList;
import java.util.List;
import me.dylancurzon.nea.world.Tickable;

public class GUI implements Tickable, Renderable {

    @NotNull
    private final StaticSprite backgroundSprite;
    @NotNull
    private Vector2i screenPosition;
    @NotNull
    private List<String> strings;

    private final Vector2i margin;
    private final int padding;
    private final String header;

    private Vector2i initialPosition;
    private Vector2i finalPosition;
    private Animation activeAnimation;

    public GUI(final StaticSprite background, final Vector2i position, final Vector2i margin, final int padding,
               final String header, final List<String> strings) {
        this.backgroundSprite = background;
        this.screenPosition = position;
        this.margin = margin;
        this.padding = padding;
        this.header = header;
        this.strings = strings;
    }

    @NotNull
    public static GUIBuilder builder() {
        return new GUIBuilder();
    }

    @Override
    public void tick() {
        // sanity checks
        if (this.initialPosition != null && this.finalPosition != null &&
            this.activeAnimation != null) {
            this.activeAnimation.tick();
            final double progress = this.activeAnimation.determineValue();
            final Vector2i delta =
                this.finalPosition.sub(this.initialPosition)
//                    .normalize()
                    .toDouble()
                    .mul(progress)
                    .floor().toInt();
            this.screenPosition = this.initialPosition.add(delta);
            if (this.activeAnimation.isCompleted()) {
                this.initialPosition = null;
                this.finalPosition = null;
                this.activeAnimation = null;
            }
        }
    }

    @Override
    public void render(final PixelContainer window) {
        final Vector2i pos = this.screenPosition;
        this.backgroundSprite.render(window, pos.getX(), pos.getY());
        Vector2i offset = this.screenPosition.add(this.margin);
        if (this.header != null) {
            TextTypes.SMALL
                .getText(this.header, 2)
                .render(window, offset.getX(), offset.getY());
            offset = offset.add(Vector2i.of(0, this.padding));
        }
        if (this.strings.isEmpty()) return;
        for (final String line : this.strings) {
            TextTypes.TINY
                .getText(line, 2)
                .render(window, offset.getX(), offset.getY());
            offset = offset.add(Vector2i.of(0, this.padding));
        }
    }

    public void transform(final Vector2i newPosition) {
        this.screenPosition = newPosition;
    }

    public void transform(final Vector2i newPosition, final Animation animation) {
        this.initialPosition = this.screenPosition;
        this.activeAnimation = animation;
        this.finalPosition = newPosition;
    }

    public static class GUIBuilder {

        @NotNull
        private StaticSprite backgroundSprite;
        @NotNull
        private Vector2i screenPosition;
        @NotNull
        private List<String> strings = new ArrayList<>();

        private Vector2i margin;
        private int padding;
        private String header;

        @NotNull
        public GUIBuilder setBackground(final StaticSprite sprite) {
            this.backgroundSprite = sprite;
            return this;
        }

        @NotNull
        public GUIBuilder setPosition(final Vector2i position) {
            this.screenPosition = position;
            return this;
        }

        @NotNull
        public GUIBuilder setMargin(final Vector2i margin) {
            this.margin = margin;
            return this;
        }

        @NotNull
        public GUIBuilder setPadding(final int padding) {
            this.padding = padding;
            return this;
        }

        @NotNull
        public GUIBuilder setHeader(final String header) {
            this.header = header;
            return this;
        }

        @NotNull
        public GUIBuilder addLine(final String line) {
            this.strings.add(line);
            return this;
        }

        @NotNull
        public GUIBuilder addLines(final List<String> lines) {
            this.strings.addAll(lines);
            return this;
        }

        @NotNull
        public GUI build() {
            if (this.backgroundSprite == null || this.screenPosition == null) {
                throw new RuntimeException("A required parameter is not provided. backgroundSprite|screenPosition");
            }
            return new GUI(
                this.backgroundSprite,
                this.screenPosition,
                this.margin,
                this.padding,
                this.header,
                this.strings
            );
        }

    }

}
