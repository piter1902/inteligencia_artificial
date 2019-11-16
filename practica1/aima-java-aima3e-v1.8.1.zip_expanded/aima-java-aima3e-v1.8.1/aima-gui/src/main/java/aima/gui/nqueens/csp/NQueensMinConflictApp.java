package aima.gui.nqueens.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;

public class NQueensMinConflictApp {

	public static void main(String[] args) {
//		final String path = "D:\\PRACTICAS\\inteligencia_artificial\\tp6-parte1\\";
//		Sudoku[] lista = union(
//				union(Sudoku.listaSudokus2(path + "easy50.txt"), Sudoku.listaSudokus2(path + "top95.txt")),
//				Sudoku.listaSudokus2(path + "hardest.txt"));
		long t1 = System.nanoTime();
		CSP csp = new NQueensProblem(pack_celdasAsignadas());
		MinConflictsStrategy mcs = new MinConflictsStrategy(1000);
		StepCounter stepCounter = new StepCounter();
		mcs.addCSPStateListener(stepCounter);
		stepCounter.reset();
		// System.out.println(solver.solve(csp.copyDomains()));
		// System.out.println(stepCounter.getResults() + "\n");

		long t2 = System.nanoTime();
		System.out.printf("Se han resuelto %s sudokus en %s segundos", lista.length, (t2 - t1) / 1E9);
	}

	private static Sudoku[] union(Sudoku[] s1, Sudoku[] s2) {
		Sudoku[] lista = new Sudoku[s1.length + s2.length];
		int i = 0;
		for (Sudoku s : s1) {
			lista[i] = s;
			i++;
		}
		for (Sudoku s : s2) {
			lista[i] = s;
			i++;
		}
		return lista;
	}

	/** Counts assignment and domain changes during CSP solving. */
	protected static class StepCounter implements CSPStateListener {
		private int assignmentCount = 0;
		private int domainCount = 0;

		@Override
		public void stateChanged(Assignment assignment, CSP csp) {
			++assignmentCount;
		}

		@Override
		public void stateChanged(CSP csp) {
			++domainCount;
		}

		public void reset() {
			assignmentCount = 0;
			domainCount = 0;
		}

		public String getResults() {
			StringBuffer result = new StringBuffer();
			result.append("assignment changes: " + assignmentCount);
			if (domainCount != 0)
				result.append(", domain changes: " + domainCount);
			return result.toString();
		}
	}
}
