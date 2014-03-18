package in.tapmobi.athome.incall;

import in.tapmobi.athome.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InCallActivity extends Activity {
	ImageView mProfileImage;
	ImageButton mCallEndBtn, mMuteBtn, mSpeakerBtn, mHoldBtn;
	TextView txtCallStatus, txtContactName;

	String mName, mNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incall);

		Intent intent = getIntent();
		mName = intent.getStringExtra("CONTACT_NAME");
		mNumber = intent.getStringExtra("CONTACT_NUMBER");

		initViews();
	}

	private void initViews() {

		mProfileImage = (ImageView) findViewById(R.id.profileImage);
		mCallEndBtn = (ImageButton) findViewById(R.id.btnEndcall);
		mMuteBtn = (ImageButton) findViewById(R.id.btnMute);
		mSpeakerBtn = (ImageButton) findViewById(R.id.btnSpeaker);
		mHoldBtn = (ImageButton) findViewById(R.id.btnHold);

		txtCallStatus = (TextView) findViewById(R.id.txtCallStatus);
		txtContactName = (TextView) findViewById(R.id.txtContactName);

		if (mName == null) {
			txtContactName.setText(mNumber);
		} else {
			txtContactName.setText(mName);

		}

		mCallEndBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}

}