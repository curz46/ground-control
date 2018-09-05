package me.dylancurzon.nea.terrain.world.gen;

import java.util.HashMap;
import java.util.Map;
import me.dylancurzon.nea.util.PerlinNoise;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.World;
import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.terrain.world.tile.TileType;
import me.dylancurzon.nea.terrain.world.tile.TileTypes;

public class RockyWorldGenerator implements ChunkGenerator {

    private final PerlinNoise noise = new PerlinNoise()
        .seed((int) Math.floor(Math.random() * 1000000000));

    @Override
    public Map<Vector2i, Tile> generate(final World world, final Vector2i chunkPosition) {
        final int chunkX = chunkPosition.getX();
        final int chunkY = chunkPosition.getY();
        final Map<Vector2i, Tile> tiles = new HashMap<>();

        for (int cx = 0; cx < World.CHUNK_WIDTH; cx++) {
            for (int cy = 0; cy < World.CHUNK_WIDTH; cy++) {
                final int worldX = chunkX >= 0
                    ? (chunkX * World.CHUNK_WIDTH) + cx
                    : (chunkX * World.CHUNK_WIDTH) - cx;
                final int worldY = chunkY >= 0
                    ? (chunkY * World.CHUNK_WIDTH) + cy
                    : (chunkY * World.CHUNK_WIDTH) - cy;
//                if (worldX == 0 || worldY == 0) {
//                    tiles.put(Vector2i.of(cx, cy), new Tile(world, TileTypes.UNLOADED));
//                    continue;
//                }
                final double value = this.noise.generateOctaveNoiseValue(worldX * 100, worldY * 100);

                final TileType type;
                if (value >= 0.35) {
                    type = TileTypes.GRASS;
                } else {
                    type = TileTypes.WATER;
                }

                final Tile tile = new Tile(type);
                tiles.put(Vector2i.of(cx, cy), tile);
            }
        }

        return tiles;
    }

}
