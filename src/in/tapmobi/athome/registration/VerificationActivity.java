package in.tapmobi.athome.registration;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.R;
import in.tapmobi.athome.models.UserProfile;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import in.tapmobi.athome.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

public class VerificationActivity extends Activity {

	FormEditText etVerificationCode;
	Button btnVerify;
	SessionManager session;
	String Msisdn;
	Handler myHandler = new Handler();
	Runnable runnableReg, runnableVerify;
	public static RelativeLayout progressLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		session = new SessionManager(VerificationActivity.this);
		Intent i = getIntent();
		Msisdn = i.getStringExtra("MSISDN");

		initViews();
	}

	private void initViews() {

		progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
		progressLayout.setVisibility(View.GONE);
		etVerificationCode = (FormEditText) findViewById(R.id.etVerifyNumber);
		btnVerify = (Button) findViewById(R.id.btnVerify);

		btnVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utility.hideSoftKeyboard(VerificationActivity.this);

				String code = etVerificationCode.getText().toString();
				String generatedCode = session.getVerificationCode();

				if (code != null && !code.equals("") && code.equals(generatedCode)) {
					progressLayout.setVisibility(View.VISIBLE);
					getProfileForMsisdn(Msisdn);
					// Save the registered and verified number in shared preferences.
					session.createPhoneNumber(Msisdn);

				} else {

					Toast.makeText(VerificationActivity.this, "Wrong verification code", Toast.LENGTH_SHORT).show();

				}

			}

		});

	}

	private void getProfileForMsisdn(final String msisdn) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					UserProfile usrPofile = new UserProfile();
					usrPofile = ServerAPI.getUserProfile(msisdn);

					if (usrPofile.SipUsername != null) {
						// Store the profile in preferences locally
						session.createSipUserProfile(usrPofile);

						myHandler.post(runnableReg);

					} else {
						// registerNumber(msisdn);
					}
				} catch (Exception e) {
					myHandler.post(runnableVerify);

				}

			}
		}).start();

		runnableReg = new Runnable() {

			@Override
			public void run() {
				progressLayout.setVisibility(View.GONE);
				Intent i = new Intent(VerificationActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				VerificationActivity.this.finish();

			}
		};

		runnableVerify = new Runnable() {

			@Override
			public void run() {
				progressLayout.setVisibility(View.GONE);
				Intent i = new Intent(VerificationActivity.this, SubscriptionActivity.class);
				startActivity(i);
				VerificationActivity.this.finish();
			}

		};
	}

}
