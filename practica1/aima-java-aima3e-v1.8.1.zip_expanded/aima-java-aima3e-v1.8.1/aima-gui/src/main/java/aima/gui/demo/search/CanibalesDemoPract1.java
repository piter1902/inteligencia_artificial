package aima.gui.demo.search;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.Canibales.CanibalesBoard;
import aima.core.environment.Canibales.CanibalesFunctionFactory;
import aima.core.environment.Canibales.CanibalesGoalTest;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.environment.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
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

/**
 * @author Ravi Mohan
 * 
 */

public class CanibalesDemoPract1 {
	static EightPuzzleBoard boardWithThreeMoveSolution = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });;

	static EightPuzzleBoard random1 = new EightPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });

	static EightPuzzleBoard extreme = new EightPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });

	static CanibalesBoard initial = new CanibalesBoard();
	
	public static void main(String[] args) {

		System.out.format("%15s|%11s|%11s|%11s|%11s|%11s", "Problema", "Profundidad", "Expand", "Q.Size", "MasQS",
				"tiempo");
		// Tablero de 3 movimientos
		// Busqueda en anchura
		executeSearch(initial, "BFS-G", new BreadthFirstSearch(new GraphSearch()), true, "");
		executeSearch(initial, "BFS-T", new BreadthFirstSearch(new TreeSearch()), true, "");
		// Busqueda en profuncidad
		executeSearch(initial, "DFS-G", new DepthFirstSearch(new GraphSearch()), true, "");
		executeSearch(initial, "DFS-T", new DepthFirstSearch(new TreeSearch()), true, "");
		// Busqueda en profundidad limitada
		executeSearch(initial, "DLS-9", new DepthLimitedSearch(9), true, "");
		executeSearch(initial, "DLS-3", new DepthLimitedSearch(3), true, "");
		// Iterative Deepening Search
		executeSearch(initial, "IDS", new IterativeDeepeningSearch(), true, "");
		// Uniform Cost Search
		executeSearch(initial, "UCS-G", new UniformCostSearch(new GraphSearch()), true, "");
		executeSearch(initial, "UCS-T", new UniformCostSearch(new TreeSearch()), true, "");

	}

	public static void executeSearch(CanibalesBoard board, String header, Search search, boolean execute,
			String message) {
		long t1, t2;
		Properties prop = new Properties();
		int queueSize, maxQueueSize, depth, expandedNodes;
		if (execute) {
			try {
				Problem p = new Problem(board, CanibalesFunctionFactory.getActionsFunction(),
						CanibalesFunctionFactory.getResultFunction(), new CanibalesGoalTest());
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

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// Caso de no ejecucion -> <header> --- --- --- --- <message>
			System.out.format("\n%15s|%11s|%11s|%11s|%11s|%11s", header, "---", "---", "---", "---", message);
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