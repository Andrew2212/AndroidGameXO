package org.hexlet.gamexo.gamefield.players;

public interface IPlayer {
	/**
	 * 
	 * @return calculated move coordinates
	 */
	int[] doMove();

	/**
	 * 
	 * @param cellX
	 *            move coordinate 'X'
	 * @param cellY
	 *            move coordinate 'Y' <br>
	 *            Set move into the 'fieldMatrix'
	 * @return GameField.setSignToCell(cellX, cellY, signPlayer)
	 */
	boolean setMove(int cellX, int cellY, char signPlayer);

	char getSignPlayer();
}
