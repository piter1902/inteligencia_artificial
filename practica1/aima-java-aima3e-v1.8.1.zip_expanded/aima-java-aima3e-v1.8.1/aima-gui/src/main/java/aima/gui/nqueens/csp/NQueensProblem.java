package aima.gui.nqueens.csp;

import java.util.ArrayList;
import java.util.List;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;

public class NQueensProblem extends CSP {

	private static final int dimension = 8;
	private static List<Variable> variables = null;

	/**
	 *
	 * @return Devuelve la lista de variables de las reinas. Nombre Reina en
	 *         columna[ columna ], con el valor de la columna.
	 */
	private static List<Variable> collectVariables() {
		variables = new ArrayList<Variable>();
		for (int i = 0; i < dimension; i++) {
			variables.add(new NQueensVariable("Reina en columna[" + i + "]", i));
		}
		return variables;
	}

	/**
	 *
	 * @param var variable de las NQueens
	 * @return Dominio de la variable, si tiene valor el domino es el valor. Sino el
	 *         domino 1-9
	 */
	private static List<Integer> getNQueensDomain(NQueensVariable var) {
		List<Integer> list = new ArrayList<Integer>();
		if (var.getValue() != 0) {
			list.add(new Integer(var.getValue()));
			return list;
		} else
			for (int i = 0; i < dimension; i++)
				list.add(new Integer(i));
		return list;
	}

	/**
	 * Define como un CSP. Define variables, sus dominios y restricciones.
	 * 
	 * @param pack
	 */
	public NQueensProblem() {
		// variables
		super(collectVariables());
		for (int i = 0; i < dimension; i++) {
			NQueensVariable x = (NQueensVariable) variables.get(i);
		}
		// Define dominios de variables
		Domain domain;
		for (Variable var : getVariables()) {
			domain = new Domain(getNQueensDomain((NQueensVariable) var));
			setDomain(var, domain);
		}
		// restricciones
		doConstraint();
	}

	private void doConstraint() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i != j) {
					addConstraint(new NQueensConstraint(variables.get(i), variables.get(j)));
				}
			}
		}
	}
}
