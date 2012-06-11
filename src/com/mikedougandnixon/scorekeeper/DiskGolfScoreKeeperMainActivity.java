package com.mikedougandnixon.scorekeeper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DiskGolfScoreKeeperMainActivity extends Activity {
	private static List<Player> players;
	private Integer holeNumber;
	private Context thisContext;
	private ListView playersListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisContext = this;
		setContentView(R.layout.main);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			holeNumber = (Integer) extras.get("holeNumber");
		} else {
			holeNumber = 1;
		}
		if (players == null)
			players = new ArrayList<Player>();

		playersListView = (ListView) findViewById(R.id.playersListView);
		playersListView.setAdapter(new PlayerArrayAdapter(this, R.layout.playerview, players, holeNumber));

		TextView currentHoleTextView = (TextView) findViewById(R.id.currentHoleTextView);
		Button previousHoleButton = (Button) findViewById(R.id.previousHoleButton);
		Button nextHoleButton = (Button) findViewById(R.id.nextHoleButton);

		currentHoleTextView.setText("Hole " + holeNumber);
		previousHoleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (holeNumber == 1) {
					holeNumber = 18;
				} else
					holeNumber -= 1;
				refreshView();
			}

		});
		nextHoleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (holeNumber == 18) {
					holeNumber = 1;
				} else
					holeNumber += 1;
				refreshView();
			}

		});

	}

	public void listItemLongPress(final Player p) {
		final Dialog confirmDialog = onCreateDialog(1);
		confirmDialog.dismiss();
//		TextView removePlayerTextView = (TextView) confirmDialog.findViewById(R.id.removePlayerTextView);
//		removePlayerTextView.setText("Remove " + p.getName() + " from game?");
		confirmDialog.setTitle("Remove " + p.getName() + " from game?");
		Button confirmButton = (Button) confirmDialog.findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				players.remove(p);
				confirmDialog.dismiss();
				refreshView();
			}

		});
		Button denyButton = (Button) confirmDialog.findViewById(R.id.denyButton);
		denyButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				confirmDialog.dismiss();
			}
		});
		confirmDialog.show();

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

	private void refreshView() {
		TextView holeTextView = (TextView) findViewById(R.id.currentHoleTextView);
		holeTextView.setText("Hole " + holeNumber);
		playersListView.setAdapter(new PlayerArrayAdapter(thisContext, R.layout.playerview, players, holeNumber));
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
		switch (id) {
		case 0:
			dialog.setContentView(R.layout.playernamedialog);
			dialog.setTitle("Player Name:");
			Button addPlayerButton = (Button) dialog.findViewById(R.id.addPlayerButton);
			addPlayerButton.setOnClickListener(new addPlayerListener(dialog, this));
			break;
		case 1:
			dialog.setContentView(R.layout.removeplayerdialog);
			break;
		}
		return dialog;
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

	public class PlayerArrayAdapter extends ArrayAdapter<Player> {
		private List<Player> playerList;
		private int viewId;
		private int holeNumber;

		public PlayerArrayAdapter(Context c, int viewId, List<Player> p, int holeNumber) {
			super(c, viewId, p);
			playerList = p;
			this.viewId = viewId;
			this.holeNumber = holeNumber;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater li = getLayoutInflater();
			View v = li.inflate(viewId, parent, false);

			TextView totalScore = (TextView) v.findViewById(R.id.totalScoreTextView);
			TextView holeScore = (TextView) v.findViewById(R.id.holeScoreTextView);
			TextView playerName = (TextView) v.findViewById(R.id.playerNameTextView);
			Button scoreUpButton = (Button) v.findViewById(R.id.scoreUpButton);
			Button scoreDownButton = (Button) v.findViewById(R.id.scoreDownButton);

			final Player p = playerList.get(position);

			scoreUpButton.setOnClickListener(new PlayerButtonOnClickListener(totalScore, holeScore, p, 1, holeNumber));
			scoreDownButton
					.setOnClickListener(new PlayerButtonOnClickListener(totalScore, holeScore, p, -1, holeNumber));
			playerName.setOnLongClickListener(new View.OnLongClickListener() {

				public boolean onLongClick(View v) {
					listItemLongPress(p);
					return true;
				}
				
			});

			totalScore.setText("" + p.getTotalScore());
			holeScore.setText("" + p.getScore(holeNumber));
			playerName.setText(p.getName());

			return v;
		}

		private class PlayerButtonOnClickListener implements OnClickListener {

			TextView totalScore;
			TextView holeScore;
			Player p;
			int delta;
			int holeNumber;

			public PlayerButtonOnClickListener(TextView totalScore, TextView holeScore, Player p, int delta,
					int holeNumber) {
				this.totalScore = totalScore;
				this.holeScore = holeScore;
				this.p = p;
				this.delta = delta;
				this.holeNumber = holeNumber;
			}

			public void onClick(View arg0) {
				int newScore = p.getScore(holeNumber) + delta;
				p.setScore(holeNumber, newScore);
				holeScore.setText("" + newScore);
				totalScore.setText("" + p.getTotalScore());
			}
		}

	}

}