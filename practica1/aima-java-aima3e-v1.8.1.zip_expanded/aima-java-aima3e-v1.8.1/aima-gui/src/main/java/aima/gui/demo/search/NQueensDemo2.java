package aima.gui.demo.search;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import aima.core.search.local.HillClimbingSearch.SearchOutcome;
import aima.core.search.local.Individual;
import aima.core.search.local.Scheduler;
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
//		nQueensHillClimbingSearch_Statistics(10000);
//		System.out.println("-------------------------");
//		nQueensRandomReestartHillClimbing();
//		System.out.println("-------------------------");
//		nQueensSimulatedAnnealingSearch_Statistics(1000);
//		System.out.println("-------------------------");
		nQueensHillSimulatedAnnealingRestart();
		System.out.println("-------------------------");
	}

	public static void nQueensHillClimbingSearch_Statistics(int numExperiments) {
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
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_acierto += (float) (sa.getActions().size());
					break;
				case FAILURE:
					fallos += 1;
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_fallo += (float) (sa.getActions().size());
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
		System.out.format("Fallos: %4.4s\n", fallos);
		System.out.format("Coste medio fallos: %4.4s\n", cost_fallo);
		System.out.format("Exitos: %4.4s\n", aciertos);
		System.out.format("Coste medio Exitos: %4.4s\n", cost_acierto);
	}

	public static void nQueensRandomReestartHillClimbing() {

		boolean finding = true;

		int intentos = 0, coste_acierto = 0, fallos = 0, coste_fallo = 0;

		HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
		NQueensBoard b = null;
		do {
			b = new NQueensBoard(8);
			b.setBoard(generateBoard(8));
			Problem p = new Problem(b, NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			SearchAgent sa = null;
			try {
				sa = new SearchAgent(p, search);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sa != null) {
				intentos++;
				switch (search.getOutcome()) {
				case FAILURE:
					fallos++;
					coste_fallo += sa.getActions().size();
					break;
				case SOLUTION_FOUND:
					finding = false;
					coste_acierto += sa.getActions().size();
					break;
				}
			}
		} while (finding);
		// Mostramos los resultados
		System.out.println("Seach Outcome=" + search.getOutcome());
		System.out.println("Final State=");
		System.out.println(search.getLastSearchState() + "\n");
		System.out.println("Numero de intentos:" + intentos);
		System.out.println("Fallos:" + fallos);
		System.out.println("Coste de exito:" + coste_acierto);
		System.out.println("Coste medio de exito:" + coste_acierto);
	}

	public static void nQueensSimulatedAnnealingSearch_Statistics(int numExperiments) {
		System.out.println("NQueens Simulated Annealing con " + numExperiments + " estados iniciales diferentes");
		int k = 650, limit = 2000;
		double lam = 0.05;
		Scheduler scheduler = new Scheduler(k, lam, limit);
		System.out.printf("Parametros Scheduler: Scheduler(%d,%f,%d)\n", k, lam, limit);
		// Generar numExperiments tableros diferentes
		List<NQueensBoard> boards = generarTableros(numExperiments);
		float fallos = 0, aciertos = 0, cost_fallo = 0, cost_acierto = 0;
		for (NQueensBoard b : boards) {
			Problem p = new Problem(b, NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic(), scheduler);
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
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_acierto += (float) (sa.getActions().size());
					break;
				case FAILURE:
					fallos += 1;
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_fallo += (float) (sa.getActions().size());
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
		System.out.format("Fallos: %4.4s\n", fallos);
		System.out.format("Coste medio fallos: %4.4s\n", cost_fallo);
		System.out.format("Exitos: %4.4s\n", aciertos);
		System.out.format("Coste medio Exitos: %4.4s\n", cost_acierto);
	}

	public static void nQueensHillSimulatedAnnealingRestart() {

		boolean finding = true;

		int intentos = 0, coste_acierto = 0, fallos = 0;

		int k = 650, limit = 2000;
		double lam = 0.05;
		Scheduler scheduler = new Scheduler(k, lam, limit);
		SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic(), scheduler);
		NQueensBoard b = null;
		do {
			b = new NQueensBoard(8);
			b.setBoard(generateBoard(8));
			Problem p = new Problem(b, NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
			SearchAgent sa = null;
			try {
				sa = new SearchAgent(p, search);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sa != null) {
				intentos++;
				switch (search.getOutcome()) {
				case FAILURE:
					fallos++;
					break;
				case SOLUTION_FOUND:
					finding = false;
					coste_acierto += sa.getActions().size();
					break;
				}
			}
		} while (finding);
		// Mostramos los resultados
		System.out.println("Seach Outcome=" + search.getOutcome());
		System.out.println("Final State=");
		System.out.println(search.getLastSearchState() + "\n");
		System.out.println("Numero de intentos:" + intentos);
		System.out.println("Fallos:" + fallos);
		System.out.println("Coste de exito:" + coste_acierto);
	}

	public static void nQueenGeneticAlgorithmSearch() {
		System.out.println("GeneticAlgorithm");
		// Generar numExperiments tableros diferentes

		int poblacion = 2;
		double probab_mutacion = 1.0;
		System.out.printf("Parametros iniciales:\t Poblacion:%d, Probabilidad mutacion:%f)\n", poblacion,
				probab_mutacion);
		List<NQueensBoard> boards = generarTableros(poblacion);
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
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_acierto += (float) (sa.getActions().size());
					break;
				case FAILURE:
					fallos += 1;
					// Comprobamos el tamaño de la lista de acciones a realizar -> profundidad de la
					// solucion
					cost_fallo += (float) (sa.getActions().size());
					break;
				}
			}
		}
		System.out.println("Mejor individuo=");
		System.out.println();
	}

	private static List<NQueensBoard> generarTableros(int numExperiments) {
		ArrayList<NQueensBoard> lista = new ArrayList<NQueensBoard>();
		int generados = 0;
		while (generados < numExperiments) {
			NQueensBoard board = new NQueensBoard(8);
			board.setBoard(generateBoard(8));
			if (!lista.contains(board)) {
				lista.add(board);
				generados++;
				// System.out.println("Generado nº " + generados);
				if ((generados % 1000) == 0) {
					System.out.println(generados);
				}
			}
		}
		return lista;
	}

	private static ArrayList<XYLocation> generateBoard(int n) {
		// Generamos posiciones aleatorias (una por columna)
		ArrayList<XYLocation> tablero = new ArrayList<XYLocation>();
		for (int i = 0; i < n; i++) {
			// tablero.add(new XYLocation(i, new Random(System.nanoTime()).nextInt(n)));
			tablero.add(new XYLocation(i, ((new Random(System.nanoTime()).nextInt() % n) + n) % n));
		}
		return tablero;
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