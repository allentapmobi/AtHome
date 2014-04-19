package in.tapmobi.athome.subscription;

import in.tapmobi.athome.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SubscriptionActivity extends Activity {
	EditText etName, etEmail;
	CheckBox cb;
	Button btnSubscribe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_subscription);

		initViews();

	}

	private void initViews() {
		
		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		cb = (CheckBox) findViewById(R.id.checkBox1);
		btnSubscribe = (Button) findViewById(R.id.btnSubscribe);

	}

}
