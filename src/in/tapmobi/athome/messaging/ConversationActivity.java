package in.tapmobi.athome.messaging;

import in.tapmobi.athome.MessageLogsFragment;
import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.MessageAdapter;
import in.tapmobi.athome.models.ChatConversation;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.util.Utility;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class ConversationActivity extends SherlockActivity {
	ListView mMsgList;
	EditText etsendMessage;
	MessageAdapter mMsgAdapter;
	ImageButton btnSend;
	public static String txtMsg;
	public static String Name;
	public static String Number;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);

		// ------------------------------------------------------------------------------

		Intent i = getIntent();
		Name = i.getStringExtra("TEXT_NAME");
		Number = i.getStringExtra("TEXT_CONTACT_NUMBER");
		// ------------------------------------------------------------------------------
		// ACTION BAR SETUP
		// ------------------------------------------------------------------------------
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff472347));
		if (Name == null) {
			getSupportActionBar().setTitle(Number);
		} else {
			getSupportActionBar().setTitle(Name);
		}

		// ------------------------------------------------------------------------------

		btnSend = (ImageButton) findViewById(R.id.ibSend);
		etsendMessage = (EditText) findViewById(R.id.editMsgsBar);

		mMsgList = (ListView) findViewById(R.id.listMsgs);
		mMsgAdapter = new MessageAdapter(getApplicationContext(), R.layout.msg_list_item);
		mMsgList.setAdapter(mMsgAdapter);
		// Call ServerApi to initiate msgs

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				txtMsg = etsendMessage.getText().toString();
				if (txtMsg != null && !txtMsg.equals("")) {
					mMsgAdapter.add(new ChatConversation(false, txtMsg));
					etsendMessage.setText("");
					// Store txtMsg along with SenderName and Number
					new RegisterMsgLogs().execute();
				} else {
					Toast.makeText(ConversationActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
				}
			}
		});
		addItems();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	public class RegisterMsgLogs extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Utility.regInMsgLogs(Name, Number, txtMsg);
			try {
				Number.replaceAll("[\\s\\-()]", "");
				System.out.println(Number);
				boolean success = ServerAPI.sendSms(Number, txtMsg);
				if (success) {
					Toast.makeText(getApplicationContext(), "Msg sent successfully", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}
	}

	// Display items from database to View.

	private void addItems() {
		// mMsgAdapter.add(new ChatConversation(true, "Hello bubbles!"));

		for (int i = 0; i < MessageLogsFragment.msgBasedCtx.size(); i++) {
			boolean left = false;

			String words = MessageLogsFragment.msgBasedCtx.get(i).getTxtMsg();

			mMsgAdapter.add(new ChatConversation(left, words));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		MessageLogsFragment.msgBasedCtx.clear();
	}

}
