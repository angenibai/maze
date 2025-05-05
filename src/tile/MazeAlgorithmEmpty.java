package tile;

public class MazeAlgorithmEmpty implements MazeAlgorithm {
    @Override
    public MazeCell[][] generateMaze(int numRows, int numCols) {
        return MazeAlgorithm.generateEmptyMaze(numRows, numCols);
    }
}
