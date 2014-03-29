package in.tapmobi.athome.sip;

import in.tapmobi.athome.R;
import in.tapmobi.athome.session.SessionManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SipDetails extends Activity {
	// TextView txtUserName,txtPassword,txtDomain;
	EditText etUsername, etPassword, etDomain;
	Button btnSaveToPreference;
	public static String SIP_USERNAME, SIP_PASSWORD, SIP_DOMAIN;
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sip_details_activity);
		session = new SessionManager(getApplicationContext());
		initViews();
	}

	private void initViews() {
		etUsername = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etDomain = (EditText) findViewById(R.id.etSipDomain);
		btnSaveToPreference = (Button) findViewById(R.id.btnSubmit);

		btnSaveToPreference.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check if the edit box values has some values
				SIP_USERNAME = etUsername.getText().toString();
				SIP_DOMAIN = etDomain.getText().toString();
				SIP_PASSWORD = etPassword.getText().toString();
				if (SIP_DOMAIN != null && SIP_PASSWORD != null && SIP_USERNAME != null) {
					session.createSipProfile(SIP_USERNAME, SIP_DOMAIN, SIP_PASSWORD);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Please enter valid values", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

}
