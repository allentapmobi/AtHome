package in.tapmobi.athome.incomming;

import in.tapmobi.athome.R;
import in.tapmobi.athome.sip.IncomingCallReceiver;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class IncomingCallActivity extends Activity {
	ImageButton btnAnswer, btnDecline;
	TextView txtTimer;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incomming_call);
		initViews();
	}

	private void initViews() {
		btnAnswer = (ImageButton) findViewById(R.id.btnAnswercall);
		btnDecline = (ImageButton) findViewById(R.id.btnEndcall);
		txtTimer = (TextView) findViewById(R.id.txtTimeDuration);

		btnAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start the timer
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);
				IncomingCallReceiver.answerIncomingCall();

			}
		});

		btnDecline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Pause the timer
				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

				IncomingCallReceiver.rejectIncomingCall();
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

}
