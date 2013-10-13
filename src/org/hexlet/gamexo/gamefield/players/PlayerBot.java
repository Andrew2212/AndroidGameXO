package org.hexlet.gamexo.gamefield.players;

import org.hexlet.gamexo.ai.IBrainAI;
import org.hexlet.gamexo.ai.IPlayerBot;
import org.hexlet.gamexo.ai.WayEnum;
import org.hexlet.gamexo.ai.brutforceway.BrutforceAI;
import org.hexlet.gamexo.gamefield.Game;
import org.hexlet.gamexo.gamefield.GameField;

public class PlayerBot<T> implements IPlayer, IPlayerBot<T> {

	private static final int X = 0;
	private static final int Y = 1;
	private static int[] position = new int[2];
	private IBrainAI<?> iBrainAI;
	private WayEnum wayEnum;
	private char signPlayer;

	public PlayerBot(int fieldSize, int numChecked, char signPlayer) {
		this.signPlayer = signPlayer;
		wayEnum = Game.getWayEnum();

		switch (wayEnum) {

		case GARDNER:
			// iBrainAI = new Gardner(fieldSize, numChecked);
			iBrainAI = new BrutforceAI(fieldSize, numChecked);
			break;

		case MINIMAX:
			// iBrainAI = new Minimax(fieldSize, numChecked);
			iBrainAI = new BrutforceAI(fieldSize, numChecked);
			break;

		case SPARE:
			// iBrainAI = new Spare(fieldSize, numChecked);
			iBrainAI = new BrutforceAI(fieldSize, numChecked);
			break;

		case BRUTFORCE:
			iBrainAI = new BrutforceAI(fieldSize, numChecked);
			break;

		case NONE:
			// iBrainAI = new BrutforceAI(fieldSize, numChecked);
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int[] doMove() {

		do {
			position = getCoordinate(iBrainAI,
					(T[][]) GameField.getFieldMatrix(),
					((T) (Character) signPlayer));
		} while (GameField.getFieldMatrix()[position[X]][position[Y]] != GameField.VALUE_DEFAULT);

		return position;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int[] getCoordinate(IBrainAI iBrainAI, T[][] fieldMatrix, T figure) {
		position = iBrainAI.findMove(fieldMatrix, figure);
		System.out.println("PlayerBot::getCoordinate::position[X] = "
				+ position[X] + ", position[Y] = " + position[Y]);
		return position;
	}

	@Override
	public boolean setMove(int cellX, int cellY, char signPlayer) {
		return GameField.setSignToCell(cellX, cellY, signPlayer);
	}

	public char getSignPlayer() {
		return signPlayer;
	}

}
