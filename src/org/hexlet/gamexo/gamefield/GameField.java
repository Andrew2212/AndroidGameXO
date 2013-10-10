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

	private static final int FLAG_DEFAULT = 1;
	private static int flagSign = FLAG_DEFAULT;// Exchange sign 'X' and 'Y'
	private static char signForNextMove = VALUE_X;

	private static int FIELD_SIZE;// 
	private static char[][] fieldMatrix;

	public static void initNewFieldMatrix(int cellNum) {
		FIELD_SIZE = cellNum;
		fieldMatrix = new char[cellNum][cellNum];
		fillDefaultGameMatrix();
		flagSign = FLAG_DEFAULT;
	}

	public static void resetGameFieldMatrix() {
		fillDefaultGameMatrix();
		flagSign = FLAG_DEFAULT;
	}

	/**
	 * @param cellNumeroX cell number on the 'X' 
	 * @param cellNumeroY cell number on the 'Y'  
	 * @return true if sign is set into cell, false if it's not.
	 */
	public static boolean setSignToCell(int cellNumeroX, int cellNumeroY) {

		if (!isCellValid(cellNumeroX, cellNumeroY)) {
			Logger.i("Cell is invalid!");
			return false;
		}

		char sign = chooseSignForNextMove();
		fieldMatrix[cellNumeroX][cellNumeroY] = sign;

		return true;
	}

	public static void reverseFlagSign() {
		flagSign = -flagSign;
	}

	// -------Getters and Setters---------

	public static char getSignForNextMove() {
		return signForNextMove;
	}

	public static char[][] getFieldMatrix() {
		return fieldMatrix;
	}

	public static char getCellValue(int cellNumeroX, int cellNumeroY) {
		return fieldMatrix[cellNumeroX][cellNumeroY];
	}
	

	// ---------Private Methods---------------------

	private static void fillDefaultGameMatrix() {
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
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

		if (fieldMatrix[x][y] == VALUE_DEFAULT) {
			return true;
		}
		return false;
	}

	private static boolean isValueValid(int x, int y) {

		if ((0 <= x && x < FIELD_SIZE) && (0 <= y && y < FIELD_SIZE)) {
			return true;
		}
		return false;
	}

	private static char chooseSignForNextMove() {

		if (0 < flagSign) {
			signForNextMove = VALUE_X;
		} else {
			signForNextMove = VALUE_O;
		}
		reverseFlagSign();
		return signForNextMove;
	}
}
