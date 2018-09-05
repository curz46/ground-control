package me.dylancurzon.nea.terrain.world.gen;

import java.util.Map;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.terrain.world.World;
import me.dylancurzon.nea.terrain.world.tile.Tile;

public interface ChunkGenerator {

    Map<Vector2i, Tile> generate(final World world, final Vector2i chunkPosition);

}
