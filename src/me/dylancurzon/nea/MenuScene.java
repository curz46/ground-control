package me.dylancurzon.nea;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import me.dylancurzon.nea.gfx.PixelContainer;
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
import me.dylancurzon.nea.gfx.text.TextTypes;
import me.dylancurzon.nea.util.Vector2i;

public class MenuScene implements Scene {

    private static final BiFunction<String, Consumer<MutableElement>, Function<ImmutableContainer, ImmutableElement>> BUTTON =
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
    private static final PageTemplate TEMPLATE = PageTemplate.builder()
        .setPosition(Vector2i.of(1, 8))
        .setSize(Vector2i.of(398, 231))
        .add(page -> ImmutableContainer.builder()
            .setSize(page.getSize())
            .setCentering(true)
            .add(ctr -> LayoutImmutableContainer.builder()
                .setSize(Vector2i.of(ctr.getSize().getX() / 3, ctr.getSize().getY() - 50))
                .setCentering(true)
                .add(1, BUTTON.apply("play", mut -> System.out.println("playegd")))
                .add(1, BUTTON.apply("options", mut -> System.out.println("options")))
                .add(1, BUTTON.apply("quit", mut -> System.out.println("just quit")))
                .build())
            .build())
        .build();
    private final Stack<Page> pages = new Stack<>();

    private final MenuBackground background;

    public MenuScene(final int width, final int height) {
        this.pages.push(TEMPLATE.asMutable());
        this.background = new MenuBackground(width, height);
    }

    @Override
    public void render(final PixelContainer window) {
        this.background.render(window);
//        this.pages.forEach(page -> page.render(window));
    }

    @Override
    public void setMousePosition(final Vector2i pos) {
        this.pages.forEach(page -> page.setMousePosition(pos));
    }

    @Override
    public void click(final Vector2i pos) {
        this.pages.forEach(page -> page.click(pos));
    }

    @Override
    public void scroll(final double amount) {
        this.pages.forEach(page -> page.scroll(amount));
    }

    @Override
    public void tick() {
        this.pages.forEach(Page::tick);
        this.background.tick();
    }

}
