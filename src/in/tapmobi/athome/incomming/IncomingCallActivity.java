package in.tapmobi.athome.incomming;

import in.tapmobi.athome.R;
import in.tapmobi.athome.sip.IncomingCallReceiver;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class IncomingCallActivity extends Activity {
	ImageButton btnAnswer, btnDecline;

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

		btnAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IncomingCallReceiver.answerIncomingCall();

			}
		});

		btnDecline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				IncomingCallReceiver.rejectIncomingCall();
				finish();
			}
		});

	}

}
