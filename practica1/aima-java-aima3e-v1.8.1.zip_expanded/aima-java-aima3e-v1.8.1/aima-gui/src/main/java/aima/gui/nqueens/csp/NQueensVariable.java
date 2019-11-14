package aima.gui.nqueens.csp;

import aima.core.search.csp.Variable;

public class NQueensVariable extends Variable {

	private int x, y;
	private int value;

	public NQueensVariable(String name, int coordX, int coordY) {
		super(name);
		this.x = coordX;
		this.y = coordY;
		this.value = 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
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
