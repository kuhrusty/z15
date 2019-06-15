package com.kuhrusty.z15.model;

import java.util.List;

/**
 * You know, every time I name a class "Map," I regret it.  This stores the
 * arrangement of the tiles needed for setting up a scenario.
 */
public class Map {
    public Map(List<List<Tile>> tiles) {
        //  first pass, figure out the length of the longest row
        for (int row = 0; row < tiles.size(); ++row) {
            if (width < tiles.get(row).size()) width = tiles.get(row).size();
        }
        //  now that we know how big to make each array, go back & do it
        this.tiles = new Tile[tiles.size()][];
        for (int row = 0; row < tiles.size(); ++row) {
            this.tiles[row] = new Tile[width];
            List<Tile> tl = tiles.get(row);
            for (int col = 0; col < tl.size(); ++col) {
                this.tiles[row][col] = tl.get(col);  //  may be null
            }
        }
    }

    private int width;
    private Tile[][] tiles;

    /**
     * Returns the width of the map, in tiles.
     */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the height of the map, in tiles.
     */
    public int getHeight() {
        return tiles.length;
    }
    /**
     * Returns the tile with the given coordinates, or null.  (0, 0) is the
     * lower left corner.
     */
    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }
}
