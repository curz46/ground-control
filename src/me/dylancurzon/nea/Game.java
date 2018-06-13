package me.dylancurzon.nea;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends JPanel {

    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH / 16*9;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private boolean running;
    private int ticks;

    private JFrame frame;

    private void start() {
        this.running = true;
        this.initialize();
    }

    private void initialize() {
        this.frame = new JFrame("Game");

        final Dimension dim = new Dimension(WIDTH, HEIGHT);
        this.frame.setSize(dim);
        this.frame.setPreferredSize(dim);
        this.frame.setMinimumSize(dim);

        this.frame.add(this);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
                System.out.println("Ticks: " + updates + ", Frames: " + frames);
                frames = 0;
                updates = 0;
            }
            if (lastUpdate + (1000 / 60) < System.currentTimeMillis()) {
                lastUpdate = System.currentTimeMillis();
                this.update();
                updates++;
            }

            // add fps checks, add update time limiting
            this.render();
            frames++;
        }
    }

    private void update() {
//        this.ticks++;
        for (int i = 0; i < this.pixels.length; i++) {
            if (i % 3 == 0) this.pixels[i] = i + this.ticks++;
            else this.pixels[i] = 0;
        }
    }

    private void render() {
        BufferStrategy bs = this.frame.getBufferStrategy();
        if (bs == null) {
            this.frame.createBufferStrategy(2);
            bs = this.frame.getBufferStrategy();
        }

        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();
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