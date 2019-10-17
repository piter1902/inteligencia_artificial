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

/**
 * @author Ravi Mohan
 * 
 */

public class FifteenPuzzlePract1 {
	static FifteenPuzzleBoard boardWithThreeMoveSolution = new FifteenPuzzleBoard(
			new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 });;

	static FifteenPuzzleBoard random1 = new FifteenPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });

	static FifteenPuzzleBoard extreme = new FifteenPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });

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
		/*
		 * executeSearch(boardWithThreeMoveSolution, "BFS-T-3", new
		 * BreadthFirstSearch(new TreeSearch()), true, ""); // Busqueda en profuncidad
		 * executeSearch(boardWithThreeMoveSolution, "DFS-G-3", new DepthFirstSearch(new
		 * GraphSearch()), true, ""); executeSearch(boardWithThreeMoveSolution,
		 * "DFS-T-3", new DepthFirstSearch(new TreeSearch()), false, "(1)"); // Busqueda
		 * en profundidad limitada executeSearch(boardWithThreeMoveSolution, "DLS-9-3",
		 * new DepthLimitedSearch(9), true, "");
		 * executeSearch(boardWithThreeMoveSolution, "DLS-3-3", new
		 * DepthLimitedSearch(3), true, ""); // Iterative Deepening Search
		 * executeSearch(boardWithThreeMoveSolution, "IDS-3", new
		 * IterativeDeepeningSearch(), true, ""); // Uniform Cost Search
		 * executeSearch(boardWithThreeMoveSolution, "UCS-G-3", new
		 * UniformCostSearch(new GraphSearch()), true, "");
		 * executeSearch(boardWithThreeMoveSolution, "UCS-T-3", new
		 * UniformCostSearch(new TreeSearch()), true, "");
		 */
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