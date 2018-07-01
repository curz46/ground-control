package me.dylancurzon.nea.world.entity;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.Window;
import me.dylancurzon.nea.gfx.StaticSprite;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;

import static me.dylancurzon.nea.gfx.SpriteSheet.PRIMARY_SHEET;

public class Worker extends Entity {

    private static final StaticSprite WORKER = PRIMARY_SHEET.getSprite(2, 7, 16, 16);

    public Worker(@NotNull final World world, @NotNull final Vector2i position) {
        super(world, position);
    }

    @Override
    public void render(@NotNull final Window window, final int offsetX, final int offsetY) {
        WORKER.render(window, offsetX, offsetY);
    }

}
