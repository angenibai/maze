package entity;

import tile.Coord;

import java.util.List;

public class MapMetadata {
    public Coord p1Start;
    public Coord p2Start;
    public List<Item> items;

    public static class Item {
        public String type;
        public Coord pos;

        public Item(String type, Coord itemCoord) {
            this.type = type;
            this.pos = itemCoord;
        }
    }

}
