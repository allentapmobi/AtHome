package in.tapmobi.athome.registration;

import in.tapmobi.athome.R;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import in.tapmobi.athome.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

public class VerificationActivity extends Activity {

	FormEditText etVerificationCode;
	Button btnVerify;
	SessionManager session;
	String Msisdn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		session = new SessionManager(VerificationActivity.this);

		initViews();
	}

	private void initViews() {
		etVerificationCode = (FormEditText) findViewById(R.id.etVerifyNumber);
		btnVerify = (Button) findViewById(R.id.btnVerify);

		btnVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utility.hideSoftKeyboard(VerificationActivity.this);

				String code = etVerificationCode.getText().toString();

				if (code != null && !code.equals("") && code.equals("4004")) {
					RegisterationActivity reg = new RegisterationActivity();
					Intent i = new Intent(VerificationActivity.this, SubscriptionActivity.class);
					startActivity(i);
					VerificationActivity.this.finish();

					reg.finish();

				} else {

					Toast.makeText(VerificationActivity.this, "Wrong verification code", Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

}
