package aima.gui.nqueens.csp;

import java.util.ArrayList;
import java.util.List;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;

public class NQueensProblem extends CSP {
	private static final int cells = 81;
	private static final int dimension = 8;
	private static List<Variable> variables = null;

	/**
	 *
	 * @return Devuelve la lista de variables del Sudoku. Nombre Cell at [i][j], y
	 *         coordenadas i,j
	 */
	private static List<Variable> collectVariables() {
		variables = new ArrayList<Variable>();
//		for (int i = 0; i < 9; i++)
		for (int j = 0; j < dimension; j++) {
			variables.add(new NQueensVariable("Reina en columna[" + j + "]", j));
		}
		return variables;
	}

	/**
	 *
	 * @param var variable del Sudoku
	 * @return Dominio de la variable, si tiene valor el domio es el valor. Sino el
	 *         domino 1-9
	 */
	private static List<Integer> getNQueensDomain(NQueensVariable var) {
		List<Integer> list = new ArrayList<Integer>();
		if (var.getValue() != 0) {
			list.add(new Integer(var.getValue()));
			return list;
		} else
			for (int i = 1; i <= dimension; i++)
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
		// initialize(pack);
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

	/**
	 * Inicializa las variables a partir de las celdas disponibles, que tienen
	 * valor. Recorren las listas de variables del Sudoku y del pack, y si tienen
	 * las mismas coordenadas, les da el valor que tiene.
	 * 
	 * @param pack
	 */
	private void initialize(AvailableCells pack) {
		List<Variable> alList = pack.getList();
		Domain domain;
		for (int i = 0; i < cells; i++) {
			NQueensVariable var1 = (NQueensVariable) variables.get(i);
			for (int j = 0; j < pack.getNumOfAvailable(); j++) {
				NQueensVariable var2 = (NQueensVariable) alList.get(j);
				if (var1.getX() == var2.getX() && var1.getY() == var2.getY()) {
					var1.setValue(var2.getValue());
				}
			}
		}
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
