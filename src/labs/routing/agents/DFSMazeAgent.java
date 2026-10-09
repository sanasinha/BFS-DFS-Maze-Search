package src.labs.routing.agents;

// SYSTEM IMPORTS
import edu.bu.labs.routing.utils.Coordinate;
import edu.bu.labs.routing.enums.Direction;
import edu.bu.labs.routing.graph.Path;
import edu.bu.labs.routing.State.StateView;
import edu.bu.labs.routing.maze.Tile;
import edu.bu.labs.routing.agents.MazeAgent;

import java.util.Collection;
import java.util.HashSet;   // will need for dfs
import java.util.Stack;     // will need for dfs
import java.util.Set;       // will need for dfs


// JAVA PROJECT IMPORTS


public class DFSMazeAgent extends MazeAgent {

    public DFSMazeAgent(final int agentId) {
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
        Stack<Path<Coordinate>> frontier = new Stack<Path<Coordinate>>();
        Set<Coordinate> visited = new HashSet<Coordinate>();

        frontier.push(new Path<Coordinate>(src));

        while(!frontier.isEmpty()) {
            Path<Coordinate> path = frontier.pop();
            Coordinate current = path.current();

            if(visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if(current.equals(goal)) {
                return path;
            }

            for(Direction direction : Direction.getCardinalDirections()) {
                Coordinate neighbor = current.getNeighbor(direction);
                if(stateView.isInBounds(neighbor)
                   && stateView.getTileState(neighbor).canPassThrough()
                   && !visited.contains(neighbor)) {
                    frontier.push(new Path<Coordinate>(path, neighbor, 1.0));
                }
            }
        }

        // no path exists
        return null;
    }

}
