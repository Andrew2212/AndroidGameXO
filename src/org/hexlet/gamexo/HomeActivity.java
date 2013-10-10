package org.hexlet.gamexo;

import org.hexlet.gamexo.gamefield.EnumEnemy;
import org.hexlet.gamexo.utils.Logger;
import org.hexlet.gamexo.utils.Sounder;
import org.hexlet.gamexo.utils.Toaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class HomeActivity extends FragmentActivity implements OnClickListener {

	TextView tvHiUser;
	Button btnGameVsHuman;
	Button btnGameVsBot;
	Button btnGameRemote;
	Button btnStatistic;
	Button btnPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);

		init();

	}

	@Override
	protected void onResume() {
		super.onResume();

		setUserName();
	}

	@Override
	public void onClick(View v) {

		Logger.v();
		Intent intent;
		String enemy = getResources().getString(R.string.put_extra_enemy);	
		
		switch (v.getId()) {

		case R.id.btn_HomeGameVsHuman:
			Sounder.doSound(this, R.raw.beep_notify);
			intent = new Intent(this, GameActivity.class);
			intent.putExtra(enemy, EnumEnemy.HUMAN);
			startActivity(intent);
			break;
			
		case R.id.btn_HomeGameVsBot:
			Sounder.doSound(this, R.raw.beep_notify);
			intent = new Intent(this, GameActivity.class);
			intent.putExtra(enemy, EnumEnemy.BOT);
			startActivity(intent);
			break;

		case R.id.btn_HomeGameRemote:
			Sounder.doSound(this, R.raw.beep);
			intent = new Intent(this, RemoteActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_HomePrefs:
			Sounder.doSound(this, R.raw.beep);
			intent = new Intent(this, Preferences.class);
			startActivity(intent);
			break;

		case R.id.btn_HomeStatistic:
			Sounder.doSound(this, R.raw.wilhelm_scream);
			Toaster.doToastShort(this, R.string.toast_nothing_happens);
			break;

		default:
			Toaster.doToastShort(this, R.string.toast_nothing_happens);
			break;
		}

	}
	
//	--------Private Methods---------------------
	
	private void init() {

		tvHiUser = (TextView) findViewById(R.id.tv_HomeHiUser);
		btnGameVsHuman = (Button) findViewById(R.id.btn_HomeGameVsHuman);
		btnGameVsBot = (Button) findViewById(R.id.btn_HomeGameVsBot);
		btnGameRemote = (Button) findViewById(R.id.btn_HomeGameRemote);
		btnStatistic = (Button) findViewById(R.id.btn_HomeStatistic);
		btnPrefs = (Button) findViewById(R.id.btn_HomePrefs);

		btnGameVsHuman.setOnClickListener(this);
		btnGameVsBot.setOnClickListener(this);
		btnGameRemote.setOnClickListener(this);
		btnStatistic.setOnClickListener(this);
		btnPrefs.setOnClickListener(this);
	}

	private void setUserName() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userName = prefs.getString(
				getResources().getString(R.string.pref_user_name_key),
				getResources().getString(R.string.pref_user_name_value));
		tvHiUser.setText(getResources().getString(R.string.screen_hi) + " "
				+ userName + "!");
	}
}
