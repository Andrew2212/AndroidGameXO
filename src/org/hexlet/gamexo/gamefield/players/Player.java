package org.hexlet.gamexo.gamefield.players;

import android.widget.TextView;

public  class Player {

	public Player() {
		
	}
	
	public void doMove(TextView tv, String string){
		tv.setText(string);
	}

}
