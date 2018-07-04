package me.dylancurzon.nea.gfx.gui2;

import me.dylancurzon.nea.gfx.text.TextTypes;

public class Test {

    public static void test() {
        (new PageBuilder())
            .addElement(InlineContainer.of(
                TextElement
                    .of(TextTypes.SMALL.getText("Hello world", 2))
                    .margined(10),
                TextElement
                    .of(TextTypes.TINY.getText("This is a quick message", 2))
                    .margined(5)
            ).margined(10))
            .build();
    }

}
