package tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class MazeGenerator {
    private Cell cells[][];
    private int numRows;
    private int numCols;
    private Random random;

    public MazeGenerator(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.random = new Random();
        this.cells = generateMazeGrowingTree(numRows, numCols);
    }

    public static void main(String[] args) {
        System.out.println("yuh");
        MazeGenerator mazeGen = new MazeGenerator(5, 7);
        mazeGen.printCells(mazeGen.cells);
    }

    private void printCells(Cell[][] cells) {
        for (int r = 0; r < numRows * 2 + 1; r++) {
            for (int c = 0; c < numCols * 2 + 1; c++) {
                boolean isWall = true;

                // check horizontal tunnels
                if (r > 0 && r < numRows * 2 && r % 2 == 1) {
                    if (c > 1) {
                        // check if the cell on the west side extends into the east
                        int cellR = (r - 1) / 2;
                        int cellC = (c - 2) / 2;
                        if (cells[cellR][cellC].E) {
                            isWall = false;
                        }
                    }

                    if (c < numCols * 2 - 1) {
                        // check if the cell on the east side extends into the west
                        int cellR = (r - 1) / 2;
                        int cellC = (c + 1) / 2;
                        if (cells[cellR][cellC].W) {
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
                        if (cells[cellR][cellC].S) {
                            isWall = false;
                        }
                    }

                    if (r < numRows * 2 - 1) {
                        // check if the cell on the south side extends into the north
                        int cellR = (r + 1) / 2;
                        int cellC = (c - 1) / 2;
                        if (cells[cellR][cellC].N) {
                            isWall = false;
                        }
                    }
                }

                System.out.printf(isWall ? "X " : "  ");
            }
            System.out.printf("\n");
        }
    }

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

    public class Cell {
        public boolean N;
        public boolean S;
        public boolean E;
        public boolean W;
        public Coord position;

        public Cell() {
            clearCell();
        }

        public Cell(String direction) {
            clearCell();
            setDirection(direction, true);
        }

        public Cell(String[] directions) {
            clearCell();
            for (String d : directions) {
                setDirection(d, true);
            }
        }

        @Override
        public String toString() {
            return this.position.toString();
        }

        private void clearCell() {
            this.N = false;
            this.S = false;
            this.E = false;
            this.W = false;
        }

        public void setPosition(Coord coord) {
            this.position = coord;
        }

        public void setDirection(String direction, boolean isTrue) {
            switch (direction) {
                case "N":
                    this.N = isTrue;
                    break;
                case "S":
                    this.S = isTrue;
                    break;
                case "E":
                    this.E = isTrue;
                    break;
                case "W":
                    this.W = isTrue;
                    break;
                default:
                    break;
            }
        }

        public void setOpposite(String direction, boolean isTrue) {
            switch (direction) {
                case "N":
                    this.S = isTrue;
                    break;
                case "S":
                    this.N = isTrue;
                    break;
                case "E":
                    this.W = isTrue;
                    break;
                case "W":
                    this.E = isTrue;
                    break;
                default:
                    break;
            }
        }

        public boolean isDirection(String direction) {
            boolean isTrue = false;

            switch (direction) {
                case "N":
                    isTrue = this.N;
                    break;
                case "S":
                    isTrue = this.S;
                    break;
                case "E":
                    isTrue = this.E;
                    break;
                case "W":
                    isTrue = this.W;
                    break;
                default:
                    break;
            }

            return isTrue;
        }

        public void carvePassageTo(Cell neighbour) {
            Coord neighbourCoord = neighbour.position;
            if (neighbourCoord.r == this.position.r + 1) {
                setDirection("S", true);
            } else if (neighbourCoord.r == this.position.r - 1) {
                setDirection("N", true);
            } else if (neighbourCoord.c == this.position.c + 1) {
                setDirection("E", true);
            } else if (neighbourCoord.c == this.position.c - 1) {
                setDirection("W", true);
            }
        }
    }

    private Cell[][] generateEmptyMaze(int numRows, int numCols) {
        Cell[][] newMaze = new Cell[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell newCell = new Cell();
                newCell.setPosition(new Coord(i, j));
                newMaze[i][j] = newCell;
            }
        }
        return newMaze;
    }

    public Cell[][] generateMazeGrowingTree(int numRows, int numCols) {
        // growing tree algorithm
        // https://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm
        // later refactor this into strategy pattern
        Cell[][] newMaze = generateEmptyMaze(numRows, numCols);

        Set<Coord> visitedCoords = new HashSet<>();
        List<Cell> toCarve = new ArrayList<Cell>();
        Cell startCell = getRandomCell(newMaze, numRows, numCols);
        toCarve.add(startCell);
        visitedCoords.add(startCell.position);

        int count = 0;

        while (!toCarve.isEmpty() && count < 40) {
            // pick a cell from toCarve
            Cell curCell = pickNextCell(toCarve);
            System.out.println(curCell);

            // pick a random unvisited neighbour
            List<Coord> neighbourCoords = getNeighbourCoords(curCell);
            List<Coord> unvisitedNeighbours = neighbourCoords.stream()
                    .filter(coord -> !visitedCoords.contains(coord))
                    .collect(Collectors.toList());

            if (unvisitedNeighbours.isEmpty()) {
                toCarve.remove(curCell);
                continue;
            }

            Collections.shuffle(unvisitedNeighbours);
            Coord neighbourCoord = unvisitedNeighbours.getFirst();
            Cell neighbourCell = newMaze[neighbourCoord.r][neighbourCoord.c];

            // carve passage between cell and neighbour
            curCell.carvePassageTo(neighbourCell);
            neighbourCell.carvePassageTo(curCell);

            // mark as visited and add onto to carve list
            visitedCoords.add(neighbourCoord);
            toCarve.add(neighbourCell);
            count++;
            this.printCells(newMaze);
            System.out.println("");
        }

        return newMaze;
    }

    private Cell getRandomCell(Cell[][] maze, int numRows, int numCols) {
        // maybe in the future this can just be one maze object that includes
        // both cell info and num rows and cols info
        int r = random.nextInt(numRows);
        int c = random.nextInt(numCols);
        return maze[r][c];
    }

    private Cell pickNextCell(List<Cell> cells) {
        // changing how this works will change how the algorithm behaves
        return cells.getLast();
    }

    private List<Coord> getNeighbourCoords(Cell cell) {
        Coord cellPos = cell.position;
        List<Coord> neighbours = new ArrayList<>();

        if (cellPos.r > 0) {
            neighbours.add(new Coord(cellPos.r - 1, cellPos.c));
        }

        if (cellPos.r < this.numRows - 1) {
            neighbours.add(new Coord(cellPos.r + 1, cellPos.c));
        }

        if (cellPos.c > 0) {
            neighbours.add(new Coord(cellPos.r, cellPos.c - 1));
        }

        if (cellPos.c < this.numCols - 1) {
            neighbours.add(new Coord(cellPos.r, cellPos.c + 1));
        }

        return neighbours;
    }

    public void loadMaze() {
        String[][][] cellDirections = {
                { { "W", "S" }, { "S", "E" }, { "W", "S" } },
                { { "N", "E" }, { "W", "N" }, { "N", "S" } }
        };
        // String[][][] cellDirections = {
        // {{"W", "E"}, {"W", "E"}, {"W", "S"}},
        // {{"W", "E"}, {"W", "E"}, {"W", "N"}}
        // };

        this.cells = new Cell[this.numRows][this.numCols];
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                Cell newCell = new Cell(cellDirections[i][j]);
                newCell.setPosition(new Coord(i, j));
                this.cells[i][j] = newCell;
            }
        }
    }
}
