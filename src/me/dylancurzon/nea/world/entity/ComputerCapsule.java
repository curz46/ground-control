package me.dylancurzon.nea.world.entity;

import static me.dylancurzon.nea.gfx.SpriteSheet.PRIMARY_SHEET;

import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.Sprite;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;

public class ComputerCapsule extends Entity {

    private static final Sprite COMPUTER = PRIMARY_SHEET.getSprite(0, 6, 32);

    public ComputerCapsule(final World world, final Vector2i position) {
        super(world, position);
    }

    @Override
    public void render(final Window window, final int offsetX, final int offsetY) {
        COMPUTER.render(window, offsetX, offsetY - 16);
    }

}
