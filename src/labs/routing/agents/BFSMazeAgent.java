package src.labs.routing.agents;



// SYSTEM IMPORTS
import edu.bu.labs.routing.utils.Coordinate;
import edu.bu.labs.routing.enums.Direction;
import edu.bu.labs.routing.graph.Path;
import edu.bu.labs.routing.State.StateView;
import edu.bu.labs.routing.maze.Tile;
import edu.bu.labs.routing.agents.MazeAgent;


import java.util.Collection;
import java.util.HashSet;       // will need for bfs
import java.util.Queue;         // will need for bfs
import java.util.LinkedList;    // will need for bfs
import java.util.Set;


// JAVA PROJECT IMPORTS


public class BFSMazeAgent extends MazeAgent {

    public BFSMazeAgent(final int agentId) {
        super(agentId);
    }

    @Override
    public void initializeFromState(final StateView stateView) {
        // find the FINISH tile
        Coordinate finishCoord = null;
        for(int rowIdx = 0; rowIdx < stateView.getNumRows(); ++rowIdx) {
            for(int colIdx = 0; colIdx < stateView.getNumCols(); ++colIdx) {
                if(stateView.getTileState(new Coordinate(rowIdx, colIdx)) == Tile.State.FINISH) {
                    finishCoord = new Coordinate(rowIdx, colIdx);
                }
            }
        }
        this.setFinishCoordinate(finishCoord);

        // make sure to call the super-class' version!
        super.initializeFromState(stateView);
    }

    @Override
    public boolean shouldReplacePlan(final StateView stateView) {
        return false;
    }

    @Override
    public Path<Coordinate> search(final Coordinate src, final Coordinate goal, final StateView stateView) {
        Queue<Path<Coordinate>> frontier = new LinkedList<Path<Coordinate>>();
        Set<Coordinate> visited = new HashSet<Coordinate>();

        frontier.add(new Path<Coordinate>(src));
        visited.add(src);

        while(!frontier.isEmpty()) {
            Path<Coordinate> path = frontier.poll();
            Coordinate current = path.current();

            if(current.equals(goal)) {
                return path;
            }

            for(Direction direction : Direction.values()) {
                Coordinate neighbor = current.getNeighbor(direction);
                if(stateView.isInBounds(neighbor)
                   && stateView.getTileState(neighbor).canPassThrough()
                   && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    frontier.add(new Path<Coordinate>(path, neighbor, 1.0));
                }
            }
        }

        // no path exists
        return null;
    }

}
