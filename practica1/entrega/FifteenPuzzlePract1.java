/**
 * Clase de prueba para el problema del 15-puzzle
 * @author Pedro Tamargo Allue
 */

package aima.gui.demo.search;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.environment.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.core.environment.fifteenpuzzle.FifteenPuzzleBoard;
import aima.core.environment.fifteenpuzzle.FifteenPuzzleFunctionFactory;
import aima.core.environment.fifteenpuzzle.FifteenPuzzleGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.UniformCostSearch;


public class FifteenPuzzlePract1 {

	// Solucion a 13 movimientos
	static FifteenPuzzleBoard board_1 = new FifteenPuzzleBoard(
			new int[] { 1, 10, 2, 3, 0, 5, 7, 4, 9, 11, 6, 8, 13, 14, 15, 12 });

	static FifteenPuzzleBoard no_moves_board = new FifteenPuzzleBoard(
			new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0 });

	public static void main(String[] args) {

		System.out.format("%15s|%11s|%11s|%11s|%11s|%11s", "Problema", "Profundidad", "Expand", "Q.Size", "MasQS",
				"tiempo");
		executeSearch(board_1, "BFS-G", new BreadthFirstSearch(new GraphSearch()), true, "");
		executeSearch(board_1, "DLS-10", new DepthLimitedSearch(15), true, "");
		executeSearch(board_1, "UCS-G", new UniformCostSearch(new GraphSearch()), true, "");
	}
	
	public static void executeSearch(FifteenPuzzleBoard board, String header, Search search, boolean execute,
			String message) {
		long t1, t2;
		Properties prop = new Properties();
		int queueSize, maxQueueSize, depth, expandedNodes;
		if (execute) {
			try {
				Problem p = new Problem(board, FifteenPuzzleFunctionFactory.getActionsFunction(),
						FifteenPuzzleFunctionFactory.getResultFunction(), new FifteenPuzzleGoalTest());
				t1 = System.currentTimeMillis();
				SearchAgent agent = new SearchAgent(p, search);
				t2 = System.currentTimeMillis();
				prop = agent.getInstrumentation();
				String pathcostM = agent.getInstrumentation().getProperty("pathCost");
				if (pathcostM != null)
					depth = (int) Float.parseFloat(pathcostM);
				else
					depth = 0;
				if (agent.getInstrumentation().getProperty("nodesExpanded") == null)
					expandedNodes = 0;
				else
					expandedNodes = (int) Float.parseFloat(agent.getInstrumentation().getProperty("nodesExpanded"));
				if (agent.getInstrumentation().getProperty("queueSize") == null)
					queueSize = 0;
				else
					queueSize = (int) Float.parseFloat(agent.getInstrumentation().getProperty("queueSize"));
				if (agent.getInstrumentation().getProperty("maxQueueSize") == null)
					maxQueueSize = 0;
				else
					maxQueueSize = (int) Float.parseFloat(agent.getInstrumentation().getProperty("maxQueueSize"));
				System.out.format("\n%15s|%11s|%11s|%11s|%11s|%11s", header, depth, expandedNodes, queueSize,
						maxQueueSize, t2 - t1);
				System.out.println();
				executeActions(agent.getActions(), p);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Caso de no ejecucion -> <header> --- --- --- --- <message>
			System.out.format("\n%15s|%11s|%11s|%11s|%11s|%11s", header, "---", "---", "---", "---", message);
		}
	}

	public static void executeActions(List<Action> actions, Problem problem) {
		Object initialState = problem.getInitialState();
		ResultFunction resultFunction = problem.getResultFunction();
		Object state = initialState;
		System.out.println("INITIAL STATE");
		System.out.println(state);
		for (Action action : actions) {
			System.out.println(action.toString());
			state = resultFunction.result(state, action);
			System.out.println(state);
			System.out.println("- - -");
		}
	}

	private static void printInstrumentation(Properties properties) {
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}

}