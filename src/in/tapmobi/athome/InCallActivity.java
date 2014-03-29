package in.tapmobi.athome;

import in.tapmobi.athome.sip.SipRegisteration;
import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InCallActivity extends Activity {
	ImageView mProfileImage;
	ImageButton mCallEndBtn, mMuteBtn, mSpeakerBtn, mHoldBtn;
	TextView txtCallStatus, txtContactName;
	TextView txtMute, txtSpeaker, txtHold;

	String mName, mNumber = null;
	boolean isMute, isSpeaker, isHold = true;
	LinearLayout layoutMute, layoutHold, layoutSpeaker;

	SipRegisteration sip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incall);

		// Initialize SipManager
		// MainActivity.initSipManager();

		sip = new SipRegisteration(getApplicationContext());

		Intent intent = getIntent();
		mName = intent.getStringExtra("CONTACT_NAME");
		mNumber = intent.getStringExtra("CONTACT_NUMBER");

		initViews();
		if (SipRegisteration.mCall == null) {
			sip.initiateCall(mNumber);
		}

	}

	private void initViews() {

		mProfileImage = (ImageView) findViewById(R.id.profileImage);
		mCallEndBtn = (ImageButton) findViewById(R.id.btnEndcall);
		mMuteBtn = (ImageButton) findViewById(R.id.btnMute);
		mSpeakerBtn = (ImageButton) findViewById(R.id.btnSpeaker);
		mHoldBtn = (ImageButton) findViewById(R.id.btnHold);
		layoutHold = (LinearLayout) findViewById(R.id.layoutHoldButton);
		layoutMute = (LinearLayout) findViewById(R.id.layoutMuteButton);
		layoutSpeaker = (LinearLayout) findViewById(R.id.layoutSpeakerButton);

		txtCallStatus = (TextView) findViewById(R.id.txtCallStatus);
		txtContactName = (TextView) findViewById(R.id.txtContactName);
		txtMute = (TextView) findViewById(R.id.txtMute);
		txtSpeaker = (TextView) findViewById(R.id.txtSpeaker);
		txtHold = (TextView) findViewById(R.id.txtHold);

		if (mName == null) {
			txtContactName.setText(mNumber);
		} else {
			txtContactName.setText(mName);

		}

		mCallEndBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// End call
				sip.endCall();
				// Finish the activity
				finish();

			}
		});

		layoutMute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMute) {
					layoutMute.setBackgroundColor(getResources().getColor(R.color.darkgrey));
					txtMute.setTextColor(getResources().getColor(R.color.WhiteSmoke));
					isMute = false;
				} else {
					layoutMute.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
					txtMute.setTextColor(getResources().getColor(R.color.black));
					isMute = true;
				}

			}
		});

		layoutHold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isHold) {
					layoutHold.setBackgroundColor(getResources().getColor(R.color.darkgrey));
					txtHold.setTextColor(getResources().getColor(R.color.WhiteSmoke));
					isHold = false;
				} else {
					layoutHold.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
					txtHold.setTextColor(getResources().getColor(R.color.black));
					isHold = true;
				}

			}
		});

		layoutSpeaker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSpeaker) {
					layoutSpeaker.setBackgroundColor(getResources().getColor(R.color.darkgrey));
					txtSpeaker.setTextColor(getResources().getColor(R.color.WhiteSmoke));
					isSpeaker = false;
				} else {
					layoutSpeaker.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
					txtSpeaker.setTextColor(getResources().getColor(R.color.black));
					isSpeaker = true;
				}

			}
		});
	}

}