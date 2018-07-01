package me.dylancurzon.nea.world.entity;

import static me.dylancurzon.nea.gfx.SpriteSheet.PRIMARY_SHEET;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.StaticSprite;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;

public class ComputerCapsule extends Entity {

    private static final StaticSprite COMPUTER = PRIMARY_SHEET.getSprite(0, 6, 32);

    public ComputerCapsule(@NotNull final World world, @NotNull final Vector2i position) {
        super(world, position);
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
        COMPUTER.render(window, offsetX, offsetY - 32);
    }

}
