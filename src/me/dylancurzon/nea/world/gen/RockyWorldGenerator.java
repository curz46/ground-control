package me.dylancurzon.nea.world.gen;

import java.util.HashMap;
import java.util.Map;
import me.dylancurzon.nea.util.PerlinNoise;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.tile.Tile;
import me.dylancurzon.nea.world.tile.TileType;
import me.dylancurzon.nea.world.tile.TileTypes;

public class RockyWorldGenerator implements ChunkGenerator {

    private final PerlinNoise noise = new PerlinNoise();

    @Override
    public Map<Vector2i, Tile> generate(final World world, final Vector2i chunkPosition) {
        // TODO: in the future, add an option to generate the World based on a seed
        final int seed = (int) Math.floor(Math.random() * 1000000000);

        final Map<Vector2i, Tile> tiles = new HashMap<>();

        for (int cx = 0; cx < World.CHUNK_WIDTH; cx++) {
            for (int cy = 0; cy < World.CHUNK_WIDTH; cy++) {
                final int worldX = (chunkPosition.getX() * 8) + cx;
                final int worldY = (chunkPosition.getY() * 8) + cy;
                final double value = this.noise.generatePerlinValue(worldX, worldY, 1);

                final TileType type;
                // TODO: not sure about the range of this utility class, check later
                if (value >= 0.5) {
                    type = TileTypes.GRASS;
                } else {
                    type = TileTypes.STONE;
                }

                final Tile tile = new Tile(world);
                tile.setType(type);
                tiles.put(Vector2i.of(cx, cy), tile);
            }
        }

        return tiles;
    }

}
