package aima.gui.demo.search;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction2;
import aima.core.environment.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.core.environment.eightpuzzle.MisplacedTilleHeuristicFunction2;
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
import aima.core.util.math.Biseccion;

/**
 * @author Ravi Mohan
 * 
 */

public class EightPuzzlePract2 {
	static EightPuzzleBoard boardWithThreeMoveSolution = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });;

	static EightPuzzleBoard random1 = new EightPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });

	static EightPuzzleBoard extreme = new EightPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });

	public static void main(String[] args) {
		System.out.format("--%3s--%40s--%40s--\n", "---", "-------------------------------------------",
				"-------------------------------------------");
		System.out.format("||%3s||%43s||%43s||\n", " ", "Nodos Generados", "b*");
		System.out.format("--%3s--%40s--%40s--\n", "---", "-------------------------------------------",
				"-------------------------------------------");
		System.out.format("||%3s||%10s|%10s|%10s|%10s||%10s|%10s|%10s|%10s||\n", "d", "BFS", "IDS", "A*h(1)", "A*h(2)",
				"BFS", "IDS", "A*h(1)", "A*h(2)");
		System.out.format("--%3s--%40s--%40s--\n", "---", "-------------------------------------------",
				"-------------------------------------------");
		System.out.format("--%3s--%40s--%40s--\n", "---", "-------------------------------------------",
				"-------------------------------------------");
		for (int i = 2; i < 25; i++) {
			generarTabla(i);
		}
	}

	public static void generarTabla(int depth) {
		Search sBFS = new BreadthFirstSearch(new GraphSearch());
		int nodos_BFS = generar100busquedas(sBFS, depth);
		// Caso IDS (solo se ejecuta si la profundidad es menor que 10)
		int nodos_IDS = 0;
		double b_est_IDS = 0;
		Search s_A_est1 = new AStarSearch(new GraphSearch(), new MisplacedTilleHeuristicFunction2());
		int nodos_A_est1 = generar100busquedas(s_A_est1, depth);
		Search s_A_est2 = new AStarSearch(new GraphSearch(), new ManhattanHeuristicFunction2());
		int nodos_A_est2 = generar100busquedas(s_A_est2, depth);

		Biseccion bf = new Biseccion();
		bf.setDepth(depth);
		// BFS
		bf.setGeneratedNodes(nodos_BFS);
		double b_est_BFS = bf.metodoDeBiseccion(1.00000000001, 4, 1E-10);
		// IDS
		if (depth < 10) {
			Search sIDS = new IterativeDeepeningSearch();
			nodos_IDS = generar100busquedas(sIDS, depth);
			bf.setGeneratedNodes(nodos_IDS);
			b_est_IDS = bf.metodoDeBiseccion(1.00000000001, 4, 1E-10);
		}
		// A*h(1)
		bf.setGeneratedNodes(nodos_A_est1);
		double b_est_A_est1 = bf.metodoDeBiseccion(1.00000000001, 4, 1E-10);
		// A*h(2)
		bf.setGeneratedNodes(nodos_A_est2);
		double b_est_A_est2 = bf.metodoDeBiseccion(1.00000000001, 4, 1E-10);
		// Distinguimos la salida en funcion de la profundidad
		if (depth < 10) {
			System.out.format("||%3s||%10s|%10s|%10s|%10s||%10.4s|%10.4s|%10.4s|%10.4s||\n", depth, nodos_BFS, nodos_IDS,
					nodos_A_est1, nodos_A_est2, b_est_BFS, b_est_IDS, b_est_A_est1, b_est_A_est2);
		} else {
			System.out.format("||%3s||%10s|%10s|%10s|%10s||%10.4s|%10.4s|%10.4s|%10.4s||\n", depth, nodos_BFS, "---",
					nodos_A_est1, nodos_A_est2, b_est_BFS, "---", b_est_A_est1, b_est_A_est2);
		}
		
	}

	/**
	 * Metodo que devuelve los nodos generados por la busqueda <search>
	 * 
	 * @param search busqueda a realizar
	 * @param depth  profundidad de la busqueda
	 * @return nodos generados
	 */
	public static int generar100busquedas(Search search, int depth) {
		int nodosGenerados = 0;
		for (int i = 0; i < 100; i++) {
			// Generamos un tablero y ponemos su solucion
			EightPuzzleBoard initial = GenerateInitialEightPuzzleBoard.randomIni();
			//initial.setGoal(GenerateInitialEightPuzzleBoard.random(depth, initial));
			EightPuzzleGoalTest.setGoal(GenerateInitialEightPuzzleBoard.random(depth, initial));
			// Ejecutamos las busquedas
			Problem p = new Problem(new EightPuzzleBoard(initial), EightPuzzleFunctionFactory.getActionsFunction(),
					EightPuzzleFunctionFactory.getResultFunction(), EightPuzzleGoalTest.getGoal());
			try {
				SearchAgent agent = new SearchAgent(p, search);
				nodosGenerados += Integer.parseInt(agent.getInstrumentation().getProperty("nodesGenerated"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// nodosGenerados es el total -> calcular la media
		return nodosGenerados / 100;
	}

}