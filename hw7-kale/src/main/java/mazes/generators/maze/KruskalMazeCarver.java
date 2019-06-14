package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.Graph;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.
        //throw new NotYetImplementedException();
        Random rand = new Random();
        ISet<Wall> toRemove = new ChainedHashSet<>();

        for (Wall wall:maze.getWalls()) {
            if (!maze.getUntouchableWalls().contains(wall)) {
               toRemove.add(wall);
            }
        }

        for (Wall wall : toRemove) {
            wall.setDistance(rand.nextDouble());
        }

        Graph<Room, Wall> graph = new Graph<Room, Wall>(maze.getRooms(), toRemove);
        return graph.findMinimumSpanningTree();

    }
}
