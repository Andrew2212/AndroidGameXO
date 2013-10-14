package org.hexlet.gamexo.gamefield.players;

import org.hexlet.gamexo.GameActivity;
import org.hexlet.gamexo.gamefield.GameView;
import org.hexlet.gamexo.utils.Toaster;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author Andrew2212
 * 
 */
public class HandlerNotHumanLocalMove extends Handler {

	public static final int MSG_BOT = 222;
	public static final int MSG_BLUETOOTH = 333;

	private GameView gameView;
	private IPlayer currentPlayer;
	private Context context;// for the some Toasts

	public HandlerNotHumanLocalMove(Context context, GameView gameView,
			IPlayer currentPlayer) {
		this.context = context;
		this.gameView = gameView;
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void handleMessage(Message msg) {

		switch (msg.what) {
		case (MSG_BOT):
			handleMessageBot(msg);
			break;

		case (MSG_BLUETOOTH):
			handleMessageBluetooth(msg);
			break;
		}
	}

	private void handleMessageBot(Message msg) {

		int[] data = (int[]) msg.getData().getIntArray(PlayerBot.KEY_MOVE);
		// Set 'the best AI move' into fieldMatrix
		currentPlayer.setMove(data[0], data[1], currentPlayer.getSignPlayer());
		// Refresh GameView
		gameView.invalidate();
		// Switch Players
		GameActivity.switchPlayer();
	}

	private void handleMessageBluetooth(Message msg) {
		// TODO bluetooth handle method
		Toaster.doToastShort(context, "Handle Bluetooth Mesage");
	}

}
