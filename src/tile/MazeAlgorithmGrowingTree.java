package tile;

import java.util.*;

/**
 * Growing tree algorithm
 * https://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm
 */
public class MazeAlgorithmGrowingTree implements MazeAlgorithm {
    private final Random random = new Random();

    @Override
    public MazeCell[][] generateMaze(int numRows, int numCols) {
        MazeCell[][] newMaze = MazeAlgorithm.generateEmptyMaze(numRows, numCols);

        Set<Coord> visitedCoords = new HashSet<>();
        List<Coord> toVisit = new ArrayList<>();

        Coord startCoord = new Coord(random.nextInt(numRows), random.nextInt(numCols));
        toVisit.add(startCoord);
        visitedCoords.add(startCoord);

        while (!toVisit.isEmpty()) {
             // pick a coord to visit next
            Coord curCoord = pickNextCoord(toVisit);

            // pick a random unvisited neighbour
            List<Coord> neighbourCoords = getNeighbourCoords(curCoord, numRows, numCols);
            List<Coord> unvisitedNeighbours = new ArrayList<>(neighbourCoords.stream()
                    .filter(coord -> !visitedCoords.contains(coord))
                    .toList());

            if (unvisitedNeighbours.isEmpty()) {
                toVisit.remove(curCoord);
                continue;
            }

            Coord neighbourCoord = pickNeighbour(unvisitedNeighbours);

            // carve passage between cell and neighbour
            MazeCell curCell = newMaze[curCoord.r][curCoord.c];
            MazeCell neighbourCell = newMaze[neighbourCoord.r][neighbourCoord.c];

            curCell.carvePassageTo(neighbourCell);
            neighbourCell.carvePassageTo(curCell);

            // mark as visited and add onto to carve list
            visitedCoords.add(neighbourCoord);
            toVisit.add(neighbourCoord);
        }

        return newMaze;
    }

    private Coord pickNextCoord(List<Coord> coords) {
        // changing how the next coord is picked will change how the algorithm behaves
        return coords.getLast();
    }

    private Coord pickNeighbour(List<Coord> coords) {
        // this will also change how algorithm behaves
        // note also mutates the order of coords
        Collections.shuffle(coords);
        return coords.getFirst();
    }

}
