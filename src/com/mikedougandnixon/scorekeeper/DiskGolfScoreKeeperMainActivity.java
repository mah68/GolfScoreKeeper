package com.mikedougandnixon.scorekeeper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DiskGolfScoreKeeperMainActivity extends Activity {
	private List<Player> players;

	private ListView playersListView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.playersListView = (ListView) findViewById(R.id.playersListView);
        players = new ArrayList<Player>();
        playersListView.setAdapter(new PlayerArrayAdapter(this, R.layout.playerview, players, 1));
        
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Add Player");
        menu.add(0, 1, 1, "View Whole Game");
        return result;
    }
    

    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case 0:
            this.showDialog(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = new Dialog(this);
        switch(id) {
        case 0:
        	dialog.setContentView(R.layout.playernamedialog);
        	Button addPlayerButton = (Button) dialog.findViewById(R.id.addPlayerButton);
        	addPlayerButton.setOnClickListener(new addPlayerListener(dialog, this));
        }
        return dialog;
    }
    
    private String[] makeArray() {
    	String[] s = new String[players.size()];
    	for (int i = 0; i < players.size(); i++) {
    		s[i] = players.get(i).getName();
    	}
    	return s;
    }
    
    public class addPlayerListener implements OnClickListener {
    	Dialog dialog;
    	Context context;
    	
    	public addPlayerListener(Dialog dialog, Context context) {
    		this.dialog = dialog;
    		this.context = context;
    	}

		public void onClick(View arg0) {
			EditText playerNameEditText = (EditText) dialog.findViewById(R.id.playerNameEditText);
			Player p = new Player(playerNameEditText.getText().toString());
			players.add(p);
			playersListView.setAdapter(new PlayerArrayAdapter(context, R.layout.playerview, players, 1));
	        playerNameEditText.setText("");
			dialog.dismiss();
		}
		
	}
    
}