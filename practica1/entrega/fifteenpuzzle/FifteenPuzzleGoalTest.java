/**
 * Clase de test objetivo para el problema del 15-puzzle
 * @author Pedro Tamargo Allue
 */
package aima.core.environment.fifteenpuzzle;

import aima.core.search.framework.GoalTest;

public class FifteenPuzzleGoalTest implements GoalTest {
	FifteenPuzzleBoard goal = new FifteenPuzzleBoard(
			new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0});

	public boolean isGoalState(Object state) {
		FifteenPuzzleBoard board = (FifteenPuzzleBoard) state;
		return board.equals(goal);
	}
}