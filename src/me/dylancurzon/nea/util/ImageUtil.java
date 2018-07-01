package me.dylancurzon.nea.util;

import com.sun.istack.internal.NotNull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import me.dylancurzon.nea.gfx.SpriteSheet;

public class ImageUtil {

    @NotNull
    public static BufferedImage loadResource(final String resourceName) {
        final InputStream stream = ImageUtil.class.getClassLoader().getResourceAsStream(resourceName);
        final BufferedImage image;
        try {
            image = ImageIO.read(stream);
        } catch (final IOException |IllegalArgumentException e) {
            throw new RuntimeException("Failed to load Sprite: " + resourceName, e);
        }
        return image;
    }

}
