package me.dylancurzon.nea.terrain.world;

import com.sun.istack.internal.NotNull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.terrain.Terrain;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.entity.Entity;
import me.dylancurzon.nea.terrain.world.gen.ChunkGenerator;
import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.terrain.world.tile.TileTypes;

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
public class World implements Terrain, Tickable {

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
     * Update this {@link World} instance. This involves updating all {@link Tickable} tiles and all currently loaded
     * entities.
     */
    @Override
    public void tick() {}

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
        // TODO: for now, just instantly write
        this.loader.writeChunk(position.getX(), position.getY(), chunk);
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
     * Unload all chunks that are currently cached. The chunks surrounding the player will be almost
     * immediately loaded again from the save file (or generated if not saved).
     */
    public void unloadAllChunks() {
        this.chunks.clear();
    }

    /**
     * Fetches a Tile given its World position.
     *
     * @param position The (World) position of the tile to get.
     * @return the relevant {@link Tile} if the Chunk that contains the Tile is loaded. Otherwise,
     * a new Tile of type {@link TileTypes#UNLOADED}.
     */
    @NotNull
    @Override
    public Optional<Tile> getTile(final Vector2i position) {
        final Vector2i chunkPosition = position.div(CHUNK_WIDTH).floor().toInt();
        final Map<Vector2i, Tile> chunk = this.chunks.get(chunkPosition);
        if (chunk == null) {
            // return an Unloaded, blank tile
            // TODO:
            // instead, we should add it to a "loading queue" on a separate thread so it doesn't
            // affect rendering
//            this.loadOrGenerateChunk(chunkPosition);
//            return new Tile(this, TileTypes.UNLOADED);
            return Optional.empty();
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
