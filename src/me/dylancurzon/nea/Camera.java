package me.dylancurzon.nea;

import com.sun.istack.internal.NotNull;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.gfx.page.Elements;
import me.dylancurzon.nea.gfx.page.InteractOptions;
import me.dylancurzon.nea.gfx.page.Page;
import me.dylancurzon.nea.gfx.page.PageTemplate;
import me.dylancurzon.nea.gfx.page.elements.ImmutableElement;
import me.dylancurzon.nea.gfx.page.elements.TextImmutableElement;
import me.dylancurzon.nea.gfx.page.elements.container.ImmutableContainer;
import me.dylancurzon.nea.gfx.page.elements.container.LayoutImmutableContainer;
import me.dylancurzon.nea.gfx.page.elements.container.Positioning;
import me.dylancurzon.nea.gfx.page.elements.mutable.MutableElement;
import me.dylancurzon.nea.gfx.sprite.AnimatedSprite;
import me.dylancurzon.nea.gfx.text.TextTypes;
import me.dylancurzon.nea.util.Benchmark;
import me.dylancurzon.nea.util.Vector2d;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.tile.Tile;
import me.dylancurzon.nea.world.tile.TileTypes;

/**
 * This {@link Camera} class represents the view of the world that is drawn by the game's window.
 * {@link Camera#boundA} determines the position. This is because a centered-position enforces
 * restrictions on the possible window sizes - a size where either dimension is not divisible by
 * two would cause rendering inconsistent with the actual window size, given that in the current
 * implementation of the game equates one pixel in the window with one pixel, and positional unit,
 * in the game's world. Note that a Camera is attached to a {@link World}, and this instance should
 * be destroyed and re-created when unloading the game world or loading another one.
 */
public class Camera implements Renderable {

    private final Benchmark render = new Benchmark("Camera#render_pixels", 20, false);

    // Given the current implementation, this should just be the size of the window.
    @NotNull
    private final Vector2i size;
    // This Camera object should be re-created when changing the World.
    @NotNull
    private final World world;
    // The upper-left bound of the Camera. This determines the position of the world view.
    @NotNull
    private Vector2d boundA = Vector2d.of(0, 0);

    private final BiFunction<String, Consumer<MutableElement>, Function<ImmutableContainer, ImmutableElement>> BUTTON =
        (string, consumer) -> ctr -> ImmutableContainer.builder()
            .setSize(ctr.getSize())
            .setCentering(true)
            .add(ImmutableContainer.builder()
                .setSize(Vector2i.of(
                    Elements.LARGE_BUTTON.getSprite().getWidth(),
                    Elements.LARGE_BUTTON.getSprite().getHeight()
                ))
                .setPositioning(Positioning.OVERLAY)
                .setCentering(true)
                .setInteractOptions(InteractOptions.builder()
                    .setHighlighting(true)
                    .click(consumer)
                    .build())
                .add(Elements.LARGE_BUTTON)
                .add(TextImmutableElement.builder()
                    .setText(TextTypes.SMALL.getText(string, 2))
                    .build())
                .build())
            .build();
    private final PageTemplate TEMPLATE = PageTemplate.builder()
        .setPosition(Vector2i.of(1, 8))
        .setSize(Vector2i.of(398, 231))
        .add(page -> ImmutableContainer.builder()
            .setSize(page.getSize())
            .setCentering(true)
            .add(ctr -> LayoutImmutableContainer.builder()
                .setSize(Vector2i.of(ctr.getSize().getX() / 3, ctr.getSize().getY() - 50))
                .setCentering(true)
                .add(1, this.BUTTON.apply("play", mut -> System.out.println("playegd")))
                .add(1, this.BUTTON.apply("options", mut -> System.out.println("options")))
                .add(1, this.BUTTON.apply("quit", mut -> System.out.println("just quit")))
                .build())
            .build())
        .build();
    private final Page page = this.TEMPLATE.asMutable();

    public Camera(final Vector2i size, final World world) {
        this.size = size;
        this.world = world;
    }

    // temp
    public void tick() {
        ((AnimatedSprite.TickContainer) TileTypes.WATER.getSprite()).tick();
    }

    public void setMousePosition(final Vector2i position) {
        this.page.setMousePosition(position);
    }

    public void click(final Vector2i position) {
        this.page.click(position);
    }

    @Override
    public void render(@NotNull final PixelContainer window) {
        // reset pixels
        for (int i = 0; i < window.getWidth() * window.getHeight(); i++) {
            window.getPixels()[i] = 0;
        }

        // render tiles
        final Vector2i pixelA = this.boundA.mul(Tile.TILE_WIDTH).toInt();
        final Vector2d boundB = this.getBoundB();
        for (int tileX = (int) this.boundA.getX() - 1; tileX < boundB.getX(); tileX++) {
            for (int tileY = (int) this.boundA.getY(); tileY < boundB.getY() + 1; tileY++) {
                final Tile tile = this.world.getTile(Vector2i.of(tileX, tileY));
                final int xa = tileX * Tile.TILE_WIDTH - pixelA.getX();
                final int ya = tileY * Tile.TILE_WIDTH - pixelA.getY();
                tile.render(window, xa, window.getHeight() - 1 - ya);
            }
        }

        this.page.render(window);
    }

    /**
     * Move this {@link Camera} by the specified transform. This affects {@link #boundA}.
     * @param delta The transform to move by.
     */
    public void transform(final Vector2d delta) {
        this.boundA = this.boundA.add(delta);
    }

    /**
     * Reset this Camera to (0, 0). This affects {@link #boundA}.
     */
    public void reset() {
        this.boundA = Vector2d.of(0, 0);
    }

    @NotNull
    public Vector2d getBoundA() {
        return this.boundA;
    }

    @NotNull
    public Vector2d getBoundB() {
        return this.boundA.add(this.size.toDouble());
    }

}
