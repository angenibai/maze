package tile;

import java.util.ArrayList;
import java.util.List;

public interface MazeAlgorithm {
    public MazeCell[][] generateMaze(int numRows, int numCols);

    public static MazeCell[][] generateEmptyMaze(int numRows, int numCols) {
        MazeCell[][] newMaze = new MazeCell[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                MazeCell newCell = new MazeCell();
                newCell.setPosition(new Coord(i, j));
                newMaze[i][j] = newCell;
            }
        }
        return newMaze;
    }

    public default List<Coord> getNeighbourCoords(Coord coord, int numRows, int numCols) {
        List<Coord> neighbours = new ArrayList<>();

        if (coord.r > 0) {
            neighbours.add(new Coord(coord.r - 1, coord.c));
        }

        if (coord.r < numRows - 1) {
            neighbours.add(new Coord(coord.r + 1, coord.c));
        }

        if (coord.c > 0) {
            neighbours.add(new Coord(coord.r, coord.c - 1));
        }

        if (coord.c < numCols - 1) {
            neighbours.add(new Coord(coord.r, coord.c + 1));
        }

        return neighbours;
    }

    public static void printMaze(MazeCell[][] cells) {
        int numRows = cells.length;
        int numCols = cells[0].length;

        for (int r = 0; r < numRows * 2 + 1; r++) {
            for (int c = 0; c < numCols * 2 + 1; c++) {
                boolean isWall = true;

                // check horizontal tunnels
                if (r > 0 && r < numRows * 2 && r % 2 == 1) {
                    if (c > 1) {
                        // check if the cell on the west side extends into the east
                        int cellR = (r - 1) / 2;
                        int cellC = (c - 2) / 2;
                        if (cells[cellR][cellC].isDirection("E")) {
                            isWall = false;
                        }
                    }

                    if (c < numCols * 2 - 1) {
                        // check if the cell on the east side extends into the west
                        int cellR = (r - 1) / 2;
                        int cellC = (c + 1) / 2;
                        if (cells[cellR][cellC].isDirection("W")) {
                            isWall = false;
                        }
                    }
                }

                // check vertical tunnels
                if (c > 0 && c < numCols * 2 && c % 2 == 1) {
                    if (r > 1) {
                        // check if the cell on the north side extends into the south
                        int cellR = (r - 2) / 2;
                        int cellC = (c - 1) / 2;
                        if (cells[cellR][cellC].isDirection("S")) {
                            isWall = false;
                        }
                    }

                    if (r < numRows * 2 - 1) {
                        // check if the cell on the south side extends into the north
                        int cellR = (r + 1) / 2;
                        int cellC = (c - 1) / 2;
                        if (cells[cellR][cellC].isDirection("N")) {
                            isWall = false;
                        }
                    }
                }

                System.out.printf(isWall ? "X " : "  ");
            }
            System.out.printf("\n");
        }
    }
}
