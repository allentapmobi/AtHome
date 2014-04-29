package in.tapmobi.athome.registration;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;

public class VerificationActivity extends Activity {

	FormEditText etVerificationCode;
	Button btnVerify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);

		initViews();
	}

	private void initViews() {
		etVerificationCode = (FormEditText) findViewById(R.id.etVerifyNumber);
		btnVerify = (Button) findViewById(R.id.btnVerify);

		btnVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(VerificationActivity.this, MainActivity.class);
				startActivity(i);
			}
		});

	}

}
