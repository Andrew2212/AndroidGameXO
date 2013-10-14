package org.hexlet.gamexo.gamefield.players;

import org.hexlet.gamexo.gamefield.GameField;
import org.hexlet.gamexo.gamefield.GameView;
import org.hexlet.gamexo.utils.Logger;

public class PlayerHumanLocal implements IPlayer {

	private char signPlayer;

	public PlayerHumanLocal(char signPlayer) {
		this.signPlayer = signPlayer;
	}

	@Override
	public int[] doMove() {
		Logger.v();
		return GameView.getResultOnTouch();
	}

	@Override
	public boolean setMove(int cellX, int cellY, char signPlayer) {
		Logger.v();
		return GameField.setSignToCell(cellX, cellY, signPlayer);
	}

	public char getSignPlayer() {
		return signPlayer;
	}

}
