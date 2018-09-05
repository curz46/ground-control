package me.dylancurzon.nea.terrain.world;

import com.sun.istack.internal.NotNull;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.terrain.world.tile.TileType;
import me.dylancurzon.nea.terrain.world.tile.TileTypes;

@Immutable
public class WorldLoader {

    @NotNull
    private final Path savePath;
    @NotNull
    private final World world;
    @NotNull
    private final Path worldPath;
    @NotNull
    private final Path chunksPath;

    public WorldLoader(@NotNull final Path savePath, @NotNull final World world) {
        this.savePath = savePath;
        this.world = world;
        this.worldPath = this.savePath.resolve(Paths.get("worlds", this.world.getId()));
        this.chunksPath = this.worldPath.resolve("chunks");
    }

    public void writeChunk(@NotNull final int chunkX, @NotNull final int chunkY, @NotNull final Map<Vector2i, Tile> tiles) {
        final File file = this.getChunkPath(chunkX, chunkY).toFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException e) {
                throw new RuntimeException("Failed to create the chunk file: ", e);
            }
        }
        final BufferedOutputStream out;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("The file we just created doesn't exist... wait, what? - ", e);
        }
        final byte[] rawBuf = new byte[16 * 16]; // TODO: as Tile information grows, this needs to grow too
        final ByteBuf buf = new ByteBuf(ByteBuffer.wrap(rawBuf));
        for (int cx = 0; cx < World.CHUNK_WIDTH; cx++) {
            for (int cy = 0; cy < World.CHUNK_WIDTH; cy++) {
                final Tile tile = tiles.get(Vector2i.of(cx, cy));
                buf.writeByte((byte) tile.getType().getId());
            }
        }
        try {
            out.write(rawBuf);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to write to File: ", e);
        }

        try {
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
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
            final int available = in.available();
            final byte[] buf = new byte[available];
            in.read(buf, 0, available);
            buffer = ByteBuffer.wrap(buf);
        } catch (final IOException e) {
            throw new RuntimeException("Exception occurred when loading bytes from file: ", e);
        }
        final ByteBuf buf = new ByteBuf(buffer);
//        buf.flip();

        final Map<Vector2i, Tile> tiles = new HashMap<>();
        for (int cx = 0; cx < World.CHUNK_WIDTH; cx++) {
            for (int cy = 0; cy < World.CHUNK_WIDTH; cy++) {
                // TODO:
                // for now, every tile consists of:
                // - byte; tile type id
                final Tile tile = new Tile(TileTypes.UNLOADED);

                final int typeId = buf.readByte();
                final Optional<TileType> tileType = TileTypes.getType(typeId);
                if (tileType.isPresent()) {
                    tile.setType(tileType.get());
                } else {
                    // TODO: add proper logging
                    System.out.println(
                        "WARNING: chunk contains unrecognized tile type id: " + typeId);
                }

                tiles.put(Vector2i.of(cx, cy), tile);
            }
        }

        try {
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return Optional.of(tiles);
    }

    @NotNull
    private Path getChunkPath(@NotNull final int chunkX, @NotNull final int chunkY) {
        return this.chunksPath.resolve(chunkX + "," + chunkY);
    }

}
