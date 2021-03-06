package me.dylancurzon.nea.gfx.text;

import com.sun.istack.internal.NotNull;
import java.awt.image.BufferedImage;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.sprite.Sprite;
import me.dylancurzon.nea.util.ImageUtil;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class TextType {

    private static final char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.".toCharArray();
    @NotNull
    private final int[][] characterData;
    @NotNull
    private final int width;
    @NotNull
    private final int height;

    public TextType(@NotNull final int[][] characterData, @NotNull final int width,
        @NotNull final int height) {
        this.characterData = characterData;
        this.width = width;
        this.height = height;
    }

    @NotNull
    public static TextType loadTextType(final String resourceName, final int width) {
        final BufferedImage image = ImageUtil.loadResource(resourceName);
        final int charCount = image.getWidth() / width;
        if (charCount != characters.length) {
            throw new RuntimeException(
                "TextType character count does not match the number of characters they have: "
                    + resourceName
            );
        }
        final int[] rgb = image
            .getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        final int[][] characterData = new int[charCount][width * image.getHeight()];
        for (int n = 0; n < charCount; n++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    characterData[n][x + y * width] = rgb[(n * width) + x + (y * image.getWidth())];
                }
            }
        }
        return new TextType(characterData, width, image.getHeight());
    }

    @NotNull
    public TextSprite getText(final String content, final int margin) {
        return new TextSprite(this, content.toUpperCase(), margin);
    }

    @NotNull
    public int[] getCharacterData(final char character) {
        int index = -1;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == character) {
                index = i;
            }
        }
        if (index == -1) {
            throw new RuntimeException("The character provided is not supported: " + character);
        }
        return this.getCharacterData(index);
    }

    @NotNull
    public int[] getCharacterData(final int index) {
        return this.characterData[index];
    }

    public int getWidth() {
        return this.width;
    }

    @NotNull
    public int getHeight() {
        return this.height;
    }

    @Immutable
    public class TextSprite implements Sprite {

        @NotNull
        private final TextType type;
        @NotNull
        private final String content;
        private final int margin;

        public TextSprite(@NotNull final TextType type, @NotNull final String content,
            final int margin) {
            this.type = type;
            this.content = content;
            this.margin = margin;
        }

        @Override
        public void render(@NotNull final PixelContainer window, final int offsetX, final int offsetY) {
            int i = 0;
            for (final char c : this.content.toCharArray()) {
                if (c != ' ') {
                    final int[] data = this.type.getCharacterData(c);
                    final int x = offsetX + (this.type.getWidth() + this.margin) * i;
                    window.copyPixels(x, offsetY, this.type.getWidth(), data);
                }
                i++;
            }
        }

        @NotNull
        public Vector2i getSize() {
            return Vector2i.of(
                this.content.length() * this.type.getWidth() + (this.content.length() - 1) * this.margin,
                this.type.getHeight()
            );
        }

        @Override
        public int getWidth() {
            return this.getSize().getX();
        }

        @Override
        public int getHeight() {
            return this.getSize().getY();
        }

    }

}