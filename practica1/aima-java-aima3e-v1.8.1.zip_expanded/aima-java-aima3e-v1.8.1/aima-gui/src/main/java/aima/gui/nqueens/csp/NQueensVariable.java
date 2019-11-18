package aima.gui.nqueens.csp;

import aima.core.search.csp.Variable;

public class NQueensVariable extends Variable {

	private int x, y;

	public NQueensVariable(String name, int column) {
		super(name);
		this.x = 0;
		this.y = column;
	}

	public int getValue() {
		return x;
	}

	public void setValue(int value) {
		this.x = value;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
