package me.dylancurzon.nea;

import me.dylancurzon.nea.util.Vector2i;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Game extends AbstractGame {

    public Game() {
        super("Ground Control");
        final Path homePath = Paths.get(System.getProperty("user.home"));
//        final Path homePath = Paths.get("P:"); // TODO: this doesn't appear to do what it should but it's ok for now
//        this.scene = new MenuScene(WIDTH, HEIGHT);
//        this.world = new World("my_world", Generators.ROCKY, homePath.resolve(".groundcontrol"));
//        this.camera = new Camera(
//            Vector2i.of(WIDTH / Tile.TILE_WIDTH, HEIGHT / Tile.TILE_WIDTH),
//            this.world
//        );
//        this.camera.transform(Vector2d.of(-5, -5));
    }

    @Override
    protected void render() {

    }

    @Override
    protected void update() {
//        this.world.tick();
//        this.scene.tick();
    }

    public static void main(final String[] args) {
        new Game().start();
    }

    @Override
    public void onMouseClick(final Vector2i point) {}

}