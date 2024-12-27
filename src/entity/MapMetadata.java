package entity;

import java.util.List;

public class MapMetadata {
    public Position p1Start;
    public Position p2Start;
    public List<Item> items;

    public static class Position {
        public int col, row;
    }

    public static class Item {
        public String type;
        public Position pos;
    }

}
