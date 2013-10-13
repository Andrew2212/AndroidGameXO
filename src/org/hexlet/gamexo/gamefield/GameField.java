package org.hexlet.gamexo.gamefield;

import org.hexlet.gamexo.utils.Logger;

/**
 * @author Andrew2212
 * 
 */
public class GameField {

	public static final char VALUE_X = 'X';
	public static final char VALUE_O = 'O';
	public static final char VALUE_DEFAULT = '_';

	private static final int FLAG_X = 1;
//	private static final int FLAG_O = -FLAG_X;
	private static int flagSign = FLAG_X;// Exchange sign 'X' and 'Y'
	private static Character signForNextMove = VALUE_X;

	private static int fieldSize;//
	private static Character[][] fieldMatrix;

//	private static boolean gameFieldInit = false;

	public static void initNewFieldMatrix(int fieldSize) {

		Logger.v("GameField::initNewFieldMatrix()::fieldSize = " + fieldSize);

		GameField.fieldSize = fieldSize;
		fieldMatrix = new Character[fieldSize][fieldSize];
		fillDefaultGameMatrix();
		flagSign = FLAG_X;

	}
	

	/**
	 * @param cellNumeroX
	 *            cell number on the 'X'
	 * @param cellNumeroY
	 *            cell number on the 'Y'
	 * @return true if sign is set into cell, false if it's not.
	 */
//	public static boolean setSignToCell(int cellNumeroX, int cellNumeroY) {
	public static boolean setSignToCell(int cellNumeroX, int cellNumeroY, char playerSign) {

		if (!isCellValid(cellNumeroX, cellNumeroY)) {
			Logger.i("Cell is invalid!");
			return false;
		}

//		Character sign = chooseSignForNextMove();
		Character sign = playerSign;
		fieldMatrix[cellNumeroX][cellNumeroY] = sign;

		return true;
	}

	public static void reverseFlagSign() {
		flagSign = -flagSign;
	}

	// -------Getters and Setters---------

//	public static char getSignForNextMove() {
//		System.out.println("getSignForNextMove() = " + signForNextMove);
//		return signForNextMove;
//	}

	public static Character[][] getFieldMatrix() {
		int size = fieldMatrix.length;
		Character[][] fieldMatrixCopy = new Character[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				fieldMatrixCopy[i][j] = fieldMatrix[i][j];
			}
		}
		return fieldMatrixCopy;
	}

	public static Character getCellValue(int cellNumeroX, int cellNumeroY) {
		return fieldMatrix[cellNumeroX][cellNumeroY];
	}

	// ---------Private Methods---------------------

	private static void fillDefaultGameMatrix() {
		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {
				fillDefaultCurrentCell(i, j);
			}
		}
	}

	private static void fillDefaultCurrentCell(int i, int j) {
		fieldMatrix[i][j] = VALUE_DEFAULT;
	}

	private static boolean isCellValid(int x, int y) {

		if (!isValueValid(x, y)) {
			return false;
		}

		if (fieldMatrix[x][y].equals(VALUE_DEFAULT)) {
			return true;
		}
		return false;
	}

	private static boolean isValueValid(int x, int y) {

		if ((0 <= x && x < fieldSize) && (0 <= y && y < fieldSize)) {
			return true;
		}
		return false;
	}

	private static Character chooseSignForNextMove() {

		if (0 < flagSign) {
			signForNextMove = VALUE_X;
		} else {
			signForNextMove = VALUE_O;
		}
		reverseFlagSign();
		return signForNextMove;
	}
}
