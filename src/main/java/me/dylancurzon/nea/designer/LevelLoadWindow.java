package me.dylancurzon.nea.designer;

import me.dylancurzon.nea.terrain.level.Level;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LevelLoadWindow extends JFrame {

    public LevelLoadWindow() {
        super("Level Editor");

        final Dimension dim = new Dimension(250, 100);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setResizable(false);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel(new GridLayout(1, 2));

        final JPanel panel1 = new JPanel(new GridBagLayout());
        final JButton button1 = new JButton("Choose Level");
        button1.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            final int option = chooser.showOpenDialog(panel1);
            if (option != JFileChooser.APPROVE_OPTION) return;

            final File file = chooser.getSelectedFile();
            if (file == null) throw new RuntimeException("file == null");
            final Level level = Level.fromFile(file);
            (new LevelDesigner(level)).start();
        });

        panel1.add(button1);

        final JPanel panel2 = new JPanel(new GridBagLayout());
        final JButton button2 = new JButton("Create Level");
        button2.addActionListener(e -> {
            final Level level = new Level();
            (new LevelDesigner(level)).start();
        });

        panel2.add(button2);

        panel.add(panel1);
        panel.add(panel2);

        this.pack();
        this.setLocationRelativeTo(null);
        this.getContentPane().add(panel);
    }

}
