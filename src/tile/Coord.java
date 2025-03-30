package tile;

import java.util.Objects;

public class Coord {
    public int r;
        public int c;

        public Coord(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }

            if (!(other instanceof Coord)) {
                return false;
            }

            Coord otherCoord = (Coord) other;

            return this.r == otherCoord.r && this.c == otherCoord.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", r, c);
        }
}
