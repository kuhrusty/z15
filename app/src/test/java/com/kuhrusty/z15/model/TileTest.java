package com.kuhrusty.z15.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileTest {
    @Test
    public void testRotation() {
        Tile tt = Tile.getTile("1-A");
        checkDirections(tt, true, false, true, false);
        checkRotation(tt);

        tt = Tile.getTile("7-A");
        checkDirections(tt, true, false, false, false);
        checkRotation(tt);

        tt = Tile.getTile("15-A");
        checkDirections(tt, true, false, false, true);
        checkRotation(tt);

        tt = Tile.getTile("18-A");
        checkDirections(tt, true, false, true, true);
        checkRotation(tt);

        tt = Tile.getTile("9-A");
        checkDirections(tt, true, true, true, true);
        checkRotation(tt);
    }

    private void checkDirections(Tile tile, boolean expectNorth,
                                 boolean expectEast, boolean expectSouth,
                                 boolean expectWest) {
        assertEquals(expectNorth, tile.hasRoad(Tile.Direction.North));
        assertEquals(expectEast, tile.hasRoad(Tile.Direction.East));
        assertEquals(expectSouth, tile.hasRoad(Tile.Direction.South));
        assertEquals(expectWest, tile.hasRoad(Tile.Direction.West));
    }

    private void checkRotation(Tile tile) {
        assertEquals(Tile.Direction.North, tile.getRotation());
        boolean hadN = tile.hasRoad(Tile.Direction.North);
        boolean hadE = tile.hasRoad(Tile.Direction.East);
        boolean hadS = tile.hasRoad(Tile.Direction.South);
        boolean hadW = tile.hasRoad(Tile.Direction.West);

        //  N -> E
        tile.setRotation(Tile.Direction.East);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.North));

        //  E -> N
        tile.setRotation(Tile.Direction.North);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.West));

        //  N -> S
        tile.setRotation(Tile.Direction.South);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.East));

        //  S -> N
        tile.setRotation(Tile.Direction.North);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.West));

        //  N -> W
        tile.setRotation(Tile.Direction.West);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.South));

        //  W -> N
        tile.setRotation(Tile.Direction.North);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.West));

        //  E -> S
        tile.setRotation(Tile.Direction.East);
        tile.setRotation(Tile.Direction.South);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.East));

        //  S -> E
        tile.setRotation(Tile.Direction.East);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.North));

        //  E -> W
        tile.setRotation(Tile.Direction.West);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.South));

        //  W -> E
        tile.setRotation(Tile.Direction.East);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.North));

        //  S -> W
        tile.setRotation(Tile.Direction.South);
        tile.setRotation(Tile.Direction.West);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.South));

        //  W -> S
        tile.setRotation(Tile.Direction.South);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.West));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.East));

        //  and put it back how it was when we came in here
        tile.setRotation(Tile.Direction.North);
        assertEquals(hadN, tile.hasRoad(Tile.Direction.North));
        assertEquals(hadE, tile.hasRoad(Tile.Direction.East));
        assertEquals(hadS, tile.hasRoad(Tile.Direction.South));
        assertEquals(hadW, tile.hasRoad(Tile.Direction.West));
    }
}
