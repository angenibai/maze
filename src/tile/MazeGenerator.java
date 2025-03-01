package tile;

public class MazeGenerator {
    private Cell cells[][];
    private int numRows;
    private int numCols;

    public MazeGenerator(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        loadMaze();
    }
    
    public static void main(String[] args) {
        System.out.println("yuh");
        MazeGenerator mazeGen = new MazeGenerator(2, 3);
        mazeGen.printCells();
    }

    private void printCells() {
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
                if (c > 0 && c < numCols*2 && c % 2 == 1) {
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

    public class Cell {
        public boolean N;
        public boolean S;
        public boolean E;
        public boolean W;

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

        private void clearCell() {
            this.N = false;
            this.S = false;
            this.E = false;
            this.W = false;
        }

        public void setDirection(String direction, boolean isTrue) {
            switch (direction) {
                case "N": this.N = isTrue; break;
                case "S": this.S = isTrue; break;
                case "E": this.E = isTrue; break;
                case "W": this.W = isTrue; break;
                default: break;
            }
        }

        public boolean isDirection(String direction) {
            boolean isTrue = false;

            switch (direction) {
                case "N": isTrue = this.N; break;
                case "S": isTrue = this.S; break;
                case "E": isTrue = this.E; break;
                case "W": isTrue = this.W; break;
                default: break;
            }

            return isTrue;
        }
    }

    public void loadMaze() {
        String[][][] cellDirections = {
            {{"W", "S"}, {"S", "E"}, {"W", "S"}},
            {{"N", "E"}, {"W", "N"}, {"N", "S"}}
        };
        // String[][][] cellDirections = {
        //     {{"W", "E"}, {"W", "E"}, {"W", "S"}},
        //     {{"W", "E"}, {"W", "E"}, {"W", "N"}}
        // };
        
        this.cells = new Cell[this.numRows][this.numCols];
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                this.cells[i][j] = new Cell(cellDirections[i][j]);
            }
        }
    }
}
