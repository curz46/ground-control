package me.dylancurzon.nea.world;

import com.sun.istack.internal.NotNull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.entity.Entity;
import me.dylancurzon.nea.world.gen.ChunkGenerator;
import me.dylancurzon.nea.world.tile.Tile;
import me.dylancurzon.nea.world.tile.TileTypes;

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
@Immutable
public class World {

    public static final int CHUNK_WIDTH = 16;
    private final Map<Vector2i, Map<Vector2i, Tile>> chunks = new HashMap<>();

    @NotNull
    private final WorldLoader loader;
    @NotNull
    private final String id;
    @NotNull
    private final ChunkGenerator generator;

    /**
     * @param id The unique string identifier of this World. This is used both internally and
     * externally; it determines the path that the chunks shall be stored in, and allows users to
     * determine which World they wish to load.
     * @param generator The {@link ChunkGenerator} for this {@link World} to use when generating
     * chunks.
     */
    public World(final String id, final ChunkGenerator generator, final Path savePath) {
        this.id = id;
        this.generator = generator;
        this.loader = new WorldLoader(savePath, this);
    }

    /**
     * Load a Chunk given its Chunk position. If the Chunk position is not yet generated,
     * call the {@link ChunkGenerator#generate(World, Vector2i)} method and return the result.
     *
     * @param position The (Chunk) position of the Chunk to load.
     * @return {@code false} if the Chunk was loaded from save or {@code true} if the Chunk was generated.
     */
    public boolean loadOrGenerateChunk(final Vector2i position) {
        final boolean loaded = this.loadChunk(position);
        if (loaded) return false;
        final Map<Vector2i, Tile> chunk = this.generator.generate(this, position);
        this.chunks.put(position, chunk);
        return true;
    }

    /**
     * Load a Chunk given its {@link World} position. If the Chunk is already loaded, does nothing.
     * This method utilizes the {@link WorldLoader#loadChunk(int, int)} method.
     *
     * @param position The (Chunk) position of the Chunk to load.
     * @return {@code true} if the Chunk is already loaded or was loaded.
     */
    public boolean loadChunk(final Vector2i position) {
        if (this.chunks.containsKey(position)) return true;
        final Optional<Map<Vector2i, Tile>> chunk = this.loader.loadChunk(
            position.getX(),
            position.getY()
        );
        if (!chunk.isPresent()) return false;
        this.chunks.put(position, chunk.get());
        return true;
    }

    /**
     * Fetches a Tile given its World position.
     *
     * @param position The (World) position of the tile to get.
     * @return the relevant {@link Tile} if the Chunk that contains the Tile is loaded. Otherwise,
     * a new Tile of type {@link TileTypes#UNLOADED}.
     */
    @NotNull
    public Optional<Tile> getTile(final Vector2i position) {
        final Vector2i chunkPosition = position.integerDiv(CHUNK_WIDTH);
        final Map<Vector2i, Tile> chunk = this.chunks.get(chunkPosition);
        if (chunk == null) {
            // return an Unloaded, blank tile
            this.loadOrGenerateChunk(chunkPosition);
            return Optional.of(new Tile(this));
        }
        final Vector2i relativePosition = position
            .sub(chunkPosition.mul(CHUNK_WIDTH))
            .abs();

        if (!chunk.containsKey(relativePosition)) {
            // if the Tile position doesn't exist, we've failed to completely generate the chunk
            // before adding it to the map
            throw new RuntimeException(
                "The chunk that contains this position is only partially loaded.");
        }
        return Optional.of(chunk.get(relativePosition));
    }

    public String getId() {
        return this.id;
    }

}
