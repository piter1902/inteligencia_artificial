package aima.core.environment.eightpuzzle;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class ManhattanHeuristicFunction2 implements HeuristicFunction {

	public double h(Object state) {
		EightPuzzleBoard board = (EightPuzzleBoard) state;
		int retVal = 0;
		for (int i = 1; i < 9; i++) {
			XYLocation loc = board.getLocationOf(i);
			XYLocation goal_loc = board.goal.getLocationOf(i);
			retVal += evaluateManhattanDistanceOf(i, loc, goal_loc);
		}
		return retVal;

	}

	public int evaluateManhattanDistanceOf(int i, XYLocation loc, XYLocation goal_loc) {
		int xpos = loc.getXCoOrdinate();
		int ypos = loc.getYCoOrdinate();
		int goal_xpos = goal_loc.getXCoOrdinate();
		int goal_ypos = goal_loc.getYCoOrdinate();
		int retVal = Math.abs(xpos - goal_xpos) + Math.abs(ypos - goal_ypos);
		return retVal;
	}
}