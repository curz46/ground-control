package me.dylancurzon.nea;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import me.dylancurzon.nea.util.Keys;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.gen.Generators;
import me.dylancurzon.nea.world.tile.Tile;

public class Game extends JPanel {

    public static final int SCALE = 3;
    public static final int WIDTH = 1200 / SCALE;
    public static final int HEIGHT = 720 / SCALE;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();

    private boolean running;
    private int ticks;

    private final Window window = new Window(this.pixels, WIDTH, HEIGHT);
    private World world;
    private Camera camera;

    private JFrame frame;

    private void start() {
        this.running = true;
        this.initialize();
    }

    private void initialize() {
        this.frame = new JFrame("Game");

        final Dimension dim = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
//        this.frame.setSize(dim);
        this.frame.setMinimumSize(dim);
        this.frame.setPreferredSize(dim);
        this.frame.setMaximumSize(dim);
        this.frame.pack();
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);

        this.frame.add(this);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {}

            @Override
            public void keyPressed(final KeyEvent e) {
                Keys.toggle(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                Keys.toggle(e.getKeyCode(), false);
            }
        });

        final Path homePath = Paths.get(System.getProperty("user.home"));
//        final Path homePath = Paths.get("P:"); // TODO: this doesn't appear to do what it should but it's ok for now
        this.world = new World("my_world", Generators.ROCKY, homePath.resolve(".groundcontrol"));
        this.camera = new Camera(
            Vector2i.of(WIDTH / Tile.TILE_WIDTH, HEIGHT / Tile.TILE_WIDTH),
            this.world
        );
        this.camera.transform(Vector2d.of(-5, -5));

        this.loop();
    }

    private void loop() {
        long lastSecond = 0;
        long lastUpdate = 0;
        int updates = 0;
        int frames = 0;

        while (this.running) {
            if (lastSecond + 1000 < System.currentTimeMillis()) {
                lastSecond = System.currentTimeMillis();
                this.frame.setTitle("Game | Ticks: " + updates + ", Frames: " + frames);
                frames = 0;
                updates = 0;
            }
            if (lastUpdate + (1000.0 / 60) < System.currentTimeMillis()) {
                lastUpdate = System.currentTimeMillis();
                this.update();
                updates++;
            }

            this.render();
            frames++;
        }
    }

    private long lastToggle;

    private void update() {
        this.world.tick();
        this.camera.tick();

        final double speed = 1.0 / Tile.TILE_WIDTH;
        if (Keys.pressed(KeyEvent.VK_UP)) {
            this.camera.transform(Vector2d.of(0, speed));
        }
        if (Keys.pressed(KeyEvent.VK_DOWN)) {
            this.camera.transform(Vector2d.of(0, -speed));
        }
        if (Keys.pressed(KeyEvent.VK_RIGHT)) {
            this.camera.transform(Vector2d.of(speed, 0));
        }
        if (Keys.pressed(KeyEvent.VK_LEFT)) {
            this.camera.transform(Vector2d.of(-speed, 0));
        }
        if (this.world == null) return;
        if (Keys.pressed(KeyEvent.VK_U)) {
            this.world.unloadAllChunks();
        }
        if (Keys.pressed(KeyEvent.VK_T)) {
            if (System.currentTimeMillis() - 300 < this.lastToggle) return;
            this.camera.toggleTransform();
            this.lastToggle = System.currentTimeMillis();
        }

//        final Point pos = this.getMousePosition();
//        if (pos == null) {
//            this.camera.setMousePosition(null);
//        } else {
//            this.camera.setMousePosition(
//                Vector2d.of(pos.getX(), HEIGHT + 3 - pos.getY()).toInt()
//            );
//        }
    }

    private void render() {
        BufferStrategy bs = this.frame.getBufferStrategy();
        if (bs == null) {
            this.frame.createBufferStrategy(2);
            bs = this.frame.getBufferStrategy();
        }

        this.camera.render(this.window, 0, 0);

        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.scale(SCALE, SCALE);
//        final AffineTransform at = new AffineTransform();
//        at.concatenate(AffineTransform.getScaleInstance(1, -1));
//        at.concatenate(AffineTransform.getTranslateInstance(0, -this.image.getHeight()));
//        g.transform(at);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(this.image, 0, 0, WIDTH, HEIGHT, null);
        bs.show();
        g.dispose();
    }

    public static void main(final String[] args) {
        new Game().start();
    }

}