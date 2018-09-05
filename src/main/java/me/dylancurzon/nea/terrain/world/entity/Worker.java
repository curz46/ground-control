package me.dylancurzon.nea.terrain.world.entity;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.World;

public class Worker extends Entity {

    private static final AnimatedSprite WORKER = AnimatedSprite.loadAnimatedSprite(
        "worker.png",
        16,
        20
    );
    private final AnimatedSprite.TickContainer sprite = WORKER.createContainer();

    public Worker(@NotNull final World world, @NotNull final Vector2i position) {
        super(world, position);
    }

    @Override
    public void tick() {
        this.sprite.tick();
    }

    @Override
    public void render(@NotNull final PixelContainer window, final int offsetX, final int offsetY) {
        this.sprite.render(window, offsetX, offsetY);
    }

}
