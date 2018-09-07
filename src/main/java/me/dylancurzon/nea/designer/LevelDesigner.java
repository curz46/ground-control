package me.dylancurzon.nea.designer;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.AbstractGame;
import me.dylancurzon.nea.Camera;
import me.dylancurzon.nea.terrain.level.Level;
import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.terrain.world.tile.TileType;
import me.dylancurzon.nea.terrain.world.tile.TileTypes;
import me.dylancurzon.nea.util.Keys;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

import java.awt.event.KeyEvent;
import me.dylancurzon.nea.util.Vector3d;

public class LevelDesigner extends AbstractGame {

    private final Level level;
    private final Camera camera;

    private TileType type = TileTypes.GRASS;

    public LevelDesigner(@NotNull final Level level) {
        super("Level Designer");
        this.level = level;

        final Vector2i size = Vector2i.of(AbstractGame.WIDTH, AbstractGame.HEIGHT)
            .div(Tile.TILE_WIDTH)
            .ceil().toInt();
        this.camera = new Camera(size, level);
    }

    @Override
    protected void render() {
        this.camera.render(this.window);

        final Vector2i mousePosition = this.getScaledMousePosition();
        if (mousePosition == null) return;
        final Vector2d tilePos = this.camera.getTilePosition(this.window, mousePosition);
        final Vector2i tileStart = tilePos.floor()
            .sub(this.camera.getBoundA())
            .add(Vector2d.of(0, 1))
            .mul(Tile.TILE_WIDTH)
            .toInt();
        this.type.getSprite().render(this.window, tileStart.getX(), this.window.getHeight() - 1 - tileStart.getY());
    }

    @Override
    protected void update() {
        final double speed = 0.2;
        Vector2d cameraTransform = Vector2d.of(0, 0);
        if (Keys.pressed(KeyEvent.VK_W) || Keys.pressed(KeyEvent.VK_UP)) {
            cameraTransform = cameraTransform.add(Vector2d.of(0, speed));
        }
        if (Keys.pressed(KeyEvent.VK_A) || Keys.pressed(KeyEvent.VK_LEFT)) {
            cameraTransform = cameraTransform.add(Vector2d.of(-speed, 0));
        }
        if (Keys.pressed(KeyEvent.VK_S) || Keys.pressed(KeyEvent.VK_DOWN)) {
            cameraTransform = cameraTransform.add(Vector2d.of(0, -speed));
        }
        if (Keys.pressed(KeyEvent.VK_D) || Keys.pressed(KeyEvent.VK_RIGHT)) {
            cameraTransform = cameraTransform.add(Vector2d.of(speed, 0));
        }
        this.camera.transform(cameraTransform);
    }

    @Override
    public void onMouseClick(final Vector2i point) {

    }

}
