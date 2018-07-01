package me.dylancurzon.nea.gfx;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.Window;

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
    public TextSprite getText(final String content) {
        return new TextSprite(this, content.toUpperCase());
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

    @Immutable
    public class TextSprite implements Sprite {

        @NotNull
        private final TextType type;
        @NotNull
        private final String content;

        public TextSprite(@NotNull final TextType type, @NotNull final String content) {
            this.type = type;
            this.content = content;
        }

        @Override
        public void render(@NotNull final Window window, final int offsetX, final int offsetY) {

        }

    }

}