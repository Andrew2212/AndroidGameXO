package org.hexlet.gamexo.gamefield.players;

public interface IPlayer {
	/**
	 * 
	 * @return calculated move coordinates
	 */
    int[] doMove();
    
    /**
     * 
     * @param cellX move coordinate 'X'
     * @param cellY move coordinate 'Y'
     * <br>Set move into the 'fieldMatrix'
     */
    void setMove(int cellX, int cellY);
}
