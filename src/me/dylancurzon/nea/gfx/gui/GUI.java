package me.dylancurzon.nea.gfx.gui;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.sprite.StaticSprite;
import me.dylancurzon.nea.util.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class GUI implements Renderable {

    @NotNull
    private final StaticSprite backgroundSprite;
    @NotNull
    private final Vector2i screenPosition;
    private final int margin;
    private final int padding;

    private final String header;

    @NotNull
    private List<String> strings;

    public GUI(final StaticSprite background, final Vector2i position, final int margin, final int padding,
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
    public void render(final Window window, final int ox, final int oy) {
        final Vector2i pos = this.screenPosition;
        this.backgroundSprite.render(window, pos.getX(), pos.getY());
    }

    public static class GUIBuilder {

        @NotNull
        private StaticSprite backgroundSprite;
        @NotNull
        private Vector2i screenPosition;
        @NotNull
        private List<String> strings = new ArrayList<>();

        private int margin;
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
        public GUIBuilder setMargin(final int margin) {
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
