package org.hexlet.gamexo.gamefield.players;

import org.hexlet.gamexo.gamefield.GameView;

public class PlayerHumanLocal implements IPlayer {


	@Override
	public int[] doMove() {
		return GameView.getResultOnTouch();
	}

	@Override
	public void setMove(int cellX, int cellY) {

	}

}
