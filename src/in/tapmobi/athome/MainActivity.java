package in.tapmobi.athome;

import in.tapmobi.athome.adapter.TabsPagerAdapter;
import in.tapmobi.athome.sip.IncomingCallReceiver;
import in.tapmobi.athome.sip.SipRegisteration;

import java.text.ParseException;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	DisplayMetrics dm = new DisplayMetrics();
	public static int width, height;

	public static SipRegisteration sipReg;
	public IncomingCallReceiver callReceiver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		sipReg = new SipRegisteration(MainActivity.this);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;

		// Set up the intent filter. This will be used to fire an
		// IncomingCallReceiver when someone calls the SIP address used by this
		// application.
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.AtHome.INCOMING_CALL");
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);

		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAdapter);

	}

	public static void initSipManager() {
		// When we get back from the preference setting Activity, assume
		// settings have changed, and re-login with new auth info.
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sipReg.initializeManager();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void delSipProfile() {
		if (SipRegisteration.mCall != null) {
			SipRegisteration.mCall.close();
		}
		sipReg.closeLocalProfile();
		if (callReceiver != null) {
			this.unregisterReceiver(callReceiver);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initSipManager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		delSipProfile();
	}
}