package in.tapmobi.athome;

import in.tapmobi.athome.sip.SipRegisteration;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class InCallActivity extends Activity {
	ImageView mProfileImage;
	ImageButton mCallEndBtn, mMuteBtn, mSpeakerBtn;
	public static TextView txtCallStatus;
	TextView txtContactName;
	static TextView txtTimer;

	public static MediaPlayer beeper;
	String mName, mNumber = null;
	boolean isMute, isSpeaker = true;
	LinearLayout layoutMute, layoutHold, layoutSpeaker;
	ToggleButton tglMute, tglSpeaker;

	private static long startTime = 0L;
	private static Handler customHandler = new Handler();
	static long timeInMilliseconds = 0L;
	static long timeSwapBuff = 0L;
	static long updatedTime = 0L;

	Bitmap profileImage;
	Uri profileUri;
	SipRegisteration sip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incall);

		// Initialize SipManager
		// MainActivity.initSipManager();

		sip = new SipRegisteration(InCallActivity.this, InCallActivity.this);

		try {
			Intent intent = getIntent();
			mName = intent.getStringExtra("CONTACT_NAME");
			mNumber = intent.getStringExtra("CONTACT_NUMBER");
			profileImage = (Bitmap) intent.getParcelableExtra("BITMAP");
			profileUri = Uri.parse(intent.getExtras().getString("IMAGE_URI"));
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.e("InCallActivity", "Null pointer with image uri");
		}

		initViews();
		// beeper.prepare();
		// beeper.start();
		// if (SipRegisteration.mCall == null) {
		sip.initiateCall(mNumber);
		// }

		// Start the timer
		// StartTimer();
	}

	public static void StartTimer() {
		// TODO Auto-generated method stub
		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);
	}

	private void initViews() {

		beeper = MediaPlayer.create(InCallActivity.this, R.raw.comedywithkapil);
		beeper.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);

		mProfileImage = (ImageView) findViewById(R.id.profileImage);
		mCallEndBtn = (ImageButton) findViewById(R.id.btnEndcall);

		tglSpeaker = (ToggleButton) findViewById(R.id.toggleSpeaker);
		tglMute = (ToggleButton) findViewById(R.id.toggleMute);

		// mMuteBtn = (ImageButton) findViewById(R.id.btnMute);
		// mSpeakerBtn = (ImageButton) findViewById(R.id.btnSpeaker);
		// mHoldBtn = (ImageButton) findViewById(R.id.btnHold);
		// layoutHold = (LinearLayout) findViewById(R.id.layoutHoldButton);
		// layoutMute = (LinearLayout) findViewById(R.id.layoutMuteButton);
		// layoutSpeaker = (LinearLayout) findViewById(R.id.layoutSpeakerButton);

		txtCallStatus = (TextView) findViewById(R.id.txtCallStatus);
		txtContactName = (TextView) findViewById(R.id.txtContactName);

		// txtMute = (TextView) findViewById(R.id.txtMute);
		// txtSpeaker = (TextView) findViewById(R.id.txtSpeaker);
		// txtHold = (TextView) findViewById(R.id.txtHold);

		txtTimer = (TextView) findViewById(R.id.txtTimeDuration);

		if (mName == null) {
			txtContactName.setText(mNumber);
		} else {
			txtContactName.setText(mName);
		}

		// mProfileImage.setImageBitmap(profileImage);
		if (profileUri != null)
			mProfileImage.setImageURI(profileUri);

		mCallEndBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Pause the timer
				// timeSwapBuff += timeInMilliseconds;
				timeSwapBuff = 0;
				customHandler.removeCallbacks(updateTimerThread);
				try {
					// End call
					sip.endCall();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Finish the activity
				finish();

			}
		});
		tglMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && SipRegisteration.mCall.isMuted()) {
					SipRegisteration.mCall.toggleMute();
				} else {
					SipRegisteration.mCall.toggleMute();
				}

			}
		});

		tglSpeaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SipRegisteration.mCall.setSpeakerMode(isChecked);

			}
		});
		// layoutMute.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (isMute) {
		// layoutMute.setBackgroundColor(getResources().getColor(R.color.darkgrey));
		// txtMute.setTextColor(getResources().getColor(R.color.WhiteSmoke));
		// isMute = false;
		// SipRegisteration.mCall.toggleMute();
		//
		// } else {
		// layoutMute.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
		// txtMute.setTextColor(getResources().getColor(R.color.black));
		// isMute = true;
		// SipRegisteration.mCall.toggleMute();
		// }
		//
		// }
		// });

		// layoutHold.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (isHold) {
		// layoutHold.setBackgroundColor(getResources().getColor(R.color.darkgrey));
		// txtHold.setTextColor(getResources().getColor(R.color.WhiteSmoke));
		// isHold = false;
		// } else {
		// layoutHold.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
		// txtHold.setTextColor(getResources().getColor(R.color.black));
		// isHold = true;
		// }
		//
		// }
		// });

		// layoutSpeaker.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (isSpeaker) {
		// layoutSpeaker.setBackgroundColor(getResources().getColor(R.color.darkgrey));
		// txtSpeaker.setTextColor(getResources().getColor(R.color.WhiteSmoke));
		// isSpeaker = false;
		// SipRegisteration.mCall.setSpeakerMode(false);
		// } else {
		// layoutSpeaker.setBackgroundColor(getResources().getColor(R.color.theme_button_selector));
		// txtSpeaker.setTextColor(getResources().getColor(R.color.black));
		// isSpeaker = true;
		// SipRegisteration.mCall.setSpeakerMode(true);
		// }
		//
		// }
		// });
	}

	private static Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			txtTimer.setText("" + mins + ":" + String.format("%02d", secs));
			customHandler.postDelayed(this, 0);
		}

	};
}