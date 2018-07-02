package me.dylancurzon.nea.gfx;

import com.sun.istack.internal.NotNull;
import java.awt.image.BufferedImage;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.util.ImageUtil;

@Immutable
public class TextType {

    private static final char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    @NotNull
    private final int[][] characterData;
    @NotNull
    private final int width;

    public TextType(@NotNull final int[][] characterData, @NotNull final int width) {
        this.characterData = characterData;
        this.width = width;
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
        final int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        final int[][] characterData = new int[charCount][width * image.getHeight()];
        for (int n = 0; n < charCount; n++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    characterData[n][x + y * width] = rgb[(n * width) + x + (y * image.getWidth())];
                }
            }
        }
        return new TextType(characterData, width);
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
        if (index == -1) throw new RuntimeException("The character provided is not supported: " + character);
        return this.getCharacterData(index);
    }

    @NotNull
    public int[] getCharacterData(final int index) {
        return this.characterData[index];
    }

    public int getWidth() {
        return this.width;
    }

    @Immutable
    public class TextSprite implements Sprite {

        @NotNull
        private final TextType type;
        @NotNull
        private final String content;
        @NotNull
        private final int margin;

        public TextSprite(@NotNull final TextType type, @NotNull final String content, final int margin) {
            this.type = type;
            this.content = content;
            this.margin = margin;
        }

        @Override
        public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
            int i = 0;
            for (final char c : this.content.toCharArray()) {
                final int[] data = this.type.getCharacterData(c);
                final int x = offsetX + (this.type.getWidth() + this.margin) * i;
                window.copyPixels(x, offsetY, this.type.getWidth(), data);
                i++;
            }
        }

    }

}