package me.dylancurzon.nea;

import javax.swing.text.DefaultEditorKit.InsertContentAction;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.util.Keys;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public abstract class AbstractGame extends JPanel implements MouseListener, Runnable {

    public static final int SCALE = 3;
    public static final int WIDTH = 1200 / SCALE;
    public static final int HEIGHT = 720 / SCALE;
    private static final Dimension DIMENSION = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);

    protected final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    protected final int[] pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
    protected final PixelContainer window = new PixelContainer(this.pixels, WIDTH, HEIGHT);

    private final String windowTitle;
    private boolean running;

    protected JFrame frame;

    public AbstractGame(final String windowTitle) {
        this.windowTitle = windowTitle;
    }

    @Override
    public Dimension getPreferredSize() {
        return DIMENSION;
    }

    public void start() {
        this.running = true;
        this.initialize();
    }

    public void stop() {
        this.running = false;
    }

    protected Vector2i getScaledMousePosition() {
        final Point point = this.getMousePosition();
        return point == null
            ? null
            : Vector2d.of(point.getX() / SCALE, point.getY() / SCALE)
                .toInt();
    }

    private void initialize() {
        this.frame = new JFrame(this.windowTitle);

        this.frame.add(this);
        this.frame.setResizable(false);
        this.frame.pack();

        final Insets insets = this.frame.getInsets();
        this.frame.setSize(new Dimension(
            insets.left + WIDTH * SCALE + insets.right,
            insets.bottom + HEIGHT * SCALE + insets.top
        ));

        this.frame.setLocationRelativeTo(null);
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

//        this.frame.addMouseWheelListener(event -> this.scene.scroll(event.getPreciseWheelRotation()));
        this.frame.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Point point = e.getPoint();
                final Vector2i vector = Vector2d.of(point.getX() / SCALE, point.getY() / SCALE)
                    .toInt();
                AbstractGame.this.onMouseClick(vector);
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

//        this.loop();
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        long lastSecond = 0;
        long lastUpdate = 0;
        int updates = 0;
        int frames = 0;

        while (this.running) {
            if (lastSecond + 1000 < System.currentTimeMillis()) {
                lastSecond = System.currentTimeMillis();
                this.frame.setTitle(this.windowTitle + " | Ticks: " + updates + ", Frames: " + frames);
                frames = 0;
                updates = 0;
            }
            if (lastUpdate + (1000.0 / 60) < System.currentTimeMillis()) {
                lastUpdate = System.currentTimeMillis();
                this.update();
                updates++;
            }

            this.doRender();
            frames++;
        }
    }

    private void doRender() {
//        BufferStrategy bs = this.frame.getBufferStrategy();
//        if (bs == null) {
//            this.frame.createBufferStrategy(2);
//            bs = this.frame.getBufferStrategy();
//        }

        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = 0;
        }

        this.render();

//        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        final Graphics2D g = (Graphics2D) this.getGraphics();

        g.scale(SCALE, SCALE);

//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(this.image, 0, 0, WIDTH, HEIGHT, null);
//        bs.show();
//        g.dispose();
    }

    protected abstract void render();

    protected abstract void update();

}
