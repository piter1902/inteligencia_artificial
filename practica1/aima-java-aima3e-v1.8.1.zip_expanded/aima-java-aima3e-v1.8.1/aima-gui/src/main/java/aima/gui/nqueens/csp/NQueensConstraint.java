package aima.gui.nqueens.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

public class NQueensConstraint implements Constraint {

	private Variable var1;
	private Variable var2;
	private List<Variable> scope;

	public NQueensConstraint(Variable var1, Variable var2) {
		this.var1 = var1;
		this.var2 = var2;
		scope = new ArrayList<Variable>(2);
		scope.add(var1);
		scope.add(var2);
	}

	@Override
	public List<Variable> getScope() {
		return scope;
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		Integer value1 = (Integer) assignment.getAssignment(var1);
		Integer value2 = (Integer) assignment.getAssignment(var2);
		int col1 = ((NQueensVariable) var1).getY();
		int col2 = ((NQueensVariable) var2).getY();
		return value1 == null || (!value1.equals(value2) && col1 != col2
				&& Math.abs(value1.intValue() - col1) != Math.abs(value2.intValue() - col2));
	}

}
