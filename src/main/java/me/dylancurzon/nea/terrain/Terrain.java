package me.dylancurzon.nea.terrain;

import me.dylancurzon.nea.terrain.world.tile.Tile;
import me.dylancurzon.nea.util.Vector2i;

import java.util.Optional;

public interface Terrain {

    /**
     * If a Tile at the given position exists, returns it.
     * @param position the Tile position to query.
     * @return the Tile, if it exists
     */
    Optional<Tile> getTile(final Vector2i position);

}
