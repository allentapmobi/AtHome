package in.tapmobi.athome.incomming;

import in.tapmobi.athome.R;
import in.tapmobi.athome.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

public class IncomingCallActivity extends Activity {
	ImageButton btnAnswer, btnDecline;
	TextView txtTimer, tvIncomingNumber;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	boolean isReceived = false;

	static Uri notification;
	static Ringtone r;
	String userName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incomming_call);

		Intent i = getIntent();
		userName = i.getStringExtra("INCALL_USER_NAME");

		initViews();
		/**
		 * EXPLICTLY wake up device when received an incoming call
		 */
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private void initViews() {
		btnAnswer = (ImageButton) findViewById(R.id.btnAnswercall);
		btnDecline = (ImageButton) findViewById(R.id.btnEndcall);
		txtTimer = (TextView) findViewById(R.id.txtTimeDuration);
		tvIncomingNumber = (TextView) findViewById(R.id.txtContactName);
		tvIncomingNumber.setText(userName);

		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		r = RingtoneManager.getRingtone(IncomingCallActivity.this, notification);
		r.play();

		if (tvIncomingNumber != null) {
			new RegisterCallLogsAsync().execute();
		}
		btnAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isReceived = true;
				// Start the timer
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);

				// IncomingWakefulService.answerIncomingCall();
				if (r.isPlaying()) {
					r.stop();
				}
			}
		});

		btnDecline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isReceived = true;
				// Pause the timer
				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);
				if (r.isPlaying()) {
					r.stop();
				}
				// IncomingWakefulService.rejectIncomingCall();
				finish();
			}
		});

	}

	private Runnable updateTimerThread = new Runnable() {

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

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (isReceived) {
				Utility.regInCallLogs(userName, 0);
			} else {
				Utility.regInCallLogs(userName, 2);
			}
			return null;
		}
	}

}
