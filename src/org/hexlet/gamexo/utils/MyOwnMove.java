package org.hexlet.gamexo.utils;

public class MyOwnMove {

	private static int myOwnMoveX;
	private static int myOwnMoveY;

	private static final int X = 0;
	private static final int Y = 1;
	private static int[] myOwnMove = new int[2];
	private static boolean isMyOwnMoveDone = false;

	public static int[] getMyOwnMove() {
		if (isMyOwnMoveDone) {
			isMyOwnMoveDone = false;
			return myOwnMove;
		}
		return null;
	}

	public static void setMyOwnMove(int[] myOwnMove) {
		MyOwnMove.myOwnMove = myOwnMove;
	}

	public static void setMyOwnMove(int x, int y) {
		MyOwnMove.myOwnMove[X] = x;
		MyOwnMove.myOwnMove[Y] = y;
		isMyOwnMoveDone = true;
	}

	// public static int getMyOwnMoveX() {
	// return myOwnMoveX;
	// }
	//
	// public static void setMyOwnMoveX(int myOwnMoveX) {
	// MyOwnMove.myOwnMoveX = myOwnMoveX;
	// }
	//
	// public static int getMyOwnMoveY() {
	// return myOwnMoveY;
	// }
	//
	// public static void setMyOwnMoveY(int myOwnMoveY) {
	// MyOwnMove.myOwnMoveY = myOwnMoveY;
	// }

}
