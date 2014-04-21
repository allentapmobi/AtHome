package in.tapmobi.athome.subscription;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.R;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.server.UserProfile;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

public class SubscriptionActivity extends Activity implements OnClickListener {
	FormEditText etName, etEmail;
	CheckBox cb;
	TextView tvTermsConditions;
	Button btnSubscribe;
	SessionManager session;
	String verifiedMsisdn;
	Boolean mIsSubscribed;
	Handler myHandler = new Handler();
	Runnable runnableReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_subscription);

		initViews();
		session = new SessionManager(SubscriptionActivity.this);
		verifiedMsisdn = session.getUserPhoneNumber();

	}

	private void initViews() {
		tvTermsConditions = (TextView) findViewById(R.id.txtTerms);
		etName = (FormEditText) findViewById(R.id.etName);
		etEmail = (FormEditText) findViewById(R.id.etEmail);
		cb = (CheckBox) findViewById(R.id.checkBox1);
		btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
		btnSubscribe.setOnClickListener(this);
		tvTermsConditions.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSubscribe) {
			if (etName.testValidity() && etEmail.testValidity() && cb.isChecked()) {
				Utility.hideSoftKeyboard(SubscriptionActivity.this);
				String Name = etName.getText().toString();
				String Email = etEmail.getText().toString();
				// Checks if user has been registered to sip server
				registerSipUserThread(Name, Email, verifiedMsisdn);

			} else {
				Toast.makeText(SubscriptionActivity.this, "Select the Terms and Conditions.", Toast.LENGTH_SHORT).show();
			}
		} else if (v.getId() == R.id.txtTerms) {
			Intent i = new Intent(SubscriptionActivity.this, TermsAndConditions.class);
			startActivity(i);

		}
	}

	private void registerSipUserThread(String name, String email, String verifiedMsisdn2) {
		final String Name = name;
		final String Email = email;
		final String VerifiedMsisdn = verifiedMsisdn2;

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					UserProfile usrPofile = new UserProfile();
					usrPofile = ServerAPI.registerUserSip(Name, Email, VerifiedMsisdn);
					if (usrPofile.SipUsername != null) {
						// Store the profile in preferences locally
						session.createSipUserProfile(usrPofile);
					} else {
						Toast.makeText(SubscriptionActivity.this, "Unable to reterive data", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					myHandler.post(runnableReg);
				}
				myHandler.post(runnableReg);
			}
		}).start();
		runnableReg = new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(SubscriptionActivity.this, MainActivity.class);
				startActivity(i);
				SubscriptionActivity.this.finish();

			}
		};

	}
}
