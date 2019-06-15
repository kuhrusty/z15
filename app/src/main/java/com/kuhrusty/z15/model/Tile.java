package com.kuhrusty.z15.model;

import com.kuhrusty.z15.R;

import java.util.Comparator;
import java.util.HashMap;

public class Tile {
    public static final Comparator<String> IDStringComparator = new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            return extractNumber(s1) - extractNumber(s2);
        }
    };
    //  very strongly hopes the first character or two are digits.
    private static int extractNumber(String tileID) {
        int rv = tileID.charAt(0) - '0';
        if (tileID.charAt(1) != '-') {
            rv *= 10;
            rv += tileID.charAt(1) - '0';
        }
        return rv;
    }

    public enum Direction {
        North, East, South, West;
        /**
         * Returns the bit flag for this Direction.
         */
        public int getBit() { return 1 << ordinal(); }
    }

    /**
     * Multiple tiles have the same illustration; for example, tile 4-A uses
     * the same illustration as tile 1-A.  This enum lists the distinct
     * illustrations.
     */
    public enum Illustration {
        //  The direction stuff below, showing which edges have roads, could be
        //  here instead of in Tile.
        Tile01a(R.drawable.tile01a),
        Tile02a(R.drawable.tile02a),
        Tile03a(R.drawable.tile03a),
        Tile07a(R.drawable.tile07a),
        Tile08a(R.drawable.tile08a),
        Tile09a(R.drawable.tile09a),
        Tile10a(R.drawable.tile10a),
        Tile11a(R.drawable.tile11a),
        Tile12a(R.drawable.tile12a),
        Tile13a(R.drawable.tile13a),
        Tile14a(R.drawable.tile14a),
        Tile15a(R.drawable.tile15a),
        Tile16a(R.drawable.tile16a),
        Tile18a(R.drawable.tile18a),
        Tile19a(R.drawable.tile19a),
        Tile21a(R.drawable.tile21a),
        Tile22a(R.drawable.tile22a),
        Tile27a(R.drawable.tile27a),
        Tile28a(R.drawable.tile28a),
        Tile29a(R.drawable.tile29a),

        Tile01b(R.drawable.tile01b),
        Tile02b(R.drawable.tile02b),
        Tile03b(R.drawable.tile03b),
        Tile04b(R.drawable.tile04b),
        Tile09b(R.drawable.tile10b),  //  29a, rotated 180
        Tile10b(R.drawable.tile10b),  //  28a, rotated 180
        Tile11b(R.drawable.tile11b),
        Tile12b(R.drawable.tile12b),
        Tile13b(R.drawable.tile13b),
        Tile14b(R.drawable.tile14b),
        Tile15b(R.drawable.tile15b),
        Tile21b(R.drawable.tile21b),  //  16a, rotated 90
        Tile22b(R.drawable.tile22b),  //  15a, rotated 90
        Tile23b(R.drawable.tile23b),
        Tile25b(R.drawable.tile25b),  //  1a, rotated 180
        Tile28b(R.drawable.tile28b),
        Tile30b(R.drawable.tile30b),  //  18a, rotated 180
        Tile31b(R.drawable.tile31b);
        Illustration(int imgResID) {
            this.imgResID = imgResID;
        }
        private final int imgResID;
    }
    private String id;
    private Illustration ill;
    private int roads;
    private int rotation = Direction.North.ordinal();

    private Tile(String id, Illustration ill, int roads) {
        this.id = id;
        this.ill = ill;
        this.roads = roads;
    }
    private Tile(String id, Tile other) {
        if (!other.getRotation().equals(Direction.North)) {
            throw new IllegalArgumentException(other.getID() + " is rotated " +
                    other.getRotation().toString());
        }
        this.id = id;
        this.ill = other.ill;
        this.roads = other.roads;
    }
    public Tile(Tile copyFrom) {
        this.id = copyFrom.id;
        this.ill = copyFrom.ill;
        this.roads = copyFrom.roads;
        this.rotation = copyFrom.rotation;
    }

    private static final HashMap<String, Tile> tileMap = new HashMap<>();
    static {
        addTile("1-A", Illustration.Tile01a, Direction.North, Direction.South);
        addTile("2-A", Illustration.Tile02a, Direction.North, Direction.South);
        addTile("3-A", Illustration.Tile03a, Direction.North, Direction.South);
        addTile("4-A", "1-A");
        addTile("5-A", "2-A");
        addTile("6-A", "3-A");
        addTile("7-A", Illustration.Tile07a, Direction.North);
        addTile("8-A", Illustration.Tile08a, Direction.North);
        addTile("9-A", Illustration.Tile09a, Direction.North, Direction.East, Direction.South, Direction.West);
        addTile("10-A", Illustration.Tile10a, Direction.North, Direction.East, Direction.South, Direction.West);
        addTile("11-A", Illustration.Tile11a, Direction.North);
        addTile("12-A", Illustration.Tile12a, Direction.North);
        addTile("13-A", Illustration.Tile13a, Direction.North);
        addTile("14-A", Illustration.Tile14a, Direction.North);
        addTile("15-A", Illustration.Tile15a, Direction.North, Direction.West);
        addTile("16-A", Illustration.Tile16a, Direction.North, Direction.West);
        addTile("17-A", "16-A");
        addTile("18-A", Illustration.Tile18a, Direction.North, Direction.South, Direction.West);
        addTile("19-A", Illustration.Tile19a, Direction.North, Direction.South, Direction.West);
        addTile("20-A", "19-A");
        addTile("21-A", Illustration.Tile21a, Direction.North);
        addTile("22-A", Illustration.Tile22a, Direction.North);
        addTile("23-A", "21-A");
        addTile("24-A", "22-A");
        addTile("25-A", "21-A");
        addTile("26-A", "22-A");
        addTile("27-A", Illustration.Tile27a, Direction.North, Direction.East, Direction.South);
        addTile("28-A", Illustration.Tile28a, Direction.North, Direction.East, Direction.South);
        addTile("29-A", Illustration.Tile29a, Direction.North, Direction.East, Direction.South);
        addTile("30-A", "27-A");
        addTile("31-A", "7-A");
        addTile("32-A", "8-A");

        addTile("1-B", Illustration.Tile01b, Direction.North, Direction.South);
        addTile("2-B", Illustration.Tile02b, Direction.North, Direction.South);
        addTile("3-B", Illustration.Tile03b, Direction.North, Direction.South);
        addTile("4-B", Illustration.Tile04b, Direction.North, Direction.South);
        addTile("5-B", "3-B");
        addTile("6-B", "2-B");
        addTile("7-B", "4-B");
        addTile("8-B", "1-B");
        addTile("9-B", Illustration.Tile09b, Direction.North, Direction.South, Direction.West);
        addTile("10-B", Illustration.Tile10b, Direction.North, Direction.South, Direction.West);
        addTile("11-B", Illustration.Tile11b, Direction.North, Direction.East);
        addTile("12-B", Illustration.Tile12b, Direction.North, Direction.East);
        addTile("13-B", Illustration.Tile13b, Direction.North, Direction.East);
        addTile("14-B", Illustration.Tile14b, Direction.North, Direction.East);
        addTile("15-B", Illustration.Tile15b, Direction.North, Direction.East);
        addTile("16-B", "11-B");
        addTile("17-B", "12-B");
        addTile("18-B", "13-B");
        addTile("19-B", "14-B");
        addTile("20-B", "15-B");
        addTile("21-B", Illustration.Tile21b, Direction.North, Direction.East);
        addTile("22-B", Illustration.Tile22b, Direction.North, Direction.East);
        addTile("23-B", Illustration.Tile23b, Direction.North, Direction.South);
        addTile("24-B", "8-A");
        addTile("25-B", Illustration.Tile25b, Direction.North, Direction.South);
        addTile("26-B", "9-A");
        addTile("27-B", "12-A");
        addTile("28-B", Illustration.Tile28b, Direction.North);
        addTile("29-B", "11-B");
        addTile("30-B", Illustration.Tile30b, Direction.North, Direction.East, Direction.South);
        addTile("31-B", Illustration.Tile31b, Direction.North, Direction.East, Direction.South, Direction.West);
        addTile("32-B", "31-B");
    }
    private static void addTile(String id, Illustration ill, Direction... dirs) {
        tileMap.put(id, new Tile(id, ill, getBits(dirs)));
    }
    private static void addTile(String id, String copyOfID) {
        if (id.equals(copyOfID)) throw new IllegalArgumentException(copyOfID);
        Tile other = tileMap.get(copyOfID);
        if (other == null) throw new IllegalArgumentException(copyOfID);
        tileMap.put(id, new Tile(id, other));
    }
    private static int getBits(Direction... dirs) {
        int rv = 0;
        for (int ii = 0; ii < dirs.length; ++ii) rv |= dirs[ii].getBit();
        return rv;
    }

    /**
     * Returns the tile with the given ID ("1-A"), or null.  (This is how you
     * get Tile instances.)
     */
    public static Tile getTile(String id) {
        return tileMap.get(id);
    }

    /**
     * "1-A", "30-B", etc.
     */
    public String getID() {
        return id;
    }
    public int getTileImageResID() {
        return ill.imgResID;
    }

    /**
     * Rotate the tile if necessary.  If the tile's previous rotation was
     * East, and you set rotation to South, the tile will be rotated 90 degrees.
     */
    public void setRotation(Direction dir) {
        int newrot = dir.ordinal();
        if (newrot == rotation) return;
        //  there's probably a less-crude way to do this, but I did not try to
        //  figure out what it might be.
        int newRoads = 0;
        int adjustment = ((newrot - rotation) + 4) % 4;
        for (int ii = 0; ii < 4; ++ii) {
            if ((roads & (1 << ii)) != 0) {
                newRoads |= (1 << ((ii + adjustment) % 4));
            }
        }
        roads = newRoads;
        rotation = newrot;
    }

    public Direction getRotation() {
        return Direction.values()[rotation];
    }

    /**
     * Returns the number of sides of this tile which are crossed by roads.
     */
    public int getRoadCount() {
        return Integer.bitCount(roads);
    }

    /**
     * Returns true if the tile has a road on the given edge, given its
     * current rotation.  For example, a tile which only has a road on the
     * North edge, rotated east, will return true for hasRoad(East).
     */
    public boolean hasRoad(Direction direction) {
        return (roads & direction.getBit()) != 0;
    }
}
