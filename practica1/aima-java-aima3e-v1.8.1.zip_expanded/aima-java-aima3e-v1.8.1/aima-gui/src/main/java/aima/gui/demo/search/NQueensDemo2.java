package aima.gui.demo.search;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFitnessFunction;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Individual;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */

public class NQueensDemo2 {

	private static final int _boardSize = 8;

	public static void main(String[] args) {

		newNQueensDemo();
	}

	private static void newNQueensDemo() {
//		nQueensWithDepthFirstSearch();
//		nQueensWithBreadthFirstSearch();
//		nQueensWithRecursiveDLS();
//		nQueensWithIterativeDeepeningSearch();
//		nQueensSimulatedAnnealingSearch();
//		nQueensHillClimbingSearch();
//		nQueensGeneticAlgorithmSearch();
		nQueensHillClimbingSearch_Statistics(10000);
	}

	private static void nQueensHillClimbingSearch_Statistics(int numExperiments) {
		System.out.println("NQueens HillClimbing con " + numExperiments + " estados iniciales diferentes");
		// Generar numExperiments tableros diferentes
		List<NQueensBoard> boards = generarTableros(numExperiments);
		float fallos = 0, aciertos = 0, cost_fallo = 0, cost_acierto = 0;
		for (NQueensBoard b : boards) {
			Problem p = new Problem(b, NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
			SearchAgent sa = null;
			try {
				sa = new SearchAgent(p, search);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sa != null) {
				// Comprobamos el estado final
				switch (search.getOutcome()) {
				case SOLUTION_FOUND:
					aciertos += 1;
					cost_acierto += Float.parseFloat(sa.getInstrumentation().getProperty("pathCost"));
					break;
				case FAILURE:
					fallos += 1;
					cost_fallo += Float.parseFloat(sa.getInstrumentation().getProperty("pathCost"));
					break;
				}
			}
		}
		// Calculamos la media
		cost_acierto /= aciertos;
		cost_fallo /= fallos;

		aciertos /= numExperiments;
		fallos /= numExperiments;

		// Mostramos el resultado de la ejecución
		System.out.format("Fallos: %s.4\n", fallos);
		System.out.format("Coste medio fallos: %s.4\n", cost_fallo);
		System.out.format("Exitos: %s.4\n", aciertos);
		System.out.format("Coste medio Exitos: %s.4\n", cost_acierto);
	}

	private static List<NQueensBoard> generarTableros(int numExperiments) {
		List<NQueensBoard> lista = new LinkedList<NQueensBoard>();
		int generados = 0;
		while (generados < numExperiments) {
			NQueensBoard board = generateBoard(8);
			if (!lista.contains(board)) {
				lista.add(board);
				generados++;
			}
		}
		return lista;
	}

	private static NQueensBoard generateBoard(int n) {
		NQueensBoard board = new NQueensBoard(n);
		// Generamos posiciones aleatorias (una por columna)
		List<XYLocation> tablero = new LinkedList<XYLocation>();
		for (int i = 0; i < n; i++) {
			tablero.add(new XYLocation(i, new Random(System.nanoTime()).nextInt(n)));
		}
		board.setBoard(tablero);
		return board;
	}

	private static void nQueensWithRecursiveDLS() {
		System.out.println("\nNQueensDemo recursive DLS -->");
		try {
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			Search search = new DepthLimitedSearch(_boardSize);
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void nQueensWithBreadthFirstSearch() {
		try {
			System.out.println("\nNQueensDemo BFS -->");
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			Search search = new BreadthFirstSearch(new TreeSearch());
			SearchAgent agent2 = new SearchAgent(problem, search);
			printActions(agent2.getActions());
			printInstrumentation(agent2.getInstrumentation());
		} catch (Exception e1) {

			e1.printStackTrace();
		}
	}

	private static void nQueensWithDepthFirstSearch() {
		System.out.println("\nNQueensDemo DFS -->");
		try {
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			Search search = new DepthFirstSearch(new GraphSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensWithIterativeDeepeningSearch() {
		System.out.println("\nNQueensDemo Iterative DS  -->");
		try {
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			Search search = new IterativeDeepeningSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensSimulatedAnnealingSearch() {
		System.out.println("\nNQueensDemo Simulated Annealing  -->");
		try {
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic());
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void nQueensHillClimbingSearch() {
		System.out.println("\nNQueensDemo HillClimbing  -->");
		try {
			Problem problem = new Problem(new NQueensBoard(_boardSize), NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void nQueensGeneticAlgorithmSearch() {
		System.out.println("\nNQueensDemo GeneticAlgorithm  -->");
		try {
			NQueensFitnessFunction fitnessFunction = new NQueensFitnessFunction();
			// Generate an initial population
			Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
			for (int i = 0; i < 50; i++) {
				population.add(fitnessFunction.generateRandomIndividual(_boardSize));
			}

			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(_boardSize,
					fitnessFunction.getFiniteAlphabetForBoardOfSize(_boardSize), 0.15);

			// Run for a set amount of time
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, fitnessFunction,
					1000L);

			System.out.println(
					"Max Time (1 second) Best Individual=\n" + fitnessFunction.getBoardForIndividual(bestIndividual));
			System.out.println("Board Size      = " + _boardSize);
			System.out.println("# Board Layouts = " + (new BigDecimal(_boardSize)).pow(_boardSize));
			System.out.println("Fitness         = " + fitnessFunction.getValue(bestIndividual));
			System.out.println("Is Goal         = " + fitnessFunction.isGoalState(bestIndividual));
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Itertions       = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

			// Run till goal is achieved
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, fitnessFunction, 0L);

			System.out.println("");
			System.out.println("Goal Test Best Individual=\n" + fitnessFunction.getBoardForIndividual(bestIndividual));
			System.out.println("Board Size      = " + _boardSize);
			System.out.println("# Board Layouts = " + (new BigDecimal(_boardSize)).pow(_boardSize));
			System.out.println("Fitness         = " + fitnessFunction.getValue(bestIndividual));
			System.out.println("Is Goal         = " + fitnessFunction.isGoalState(bestIndividual));
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Itertions       = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

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