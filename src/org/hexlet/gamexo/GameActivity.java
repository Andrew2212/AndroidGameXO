package org.hexlet.gamexo;

import org.hexlet.gamexo.gamefield.EnumEnemy;
import org.hexlet.gamexo.gamefield.GameView;
import org.hexlet.gamexo.gamefield.players.Player;
import org.hexlet.gamexo.utils.Logger;
import org.hexlet.gamexo.utils.Sounder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author Andrew2212
 * 
 */
public class GameActivity extends FragmentActivity implements OnClickListener {

	private static LinearLayout ltRadioGroup;
	private static LinearLayout ltGameCount;
	private static LinearLayout ltLevelDifficulty;
	private static LinearLayout ltConnectoinMode;

	private static RadioGroup radioGroup;
	private static TextView tvUserName;
	private static TextView tvEnemyName;
	private static TextView tvUserCount;//Count of the Win
//	*********************************************************************
	private static Player player = new Player();
//	********************************************************************
	private static TextView tvEnemyCount;//Count of the Win
	private static TextView tvDifficult;
	private TextView tvMode;

	private Button btnReset;

	private static int countSteps = 0;
	private static String strDifficult;
	private static EnumEnemy enemy;

	private static Context context;
	
	private static GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game);

		context = this;
		init();
		identificatioinModeGame();

		btnReset.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		countSteps = 0;
		setPrefsValue();
	}

	@Override
	public void onClick(View v) {
		Logger.v();
		Sounder.doSound(this, R.raw.beep);

		switch (v.getId()) {

		case R.id.btn_GameReset:
//			***************************************************************
			gameView.cellX = 2;
			gameView.cellY = 2;
			gameView.invalidate();			
//			***************************************************************
			// Reset Activity
//			Intent intent = getIntent();
//			finish();
//			startActivity(intent);
			break;

		default:
			break;
		}

	}
	
//	***************************************************************************
	public static void setOnTouchResult(String result){
		player.doMove(tvUserCount, result);
//		tvUserCount.setText(result);
	}
//	***************************************************************************

	/**
	 * Toggles tvPlayerName color in accordance with queue of steps 
	 */
	public static void switchPlayer() {
		Logger.v();
		int red = context.getResources().getColor(R.color.red);
		int green = context.getResources().getColor(R.color.green);

		if (countSteps % 2 == 0) {
			tvUserName.setTextColor(red);
			tvEnemyName.setTextColor(green);
		} else {
			tvUserName.setTextColor(green);
			tvEnemyName.setTextColor(red);
		}

		countSteps++;
		if (countSteps == 1 && enemy == EnumEnemy.BOT)
			doneFirstMoveVsBot();
	}

	// -------Private Methods----------------------

	private void identificatioinModeGame() {

		String namePutMode = getResources().getString(R.string.put_extra_enemy);
		enemy = (EnumEnemy) getIntent().getSerializableExtra(namePutMode);

		switch (enemy) {
		case HUMAN:
			setModeGameHuman();
			break;

		case BOT:
			seModeGameBot();
			break;

		case REMOTE:

			break;

		case REMOTE_BLUETOOTH:
			setModeGameBluetooth();
			break;

		case REMOTE_INET:
			setModeGameInet();
			break;

		default:
			break;
		}
	}

	// =============Set Mode View Value===================
	
	private void seModeGameBot() {
		tvEnemyName.setText(R.string.screen_vs_bot);
	}

	private void setModeGameHuman() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_human);
	}

	private void setModeGameBluetooth() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_remote);
		tvMode.setText(R.string.screen_bluetooth);
		ltConnectoinMode.setVisibility(View.VISIBLE);
	}

	private void setModeGameInet() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_remote);
		tvMode.setText(R.string.screen_inet);
		ltConnectoinMode.setVisibility(View.VISIBLE);
	}
	
	private static void doneFirstMoveVsBot() {
		getDifficulty();
		tvDifficult.setText(strDifficult);
		ltRadioGroup.setVisibility(View.GONE);
		ltGameCount.setVisibility(View.VISIBLE);
		ltLevelDifficulty.setVisibility(View.VISIBLE);
		ltConnectoinMode.setVisibility(View.GONE);
	}
	
	private void setModeExceptBot() {
		ltRadioGroup.setVisibility(View.GONE);
		ltGameCount.setVisibility(View.VISIBLE);
		ltLevelDifficulty.setVisibility(View.GONE);
	}

	// ============================================================

	/**
	 * Sets 'difficulty' from RadioGroup into 'strDifficult'
	 */
	private static void getDifficulty() {

		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.radio_Easy:
			strDifficult = context.getResources().getString(
					R.string.screen_easy);
			break;

		case R.id.radio_Middle:
			strDifficult = context.getResources().getString(
					R.string.screen_middle);
			break;

		case R.id.radio_Hard:
			strDifficult = context.getResources().getString(
					R.string.screen_hard);
			break;

		default:
			break;
		}

	}

	/**
	 * Sets value from 'Preferences' such as 'UserName', 'FieldSize' and etc. to
	 * SOMEWHERE
	 */
	private void setPrefsValue() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userName = prefs.getString(
				getResources().getString(R.string.pref_user_name_key),
				getResources().getString(R.string.pref_user_name_value));
		tvUserName.setText(userName);
	}

	private void init() {

		ltRadioGroup = (LinearLayout) findViewById(R.id.layout_GameRadioGroup);
		ltGameCount = (LinearLayout) findViewById(R.id.layout_GameCount);
		ltLevelDifficulty = (LinearLayout) findViewById(R.id.layout_GameLevelDifficulty);
		ltConnectoinMode = (LinearLayout) findViewById(R.id.layout_GameConnectionMode);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		tvUserName = (TextView) findViewById(R.id.tv_GameUserName);
		tvEnemyName = (TextView) findViewById(R.id.tv_GameEnemyName);
		tvUserCount = (TextView) findViewById(R.id.tv_GameUserCount);
		tvEnemyCount = (TextView) findViewById(R.id.tv_GameEnemyCount);
		tvDifficult = (TextView) findViewById(R.id.tv_GameDifficult);
		tvMode = (TextView) findViewById(R.id.tv_GameMode);

		btnReset = (Button) findViewById(R.id.btn_GameReset);
		
		gameView = (GameView) findViewById(R.id.game_view);

	}

}
