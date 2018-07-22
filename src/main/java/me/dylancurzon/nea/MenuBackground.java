package me.dylancurzon.nea;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite.TickContainer;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.Tickable;

public class MenuBackground implements Tickable, Renderable {

//    private static final int NUM_STARS = 30;
//    private static final Random RANDOM = ThreadLocalRandom.current();
//    private final Set<Star> stars = new LinkedHashSet<>(NUM_STARS);

    private final Planet planet;

    public MenuBackground(final int width, final int height) {
        this.planet = new Planet(200, 200);
//        for (int i = 0; i < NUM_STARS; i++) {
//            final Vector2i pos = Vector2i.of(
//                RANDOM.nextInt(width),
//                RANDOM.nextInt(height)
//            );
//            this.stars.add(new Star(pos));
//        }
    }

    @Override
    public void render(final PixelContainer window) {
//        for (final Star star : this.stars) {
//            star.render(container);
//        }
        final int width = 200;
        final int height = 200;
        final PixelContainer container = new PixelContainer(
            new int[width * height],
            width,
            height
        );
        this.planet.render(container);
        window.copyPixels(10, 10, width, container.getPixels());
    }

    @Override
    public void tick() {
//        for (final Star star : this.stars) {
//            star.tick();
//        }
        this.planet.tick();
    }

    public static class Star implements Tickable, Renderable {

        private static final AnimatedSprite SPRITE = AnimatedSprite.loadAnimatedSprite(
            "star-twinkle.png",
            8,
            5
        );
        private final TickContainer tickContainer = SPRITE.createContainer();
        private final Vector2i position;

        private int ticks;
        private boolean twinkling;

        public Star(final Vector2i position) {
            this.position = position;
        }

        @Override
        public void render(final PixelContainer container) {
            this.tickContainer.render(
                container,
                this.position.getX(),
                this.position.getY()
            );
        }

        @Override
        public void tick() {

        }

    }

}
