package aima.gui.sudoku.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;

public class SudokuApp {

	public static void main(String[] args) {
		final String path = "D:\\PRACTICAS\\inteligencia_artificial\\tp6-parte1\\";
		Sudoku[] lista = union(
				union(Sudoku.listaSudokus2(path + "easy50.txt"), Sudoku.listaSudokus2(path + "top95.txt")),
				Sudoku.listaSudokus2(path + "hardest.txt"));
		int resueltos = 0;
		for (Sudoku s : lista) {
			CSP csp = new SudokuProblem(s.pack_celdasAsignadas());
			StepCounter stepCounter = new StepCounter();
			SolutionStrategy solver = new ImprovedBacktrackingStrategy(true, true, true, true);
			solver.addCSPStateListener(stepCounter);
			stepCounter.reset();
			System.out.println("---------");
			s.imprimeSudoku();
			if (!s.completo()) {
				System.out.println("SUDOKU INCOMPLETO - RESOLVIENDO");
				Assignment as = solver.solve(csp.copyDomains());
				System.out.print(as);
				System.out.print(stepCounter.getResults() + "\n");
				Sudoku solved = new Sudoku(as);
				solved.imprimeSudoku();
				System.out.println("Sudoku solucionado correctamente");
				resueltos++;
			}
		}
		System.out.println("+++++++++");
		System.out.printf("Se han resuelto %s sudokus", resueltos);
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
