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
import android.view.Window;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	DisplayMetrics dm = new DisplayMetrics();
	public static int width, height;

	public static SipRegisteration sipReg;
	public IncomingCallReceiver callReceiver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.pager_title_strip);
		indicator.setViewPager(mViewPager);

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

	// *******************************************************************************************************************************************\\
	private Toast toast;
	private long lastBackPressTime = 0;
	static boolean isFirstTime = true;

	@Override
	public void onBackPressed() {

		// show message to exit if user presses back two times
		if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
			toast = Toast.makeText(this, "Press back again Exit", Toast.LENGTH_SHORT);
			toast.show();
			this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
			isFirstTime = true;

		}
	}
}