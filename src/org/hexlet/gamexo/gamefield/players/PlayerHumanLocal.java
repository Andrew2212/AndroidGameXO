package org.hexlet.gamexo.gamefield.players;

import org.hexlet.gamexo.gamefield.GameField;
import org.hexlet.gamexo.gamefield.GameView;

public class PlayerHumanLocal implements IPlayer {

	private char signPlayer;

	public PlayerHumanLocal(char signPlayer) {
		this.signPlayer = signPlayer;
	}

	@Override
	public int[] doMove() {
		return GameView.getResultOnTouch();
	}

	@Override
	public boolean setMove(int cellX, int cellY, char signPlayer) {
		return GameField.setSignToCell(cellX, cellY, signPlayer);
	}

	public char getSignPlayer() {
		return signPlayer;
	}

}
