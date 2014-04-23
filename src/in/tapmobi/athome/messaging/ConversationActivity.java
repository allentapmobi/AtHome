package in.tapmobi.athome.messaging;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.MessageAdapter;
import in.tapmobi.athome.models.Message;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class ConversationActivity extends SherlockActivity {
	ListView mMsgList;
	EditText etsendMessage;
	MessageAdapter mMsgAdapter;
	ImageButton btnSend;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);

		Intent i = getIntent();
		String Name = i.getStringExtra("TEXT_NAME");
		String Number = i.getStringExtra("TEXT_NUMBER");

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff472347));
		if (Name == null) {
			getSupportActionBar().setTitle(Number);
		} else {
			getSupportActionBar().setTitle(Name);
		}
		btnSend = (ImageButton) findViewById(R.id.ibSend);
		etsendMessage = (EditText) findViewById(R.id.editMsgsBar);

		mMsgList = (ListView) findViewById(R.id.listMsgs);
		mMsgAdapter = new MessageAdapter(getApplicationContext(), R.layout.msg_list_item);
		mMsgList.setAdapter(mMsgAdapter);

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String txtMsg = etsendMessage.getText().toString();
				if (txtMsg != null) {
					mMsgAdapter.add(new Message(false, etsendMessage.getText().toString()));
					etsendMessage.setText("");
				}
			}
		});

		// etsendMessage.setOnKeyListener(new OnKeyListener() {
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // If the event is a key-down event on the "enter" button
		// if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		// // Perform action on key press
		// mMsgAdapter.add(new Message(false, etsendMessage.getText().toString()));
		// etsendMessage.setText("");
		// return true;
		// }
		// return false;
		// }
		// });

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(menuItem));
	}

}
