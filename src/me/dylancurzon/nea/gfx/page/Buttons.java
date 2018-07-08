package me.dylancurzon.nea.gfx.page;

import me.dylancurzon.nea.gfx.page.elements.SpriteImmutableElement;
import me.dylancurzon.nea.gfx.sprite.SpriteSheet;

public interface Buttons {

    SpriteImmutableElement ARROW_UP = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(0, 0, 16))
        .build();
    SpriteImmutableElement ARROW_DOWN = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(1, 0, 16))
        .build();
    SpriteImmutableElement START = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(2, 0, 16))
        .build();
    SpriteImmutableElement STOP = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(3, 0, 16))
        .build();
    // TODO: CHECKBOX sprites need to be merged into one SpriteImmutableElement with a stateful MutableElement
    // this could be done by adding a WrappingMutableElement class which takes a MutableElement instance, generated
    // by the Static/AnimatedSpriteImmutableElement#asMutable method, and then overrides as required by creating a
    // subclass of the WrappedMutableElement. Could have generics so that super.<field name> calls access the correct
    // subclass of MutableElement.
    // potential names:
    SpriteImmutableElement CHECKBOX_UNCHECKED = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(0, 1, 16))
        .build();
    SpriteImmutableElement CHECKBOX_CHECKED = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(1, 1, 16))
        .build();
    //
    SpriteImmutableElement CIRCLE = SpriteImmutableElement.builder()
        .setSprite(SpriteSheet.GUI_SHEET.getSprite(2, 1, 16))
        .build();

}
