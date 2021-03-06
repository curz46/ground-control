package me.dylancurzon.nea.terrain.level;

import com.sun.istack.internal.NotNull;
import me.dylancurzon.nea.terrain.Terrain;
import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.terrain.world.tile.TileType;
import me.dylancurzon.nea.terrain.world.tile.TileTypes;
import me.dylancurzon.nea.util.ByteBuf;
import me.dylancurzon.nea.util.Vector2i;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Level implements Terrain {

    private final Map<Vector2i, Tile> tileMap;

    public Level() {
        this.tileMap = new HashMap<>();
    }

    public Level(@NotNull final Map<Vector2i, Tile> tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * Levels have a binary representation that is slightly different to that of Worlds -- they don't work with
     * chunks.
     *
     * # tiles written
     * integer; number of tiles
     * # foreach tile
     * integer; X position
     * integer; Y position
     * byte; tile type id
     * TODO: tile metadata
     * # end foreach
     * TODO: entities
     *
     * @param file the File to attempt to load the Level from.
     * @return the loaded Level.
     */
    @NotNull
    public static Level fromFile(@NotNull final File file) {
        BufferedInputStream in = null;
        try {
            try {
                in = new BufferedInputStream(new FileInputStream(file));
            } catch (final FileNotFoundException e) {
                throw new RuntimeException("The File that was passed doesn't exist: ", e);
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

            final Map<Vector2i, Tile> tileMap = new HashMap<>();
            final int numTiles = buf.readInt();

            for (int i = 0; i < numTiles; i++) {
                final int x = buf.readInt();
                final int y = buf.readInt();
                final int id = buf.readByte();
                final Optional<TileType> type = TileTypes.getType(id);
                if (!type.isPresent()) throw new RuntimeException("Level contains unrecognised TileType id: " + id);
                tileMap.put(Vector2i.of(x, y), new Tile(type.get()));
            }

            return new Level(tileMap);
        } finally {
            // Ensure that the InputStream is closed, regardless of Exceptions.
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets a Tile position to the given Tile.
     * @param position the position to set in the Tile map.
     * @param newTile the Tile to set the position to. If this is null, the position is removed from the Tile map.
     * @return the Tile that was previously set at that position, if present.
     */
    @NotNull
    public Optional<Tile> setTile(@NotNull final Vector2i position, final Tile newTile) {
        if (newTile == null) {
            final Tile tile = this.tileMap.remove(position);
            return Optional.ofNullable(tile);
        }

        final Tile tile = this.tileMap.put(position, newTile);
        return Optional.ofNullable(tile);
    }

    @NotNull
    @Override
    public Optional<Tile> getTile(@NotNull final Vector2i position) {
        if (this.tileMap.containsKey(position)) {
            return Optional.of(this.tileMap.get(position));
        }
        return Optional.empty();
    }

    public void save(@NotNull final File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException e) {
                throw new RuntimeException("Failed to create the file: ", e);
            }
        }
        BufferedOutputStream out = null;
        try {
            try {
                out = new BufferedOutputStream(new FileOutputStream(file));
            } catch (final FileNotFoundException e) {
                throw new RuntimeException("The file we just created doesn't exist... wait, what? - ", e);
            }
            // numTiles (4) + numTiles * [x (4) + y (4) + id (1)]
            final byte[] rawBuf = new byte[4 + this.tileMap.size() * 9];
            final ByteBuf buf = new ByteBuf(ByteBuffer.wrap(rawBuf));

            buf.writeInt(this.tileMap.size());
            this.tileMap.forEach((pos, tile) -> {
                buf.writeInt(pos.getX());
                buf.writeInt(pos.getY());
                buf.writeByte((byte) tile.getType().getId());
            });

            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write contents to file: ", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
