package aima.gui.sudoku.csp;

import aima.core.search.csp.Variable;

public class SudokuVariable extends Variable {

	private int x, y;
	private int value;

	public SudokuVariable(String name, int coordX, int coordY) {
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
