package me.dylancurzon.nea;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Keys;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.util.Vector3d;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.gen.Generators;

public class Game extends JPanel {

    public static final int SCALE = 3;
    public static final int WIDTH = 1200 / SCALE;
    public static final int HEIGHT = 720 / SCALE;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();

    private boolean running;
    private int ticks;

    private final PixelContainer window = new PixelContainer(this.pixels, WIDTH, HEIGHT);
    private Scene scene;
    private World world;

    public static JFrame frame;

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
        this.scene = new MenuScene(WIDTH, HEIGHT);
        this.world = new World("my_world", Generators.ROCKY, homePath.resolve(".groundcontrol"));
//        this.camera = new Camera(
//            Vector2i.of(WIDTH / Tile.TILE_WIDTH, HEIGHT / Tile.TILE_WIDTH),
//            this.world
//        );
//        this.camera.transform(Vector2d.of(-5, -5));

        this.frame.addMouseWheelListener(event -> this.scene.scroll(event.getPreciseWheelRotation()));
        this.frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Point point = e.getPoint();
                final Vector2i vector = Vector2d.of(point.getX() / SCALE, point.getY() / SCALE)
                    .toInt();
                Game.this.scene.click(vector);
            }

            @Override
            public void mousePressed(final MouseEvent e) {}

            @Override
            public void mouseReleased(final MouseEvent e) {}

            @Override
            public void mouseEntered(final MouseEvent e) {}

            @Override
            public void mouseExited(final MouseEvent e) {}
        });

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
//                this.frame.setTitle("Game | Ticks: " + updates + ", Frames: " + frames);
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
        final Point point = this.frame.getMousePosition();
        if (point == null) {
            this.scene.setMousePosition(null);
        } else {
            this.scene.setMousePosition(
                Vector2d.of(point.getX() / SCALE, point.getY() / SCALE)
                    .toInt()
            );
        }

        this.world.tick();
        this.scene.tick();

//        final double speed = 0.1;
        if (Keys.pressed(KeyEvent.VK_W)) {
            Planet.rotate(0, 1);
        }
        if (Keys.pressed(KeyEvent.VK_S)) {
            Planet.rotate(0, -1);
        }
        if (Keys.pressed(KeyEvent.VK_A)) {
            Planet.rotate(-1, 0);
        }
        if (Keys.pressed(KeyEvent.VK_D)) {
            Planet.rotate(1, 0);
        }
//        if (Keys.pressed(KeyEvent.VK_UP)) {
//            Planet.rotate(Vector3d.of(0, speed, 0));
//        }
//        if (Keys.pressed(KeyEvent.VK_DOWN)) {
//            Planet.rotate(Vector3d.of(0, -speed, 0));
//        }

//        this.camera.tick();

//        final double speed = 1.0 / Tile.TILE_WIDTH;
//        if (Keys.pressed(KeyEvent.VK_UP)) {
//            this.camera.transform(Vector2d.of(0, speed));
//        }
//        if (Keys.pressed(KeyEvent.VK_DOWN)) {
//            this.camera.transform(Vector2d.of(0, -speed));
//        }
//        if (Keys.pressed(KeyEvent.VK_RIGHT)) {
//            this.camera.transform(Vector2d.of(speed, 0));
//        }
//        if (Keys.pressed(KeyEvent.VK_LEFT)) {
//            this.camera.transform(Vector2d.of(-speed, 0));
//        }
//        if (this.world == null) return;
//        if (Keys.pressed(KeyEvent.VK_U)) {
//            this.world.unloadAllChunks();
//        }
//        if (Keys.pressed(KeyEvent.VK_T)) {
//            if (System.currentTimeMillis() - 300 < this.lastToggle) return;
////            this.camera.toggleTransform();
//            this.lastToggle = System.currentTimeMillis();
//        }
//        if (Keys.pressed(KeyEvent.VK_D)) {
//            if (System.currentTimeMillis() - 300 < this.lastToggle) return;
//            DefaultImmutableContainer.DEBUG = !DefaultImmutableContainer.DEBUG;
//            this.lastToggle = System.currentTimeMillis();
//        }
    }

    private void render() {
        BufferStrategy bs = this.frame.getBufferStrategy();
        if (bs == null) {
            this.frame.createBufferStrategy(2);
            bs = this.frame.getBufferStrategy();
        }

        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = 0;
        }

//        this.camera.render(this.window, 0, 0);
        this.scene.render(this.window);

        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.scale(SCALE, SCALE);

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