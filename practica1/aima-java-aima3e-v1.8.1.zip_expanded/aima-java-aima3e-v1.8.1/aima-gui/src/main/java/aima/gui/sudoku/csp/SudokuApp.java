package aima.gui.sudoku.csp;

import java.util.ArrayList;
import java.util.List;

public class SudokuApp {

	public static void main(String[] args) {
		Sudoku[] lista = union(union(Sudoku.listaSudokus2("easy50.txt"), Sudoku.listaSudokus2("top95.txt")),
				Sudoku.listaSudokus2("hardest.txt"));
	}

	private static Sudoku[] union(Sudoku[] s1, Sudoku[] s2) {
		List<Sudoku> list = new ArrayList<>();
		for (Sudoku s : s1) {
			list.add(s);
		}
		for (Sudoku s : s2) {
			list.add(s);
		}
		return (Sudoku[])list.toArray();
	}
}
