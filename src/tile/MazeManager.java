package tile;

import entity.MapMetadata;
import item.VisionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeManager {
    public MazeCell[][] cells;
    public int[][] screenTiles;
    public MazeAlgorithm algorithm;
    public int mazeRows;
    public int mazeCols;
    public final Random random;
    // these coords line up with screen positions
    public Coord p1Start;
    public Coord p2Start;
    public List<MapMetadata.Item> items;

    public MazeManager(int screenRows, int screenCols) {
        setScreenDimensions(screenRows, screenCols);
        this.algorithm = new MazeAlgorithmEmpty();
        this.random = new Random();
    }

    public MazeManager(int screenRows, int screenCols, MazeAlgorithmOption algorithmOption) {
        setScreenDimensions(screenRows, screenCols);
        setAlgorithm(algorithmOption);
        this.random = new Random();
    }

    public static void main(String[] args) {
        MazeManager mazeManager = new MazeManager(13, 25, MazeAlgorithmOption.GROWING_TREE);
        mazeManager.setup();
        MazeAlgorithm.printMaze(mazeManager.cells);
    }

    public void setup() {
        this.cells = this.algorithm.generateMaze(this.mazeRows, this.mazeCols);
        loadMazeForScreen();
    }

    public void reset() {
        setup();
    }

    public void setScreenDimensions(int screenRows, int screenCols) {
        assert screenRows % 2 == 1 && screenCols % 2 == 1;
        this.mazeRows = screenToMazeIdx(screenRows);
        this.mazeCols = screenToMazeIdx(screenCols);
    }

    public void setAlgorithm(MazeAlgorithmOption option) {
        switch (option) {
            case EMPTY -> this.algorithm = new MazeAlgorithmEmpty();
            case GROWING_TREE -> this.algorithm = new MazeAlgorithmGrowingTree();
        }
    }

    /**
     * Sets up maze for rendering on screen
     * Sets player start coordinates as well
     * TODO: set power ups as well
     */
    public void loadMazeForScreen() {
        setStartCoords();

        int screenRows = this.mazeRows * 2 + 1;
        int screenCols = this.mazeCols * 2 + 1;

        // note screen is represented in an x/y format
        int[][] screenTiles = new int[screenCols][screenRows];

        for (int r = 0; r < screenRows; r++) {
            for (int c = 0; c < screenCols; c++) {
                boolean isWall = true;

                // check horizontal tunnels
                if (r < this.mazeRows * 2 && r % 2 == 1) {
                    if (c > 1) {
                        // check if the cell on the west side extends into the east
                        int cellR = (r - 1) / 2;
                        int cellC = (c - 2) / 2;
                        if (this.cells[cellR][cellC].isDirection("E")) {
                            isWall = false;
                        }
                    }

                    if (c < this.mazeCols * 2 - 1) {
                        // check if the cell on the east side extends into the west
                        int cellR = (r - 1) / 2;
                        int cellC = (c + 1) / 2;
                        if (this.cells[cellR][cellC].isDirection("W")) {
                            isWall = false;
                        }
                    }
                }

                // check vertical tunnels
                if (c < this.mazeCols * 2 && c % 2 == 1) {
                    if (r > 1) {
                        // check if the cell on the north side extends into the south
                        int cellR = (r - 2) / 2;
                        int cellC = (c - 1) / 2;
                        if (this.cells[cellR][cellC].isDirection("S")) {
                            isWall = false;
                        }
                    }

                    if (r < this.mazeRows * 2 - 1) {
                        // check if the cell on the south side extends into the north
                        int cellR = (r + 1) / 2;
                        int cellC = (c - 1) / 2;
                        if (this.cells[cellR][cellC].isDirection("N")) {
                            isWall = false;
                        }
                    }
                }

                screenTiles[c][r] = isWall ? Tile.WALL : Tile.PATH;
            }
        }

        this.screenTiles = screenTiles;

        setVisionItems();
    }

    private int mazeToScreenIdx(int mazeIdx) {
        return mazeIdx * 2 + 1;
    }

    private int screenToMazeIdx(int screenIdx) {
        return (screenIdx - 1) / 2;
    }

    private void setStartCoords() {
        if (random.nextInt(2) == 0) {
            // pick start points at top and bottom rows
            int topCol = random.nextInt(this.mazeCols);
            int bottomCol = random.nextInt(this.mazeCols);

            // record start coord and open up start point
            this.p1Start = new Coord(mazeToScreenIdx(0) - 1, mazeToScreenIdx(topCol)); // change this
            this.cells[0][topCol].setDirection("N", true);

            this.p2Start = new Coord(mazeToScreenIdx(this.mazeRows - 1) + 1, mazeToScreenIdx(bottomCol));
            this.cells[this.mazeRows-1][bottomCol].setDirection("S", true);
        } else {
            int leftRow = random.nextInt(this.mazeRows);
            int rightRow = random.nextInt(this.mazeRows);

            this.p1Start = new Coord(mazeToScreenIdx(leftRow), mazeToScreenIdx(0) - 1);
            this.cells[leftRow][0].setDirection("W", true);

            this.p2Start = new Coord(mazeToScreenIdx(rightRow), mazeToScreenIdx(this.mazeCols - 1) + 1);
            this.cells[rightRow][this.mazeCols-1].setDirection("E", true);
        }
    }

    private void setVisionItems() {
        this.items = new ArrayList<>();
        int nItems = 3; // currently hardcoded

        int screenRows = mazeToScreenIdx(this.mazeRows);
        int screenCols = mazeToScreenIdx(this.mazeCols);
        System.out.println("rows: " + screenRows + " cols: " + screenCols);

        if (this.p1Start.r == 0) {
            // players are top to bottom - divide map into three horizontal slices

            int stepSize = screenRows / nItems;
            int overlapSize = screenRows % nItems;
            int windowSize = stepSize + overlapSize;

            int rStart = 0;
            System.out.println("top to bottom");
            System.out.println("stepSize: " + stepSize + " overlapSize: " + overlapSize + " windowSize " + windowSize);

            while (rStart <= screenRows - windowSize) {
                System.out.println("rStart: " + rStart);
                Coord itemCoord = randomPathTileInWindow(
                        new Coord(rStart, 0),
                        new Coord(rStart + windowSize, screenCols - 1)
                );
                if (itemCoord != null) {
                    // todo: do better than just a string
                    this.items.add(new MapMetadata.Item("vision", itemCoord));
                }
                rStart += stepSize;
            }

        } else {
            // players are left to right

            int stepSize = screenCols / nItems;
            int overlapSize = screenCols % nItems;
            int windowSize = stepSize + overlapSize;

            int cStart = 0;
            System.out.println("left to right");
            System.out.println("stepSize: " + stepSize + " overlapSize: " + overlapSize + " windowSize " + windowSize);

            while (cStart <= screenCols - windowSize) {
                System.out.println("cStart: " + cStart);
                Coord itemCoord = randomPathTileInWindow(
                        new Coord(0, cStart),
                        new Coord(screenRows - 1, cStart + windowSize)
                );
                if (itemCoord != null) {
                    items.add(new MapMetadata.Item("vision", itemCoord));
                }
                cStart += stepSize;
            }
        }

    }

    /**
     * Given corner coords of a window in the maze screen tiles, get a random path tile and return the coords
     */
    private Coord randomPathTileInWindow(Coord topLeft, Coord bottomRight) {
        List<Coord> allPathTiles = new ArrayList<>();

        for (int r = topLeft.r; r < bottomRight.r; r++) {
            for (int c = topLeft.c; c < bottomRight.c; c++) {
                if (this.screenTiles[c][r] == Tile.PATH) {
                    allPathTiles.add(new Coord(r, c));
                }
            }
        }

        if (allPathTiles.isEmpty()) {
            return null;
        }

        return allPathTiles.get(random.nextInt(allPathTiles.size()));
    }

    private void setItems() {
        // hardcoded to 3 items
        // split map into 3 sections and place one item in each
    }
}
