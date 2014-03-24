package in.tapmobi.athome;

import java.text.ParseException;

import in.tapmobi.athome.adapter.TabsPagerAdapter;
import in.tapmobi.athome.sip.SipRegisteration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	SipRegisteration sipReg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		sipReg = new SipRegisteration(MainActivity.this);

		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAdapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// When we get back from the preference setting Activity, assume
		// settings have changed, and re-login with new auth info.
		try {
			sipReg.initializeManager();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (sipReg.mCall != null) {
			sipReg.mCall.close();
		}

		sipReg.closeLocalProfile();

		if (sipReg.callReceiver != null) {
			this.unregisterReceiver(sipReg.callReceiver);
		}
	}
}