package me.dylancurzon.nea.world;

import com.sun.istack.internal.NotNull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.dylancurzon.nea.util.Vector2i;

/**
 * This {@link World} class contains and oversees the {@link Tile} and {@link Entity} instances and
 * any other attribute of the game world. The World is an infinite, procedurally-generated Tile map
 * with a list of entities and other attributes such as time, lifetime, etc. Tiles are contained in
 * Chunks, 16x16 Tile maps, generated in their entirety when requested. With regard to World
 * storage, the file organization will likely have this format:
 * /worlds
 * |- /[world-id]
 *    |- /chunks
 *    |- attrib
 * |- /...
 */
public class World {

    public static final int CHUNK_WIDTH = 16;
    private final Map<Vector2i, Map<Vector2i, Tile>> tiles = new HashMap<>();

    private final String id;
    private final WorldLoader loader;

    /**
     * @param id The unique string identifier of this World. This is used both internally and
     * externally; it determines the path that the chunks shall be stored in, and allows users to
     * determine which World they wish to load.
     */
    public World(final Path savePath, final String id) {
        this.id = id;
        this.loader = new WorldLoader(savePath, this);
    }

    @NotNull
    public Optional<Tile> getTile(final Vector2i position) {
        final Vector2i chunkPosition = position.integerDiv(CHUNK_WIDTH);
        final Map<Vector2i, Tile> chunk = this.tiles.get(chunkPosition);
        if (chunk == null) {
            // TODO:
            // - if chunk isn't present, then either unloaded or awaits generation
            // - this method shouldn't implicitly generate chunks; it should return some kind of
            //   placeholder tile
            return Optional.empty();
        }
        if (!chunk.containsKey(position)) {
            // if the Tile position doesn't exist, we've failed to completely generate the chunk
            // before adding it to the map
            throw new RuntimeException(
                "The chunk that contains this position is only partially loaded.");
        }
        return Optional.of(chunk.get(position));
    }

    public String getId() {
        return this.id;
    }

}
