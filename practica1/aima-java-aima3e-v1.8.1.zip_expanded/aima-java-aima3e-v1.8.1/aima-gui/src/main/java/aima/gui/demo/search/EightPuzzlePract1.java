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

public class EightPuzzlePract1 {
	static EightPuzzleBoard boardWithThreeMoveSolution = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });;

	static EightPuzzleBoard random1 = new EightPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });

	static EightPuzzleBoard extreme = new EightPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });

	public static void main(String[] args) {
		/*
		 * eightPuzzleDLSDemo(); eightPuzzleIDLSDemo();
		 * eightPuzzleGreedyBestFirstDemo(); eightPuzzleGreedyBestFirstManhattanDemo();
		 * eightPuzzleAStarDemo(); eightPuzzleAStarManhattanDemo();
		 * eightPuzzleSimulatedAnnealingDemo();
		 */

		System.out.format("%15s|%11s|%11s|%11s|%11s|%11s", "Problema", "Profundidad", "Expand", "Q.Size", "MasQS",
				"tiempo");
		// Tablero de 3 movimientos
		// Busqueda en anchura
		executeSearch(boardWithThreeMoveSolution, "BFS-G-3", new BreadthFirstSearch(new GraphSearch()), true, "");
		executeSearch(boardWithThreeMoveSolution, "BFS-T-3", new BreadthFirstSearch(new TreeSearch()), true, "");
		// Busqueda en profuncidad
		executeSearch(boardWithThreeMoveSolution, "DFS-G-3", new DepthFirstSearch(new GraphSearch()), true, "");
		executeSearch(boardWithThreeMoveSolution, "DFS-T-3", new DepthFirstSearch(new TreeSearch()), false, "(1)");
		// Busqueda en profundidad limitada
		executeSearch(boardWithThreeMoveSolution, "DLS-9-3", new DepthLimitedSearch(9), true, "");
		executeSearch(boardWithThreeMoveSolution, "DLS-3-3", new DepthLimitedSearch(3), true, "");
		// Iterative Deepening Search
		executeSearch(boardWithThreeMoveSolution, "IDS-3", new IterativeDeepeningSearch(), true, "");
		// Uniform Cost Search
		executeSearch(boardWithThreeMoveSolution, "UCS-G-3", new UniformCostSearch(new GraphSearch()), true, "");
		executeSearch(boardWithThreeMoveSolution, "UCS-T-3", new UniformCostSearch(new TreeSearch()), true, "");

		// Con tablero de 9 movimientos -> random1
		// Busqueda en anchura
		executeSearch(random1, "BFS-G-9", new BreadthFirstSearch(new GraphSearch()), true, "");
		executeSearch(random1, "BFS-T-9", new BreadthFirstSearch(new TreeSearch()), true, "");
		// Busqueda en profuncidad
		executeSearch(random1, "DFS-G-9", new DepthFirstSearch(new GraphSearch()), true, "");
		executeSearch(random1, "DFS-T-9", new DepthFirstSearch(new TreeSearch()), false, "(1)");
		// Busqueda en profundidad limitada
		executeSearch(random1, "DLS-9-9", new DepthLimitedSearch(9), true, "");
		executeSearch(random1, "DLS-3-9", new DepthLimitedSearch(3), true, "");
		// Iterative Deepening Search
		executeSearch(random1, "IDS-9", new IterativeDeepeningSearch(), true, "");
		// Uniform Cost Search
		executeSearch(random1, "UCS-G-9", new UniformCostSearch(new GraphSearch()), true, "");
		executeSearch(random1, "UCS-T-9", new UniformCostSearch(new TreeSearch()), true, "");

		// Con tablero de 30 movimientos -> extreme
		// Busqueda en anchura
		executeSearch(extreme, "BFS-G-30", new BreadthFirstSearch(new GraphSearch()), true, "");
		executeSearch(extreme, "BFS-T-30", new BreadthFirstSearch(new TreeSearch()), false, "(2)");
		// Busqueda en profuncidad
		executeSearch(extreme, "DFS-G-30", new DepthFirstSearch(new GraphSearch()), true, "");
		executeSearch(extreme, "DFS-T-30", new DepthFirstSearch(new TreeSearch()), false, "(1)");
		// Busqueda en profundidad limitada
		executeSearch(extreme, "DLS-9-30", new DepthLimitedSearch(9), true, "");
		executeSearch(extreme, "DLS-3-30", new DepthLimitedSearch(3), true, "");
		// Iterative Deepening Search
		executeSearch(extreme, "IDS-30", new IterativeDeepeningSearch(), false, "(1)");
		// Uniform Cost Search
		executeSearch(extreme, "UCS-G-30", new UniformCostSearch(new GraphSearch()), true, "");
		executeSearch(extreme, "UCS-T-30", new UniformCostSearch(new TreeSearch()), false, "(2)");

	}

	public static void executeSearch(EightPuzzleBoard board, String header, Search search, boolean execute,
			String message) {
		long t1, t2;
		Properties prop = new Properties();
		int queueSize, maxQueueSize, depth, expandedNodes;
		if (execute) {
			try {
				Problem p = new Problem(board, EightPuzzleFunctionFactory.getActionsFunction(),
						EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
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

	private static void eightPuzzleDLSDemo() {
		System.out.println("\nEightPuzzleDemo recursive DLS (9) -->");
		try {
			Problem problem = new Problem(boardWithThreeMoveSolution, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new DepthLimitedSearch(9);
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleIDLSDemo() {
		System.out.println("\nEightPuzzleDemo Iterative DLS -->");
		try {
			Problem problem = new Problem(random1, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new IterativeDeepeningSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleGreedyBestFirstDemo() {
		System.out.println("\nEightPuzzleDemo Greedy Best First Search (MisplacedTileHeursitic)-->");
		try {
			Problem problem = new Problem(boardWithThreeMoveSolution, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new GreedyBestFirstSearch(new GraphSearch(), new MisplacedTilleHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleGreedyBestFirstManhattanDemo() {
		System.out.println("\nEightPuzzleDemo Greedy Best First Search (ManhattanHeursitic)-->");
		try {
			Problem problem = new Problem(boardWithThreeMoveSolution, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new GreedyBestFirstSearch(new GraphSearch(), new ManhattanHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleAStarDemo() {
		System.out.println("\nEightPuzzleDemo AStar Search (MisplacedTileHeursitic)-->");
		try {
			Problem problem = new Problem(random1, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new AStarSearch(new GraphSearch(), new MisplacedTilleHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void eightPuzzleSimulatedAnnealingDemo() {
		System.out.println("\nEightPuzzleDemo Simulated Annealing  Search -->");
		try {
			Problem problem = new Problem(random1, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new ManhattanHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void eightPuzzleAStarManhattanDemo() {
		System.out.println("\nEightPuzzleDemo AStar Search (ManhattanHeursitic)-->");
		try {
			Problem problem = new Problem(random1, EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), new EightPuzzleGoalTest());
			Search search = new AStarSearch(new GraphSearch(), new ManhattanHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
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