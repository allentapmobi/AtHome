package in.tapmobi.athome.subscription;

import in.tapmobi.athome.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class TermsAndConditions extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terms_conditions);

	}

}
