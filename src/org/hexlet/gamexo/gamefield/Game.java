package org.hexlet.gamexo.gamefield;

import org.hexlet.gamexo.GameActivity;
import org.hexlet.gamexo.ai.WayEnum;
import org.hexlet.gamexo.gamefield.players.IPlayer;
import org.hexlet.gamexo.gamefield.players.PlayerBot;
import org.hexlet.gamexo.gamefield.players.PlayerHumanLocal;

public class Game {

	private static EnumEnemy enumEmemy;
	private static WayEnum wayEnum;

	private static int fieldSize;
	private static int numCheckedSigns;
	private static GameFieldController gameFieldController;

	private IPlayer playerUser;
	private IPlayer playerEnemy;
	
	private char signPlayerUser;
	private char signPlayerEnemy;

	public Game(EnumEnemy enumEmemy, WayEnum wayEnum) {

		Game.enumEmemy = enumEmemy;
		Game.wayEnum = wayEnum;
		Game.fieldSize = GameView.getFieldSizeCalculated();
		Game.numCheckedSigns = GameView.getNumCheckedSigns();

		// Logger.v("*******Game CONSTRUCTOR*******");
		// Logger.v("*******Game::wayEnum = " + wayEnum);
		// Logger.v("*******Game::enumEmemy = " + enumEmemy);
		// Logger.v("*******Game::fieldSize = " + fieldSize);
		// Logger.v("*******Game::numCheckedSigns = " + numCheckedSigns);

		// Create new fieldMatrix and default fill it
		GameField.initNewFieldMatrix(fieldSize);

		// Create new Controller
		gameFieldController = new GameFieldController(fieldSize,
				numCheckedSigns);

		// Create playerUser (local human)
		signPlayerUser = GameActivity.getSignPlayerUser();
		playerUser = new PlayerHumanLocal(signPlayerUser);

		// Create playerEnemy
		signPlayerEnemy = specifySignPlayerEnemy(signPlayerUser);
		chooseAndInitEnemy(signPlayerEnemy);
	}

	public GameFieldController getGameFieldController() {
		return gameFieldController;
	}
	
	private char specifySignPlayerEnemy(char signPlayerUser){
		if(signPlayerUser == GameField.VALUE_X){
			return GameField.VALUE_O;
		}
		return GameField.VALUE_X;
	}

	private <T> void chooseAndInitEnemy(char signPlayerEnemy) {

		switch (enumEmemy) {
		case HUMAN:
			playerEnemy = new PlayerHumanLocal(signPlayerEnemy);
			break;

		case BOT:
			playerEnemy = new PlayerBot<T>(fieldSize, numCheckedSigns, signPlayerEnemy);
			break;

		case REMOTE:

			break;

		case REMOTE_BLUETOOTH:

			break;

		case REMOTE_INET:

			break;

		default:
			break;
		}
	}

	// ------------Getters and Setters--------------

	public IPlayer getPlayerUser() {
		return playerUser;
	}

	public void setPlayerUser(IPlayer player) {
		this.playerUser = player;
	}

	public IPlayer getPlayerEnemy() {
		return playerEnemy;
	}

	public void setPlayerEnemy(IPlayer playerEnemy) {
		this.playerEnemy = playerEnemy;
	}

	public static EnumEnemy getEnumEmemy() {
		return enumEmemy;
	}

	public static void setEnumEmemy(EnumEnemy enumEmemy) {
		Game.enumEmemy = enumEmemy;
	}

	public static WayEnum getWayEnum() {
		return wayEnum;
	}

	public static void setWayEnum(WayEnum wayEnum) {
		Game.wayEnum = wayEnum;
	}

	public static int getFieldSize() {
		return fieldSize;
	}

	public static void setFieldSize(int fieldSize) {
		Game.fieldSize = fieldSize;
	}

	public static int getNumCheckedSigns() {
		return numCheckedSigns;
	}

	public static void setNumCheckedSigns(int numCheckedSigns) {
		Game.numCheckedSigns = numCheckedSigns;
	}

}
