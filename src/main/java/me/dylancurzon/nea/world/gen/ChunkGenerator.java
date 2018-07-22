package me.dylancurzon.nea.world.gen;

import java.util.Map;
import me.dylancurzon.nea.util.Vector2i;
import me.dylancurzon.nea.world.World;
import me.dylancurzon.nea.world.tile.Tile;

public interface ChunkGenerator {

    Map<Vector2i, Tile> generate(final World world, final Vector2i chunkPosition);

}
