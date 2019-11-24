package aima.gui.nqueens.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;
import aima.core.util.datastructure.XYLocation;

public class NQueensMinConflictApp_pruebas {

	public static void main(String[] args) {

		for (int i = 10; i <= 250; i += 10) {
			int resueltos = 0;
			long time = 0;
			for (int prueba = 0; prueba < 100; prueba++) {
				long t1 = System.nanoTime();
				CSP csp = new NQueensProblem();
				MinConflictsStrategy mcs = new MinConflictsStrategy(i);
				StepCounter stepCounter = new StepCounter();
				mcs.addCSPStateListener(stepCounter);
				stepCounter.reset();

				Assignment as = mcs.solve(csp.copyDomains());
				if (as != null) {
					NQueensBoard solutionBoard = buildBoard(as);
//					System.out.println(solutionBoard);
//					int conflicts = solutionBoard.getNumberOfAttackingPairs();
//					System.out.println(conflicts + " conflictos detectados");
//					System.out.println(as);
//					System.out.println(stepCounter.getResults() + "\n");
					long t2 = System.nanoTime();
					time += t2 - t1;
					resueltos++;
				}
			}
			System.out.printf("Limite: %s -- Se han resuelto %s/100 tableros en %.6s segundos\n", i, resueltos, time / 1E9);
		}

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

	public static NQueensBoard buildBoard(Assignment as) {
		NQueensBoard nqb = new NQueensBoard(8);
		for (Variable v : as.getVariables()) {
			int valor = (int) as.getAssignment(v);
			NQueensVariable nqv = (NQueensVariable) v;
//			System.out.println(nqv + " = " + (valor));
			nqb.addQueenAt(new XYLocation(nqv.getY(), valor));
		}
		return nqb;
	}
}
