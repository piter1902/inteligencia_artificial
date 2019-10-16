package aima.core.environment.Canibales;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

public class CanibalesFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new CanibalesActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new CanibalesResultFunction();
		}
		return _resultFunction;
	}

	private static class CanibalesActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			CanibalesBoard board = (CanibalesBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();

			if (board.canMoveBoat(CanibalesBoard.MOVER1C)) {
				actions.add(CanibalesBoard.MOVER1C);
			}
			if (board.canMoveBoat(CanibalesBoard.MOVER2C)) {
				actions.add(CanibalesBoard.MOVER2C);
			}
			if (board.canMoveBoat(CanibalesBoard.MOVER1M)) {
				actions.add(CanibalesBoard.MOVER1M);
			}
			if (board.canMoveBoat(CanibalesBoard.MOVER2M)) {
				actions.add(CanibalesBoard.MOVER2M);
			}
			if (board.canMoveBoat(CanibalesBoard.MOVER1C1M)) {
				actions.add(CanibalesBoard.MOVER1C1M);
			}

			return actions;
		}
	}

	private static class CanibalesResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			CanibalesBoard board = (CanibalesBoard) s;

			if (CanibalesBoard.MOVER1C.equals(a) && board.canMoveBoat(CanibalesBoard.MOVER1C)) {
				CanibalesBoard newBoard = new CanibalesBoard(board);
				newBoard.mover1C();
				return newBoard;
			} else if (CanibalesBoard.MOVER2C.equals(a) && board.canMoveBoat(CanibalesBoard.MOVER2C)) {
				CanibalesBoard newBoard = new CanibalesBoard(board);
				newBoard.mover2C();
				return newBoard;
			} else if (CanibalesBoard.MOVER1M.equals(a) && board.canMoveBoat(CanibalesBoard.MOVER1M)) {
				CanibalesBoard newBoard = new CanibalesBoard(board);
				newBoard.mover1M();
				return newBoard;
			} else if (CanibalesBoard.MOVER2M.equals(a) && board.canMoveBoat(CanibalesBoard.MOVER2M)) {
				CanibalesBoard newBoard = new CanibalesBoard(board);
				newBoard.mover2M();
				return newBoard;
			} else if (CanibalesBoard.MOVER1C1M.equals(a) && board.canMoveBoat(CanibalesBoard.MOVER1C1M)) {
				CanibalesBoard newBoard = new CanibalesBoard(board);
				newBoard.mover1M1C();
				return newBoard;
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
