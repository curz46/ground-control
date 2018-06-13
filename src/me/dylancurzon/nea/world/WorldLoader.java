package me.dylancurzon.nea.world;

import com.sun.istack.internal.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.dylancurzon.nea.util.ByteBuf;
import me.dylancurzon.nea.util.Vector2i;

@Immutable
public class WorldLoader {

    @NotNull
    private final Path savePath;
    @NotNull
    private final World world;

    private Path worldPath;
    private Path chunksPath;

    public WorldLoader(@NotNull final Path savePath, @NotNull final World world) {
        this.savePath = savePath;
        this.world = world;
        this.initialize();
    }

    @NotNull
    public Optional<Map<Vector2i, Tile>> loadChunk(@NotNull final int chunkX, @NotNull final int chunkY) {
        final File file = this.getChunkPath(chunkX, chunkY).toFile();
        final BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
        } catch (final FileNotFoundException e) {
            // Chunk does not exist
            return Optional.empty();
        }
        final ByteBuffer buffer;
        try {
            final byte[] buf = new byte[in.available()];
            in.read(buf, 0, in.available());
            buffer = ByteBuffer.wrap(buf);
        } catch (final IOException e) {
            throw new RuntimeException("Exception occurred when loading bytes from file: ", e);
        }
        final ByteBuf buf = new ByteBuf(buffer);
        buf.flip();

        final Map<Vector2i, Tile> tiles = new HashMap<>();
        for (int cx = 0; cx < World.CHUNK_WIDTH; cx++) {
            for (int cy = 0; cy < World.CHUNK_WIDTH; cy++) {
                // TODO:
                // for now, every tile consists of:
                // - byte; tile type id
                final Tile tile = new Tile(this.world);

                final int typeId = buf.readByte();
                final Optional<TileType> tileType = TileTypes.getType(typeId);
                if (tileType.isPresent()) {
                    tile.setType(tileType.get());
                } else {
                    // TODO: add proper logging
                    System.out.println(
                        "WARNING: chunk contains unrecognized tile type id: " + typeId);
                }

                tiles.put(Vector2i.of(cx, cy), new Tile(this.world));
            }
        }
        return Optional.of(tiles);
    }

    private void initialize() {
        this.worldPath = this.savePath.resolve(Paths.get("worlds", this.world.getId()));
        this.chunksPath = this.worldPath.resolve("chunks");
    }

    private Path getChunkPath(final int chunkX, final int chunkY) {
        return this.chunksPath.resolve(chunkX + "," + chunkY);
    }

}
