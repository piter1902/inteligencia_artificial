/**
 * Clase que representa el estado en el problema de los Canibales
 * @author Pedro Tamargo Allue
 */
package aima.core.environment.Canibales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.util.datastructure.XYLocation;

public class CanibalesBoard {
	public static Action MOVER1C = new DynamicAction("Mover1C");

	public static Action MOVER2C = new DynamicAction("Mover2C");

	public static Action MOVER1M = new DynamicAction("Mover1M");

	public static Action MOVER2M = new DynamicAction("Mover2M");

	public static Action MOVER1C1M = new DynamicAction("Mover1C1M");

	private int[] state;

	//
	// PUBLIC METHODS
	//

	/*
	 * El estado es: {Ci, Mi, pos_barca, Cd, Md} Xi : Canibales o misioneros en la
	 * orilla izquierda Xd : Canibales o misioneros en la orilla derecha pos_barca :
	 * 0 -> Izquierda ; 1 -> Derecha
	 */
	public CanibalesBoard() {
		state = new int[] { 3, 3, 0, 0, 0 };
	}

	public CanibalesBoard(int[] state) {
		this.state = new int[state.length];
		System.arraycopy(state, 0, this.state, 0, state.length);
	}

	public CanibalesBoard(CanibalesBoard copyBoard) {
		this(copyBoard.getState());
	}

	public int[] getState() {
		return state;
	}

	// Metodos para cambiar entre estados
	// Se asume que estos metodos solo se llamaran despues de la comprobacion
	// necesaria
	public void mover1C() {
		if (this.state[2] == 0) {
			this.state[0] -= 1;
			this.state[3] += 1;
		} else {
			this.state[0] += 1;
			this.state[3] -= 1;
		}
		this.state[2] = this.state[2] == 1 ? 0 : 1;
	}

	public void mover2C() {
		if (this.state[2] == 0) {
			this.state[0] -= 2;
			this.state[3] += 2;
		} else {
			this.state[0] += 2;
			this.state[3] -= 2;
		}
		this.state[2] = this.state[2] == 1 ? 0 : 1;
	}

	public void mover1M() {
		if (this.state[2] == 0) {
			this.state[1] -= 1;
			this.state[4] += 1;
		} else {
			this.state[1] += 1;
			this.state[4] -= 1;
		}
		this.state[2] = this.state[2] == 1 ? 0 : 1;
	}

	public void mover2M() {
		if (this.state[2] == 0) {
			this.state[1] -= 2;
			this.state[4] += 2;
		} else {
			this.state[1] += 2;
			this.state[4] -= 2;
		}
		this.state[2] = this.state[2] == 1 ? 0 : 1;
	}

	public void mover1M1C() {
		if (this.state[2] == 0) {
			this.state[0] -= 1;
			this.state[1] -= 1;
			this.state[3] += 1;
			this.state[4] += 1;
		} else {
			this.state[0] += 1;
			this.state[1] += 1;
			this.state[3] -= 1;
			this.state[4] -= 1;
		}
		this.state[2] = this.state[2] == 1 ? 0 : 1;
	}

	// Metodos para comprobar que movimientos se pueden hacer
	public boolean canMoveBoat(Action where) {
		boolean retVal = true;
		// Variables para simplificar las operaciones
		// Canibales
		int nCan_izq = state[0]; // Numero de canibales en la orilla izquierda
		int nCan_dch = state[3]; // Numero de canibales en la orilla derecha
		// Misioneros
		int nMis_izq = state[1]; // Numero de misioneros en la orilla izquierda
		int nMis_dch = state[4]; // Numero de misioneros en la orilla derecha
		// Bote -> si state[2] = 0 -> el bote esta en la izquierda
		boolean bote_izq = state[2] == 0; // true si y solo si el bote esta en la orilla izquierda
		//System.out.format("\n%15s|%11s|%11s|%11s|%11s", nCan_izq, nMis_izq, bote_izq, nCan_dch, nMis_dch);
		// Comprobamos el movimiento que se quiere realizar
		if (where.equals(MOVER1C)) {
			retVal = (bote_izq && nCan_izq >= 1 && noPeligroso(nCan_izq - 1, nCan_dch + 1, nMis_izq, nMis_dch))
					|| (!bote_izq && nCan_dch >= 1 && noPeligroso(nCan_izq + 1, nCan_dch - 1, nMis_izq, nMis_dch));
		} else if (where.equals(MOVER2C)) {
			retVal = (bote_izq && nCan_izq >= 2 && noPeligroso(nCan_izq - 2, nCan_dch + 2, nMis_izq, nMis_dch))
					|| (!bote_izq && nCan_dch >= 2 && noPeligroso(nCan_izq + 2, nCan_dch - 2, nMis_izq, nMis_dch));
		} else if (where.equals(MOVER1M)) {
			retVal = (bote_izq && nMis_izq >= 1 && noPeligroso(nCan_izq, nCan_dch, nMis_izq - 1, nMis_dch + 1))
					|| (!bote_izq && nMis_dch >= 1 && noPeligroso(nCan_izq, nCan_dch, nMis_izq + 1, nMis_dch - 1));
		} else if (where.equals(MOVER2M)) {
			retVal = (bote_izq && nMis_izq >= 2 && noPeligroso(nCan_izq, nCan_dch, nMis_izq - 2, nMis_dch + 2))
					|| (!bote_izq && nMis_dch >= 2 && noPeligroso(nCan_izq, nCan_dch, nMis_izq + 2, nMis_dch - 2));
		} else if (where.equals(MOVER1C1M)) {
			retVal = (bote_izq && nCan_izq >= 1 && nMis_izq >= 1
					&& noPeligroso(nCan_izq - 1, nCan_dch + 1, nMis_izq - 1, nMis_dch + 1))
					|| (!bote_izq && nCan_dch >= 1 && nMis_dch >= 1
					&& noPeligroso(nCan_izq + 1, nCan_dch - 1, nMis_izq + 1, nMis_dch - 1));
		}
		return retVal;
	}

	private boolean noPeligroso(int nCan_izq, int nCan_dch, int nMis_izq, int nMis_dch) {
		return (nCan_izq <= nMis_izq || nMis_izq == 0) && (nCan_dch <= nMis_dch || nMis_dch == 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(state);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CanibalesBoard other = (CanibalesBoard) obj;
		if (!Arrays.equals(state, other.state))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String retVal = "RIBERA-IZQ ";
		// Canibales izquierda
		if (state[1] == 3) {
			retVal += "M M M ";
		} else if (state[1] == 2) {
			retVal += "M M ";
		} else if (state[1] == 1) {
			retVal += "M ";
		}
		// Misioneros izquierda
		if (state[0] == 3) {
			retVal += "C C C ";
		} else if (state[0] == 2) {
			retVal += "C C ";
		} else if (state[0] == 1) {
			retVal += "C ";
		}
		// Posicion del bote
		if (state[2] == 0) {
			// En la izquierda
			retVal += "BOTE --RIO-- ";
		} else if (state[2] == 1) {
			retVal += "--RIO-- BOTE ";
		}
		// Canibales derecha
		if (state[4] == 3) {
			retVal += "M M M ";
		} else if (state[4] == 2) {
			retVal += "M M ";
		} else if (state[4] == 1) {
			retVal += "M ";
		}
		// Misioneros derecha
		if (state[3] == 3) {
			retVal += "C C C ";
		} else if (state[3] == 2) {
			retVal += "C C ";
		} else if (state[3] == 1) {
			retVal += "C ";
		}
		return retVal + "RIBERA-DCH";
	}
}
