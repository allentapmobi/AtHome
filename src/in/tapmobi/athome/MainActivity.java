package in.tapmobi.athome;

import in.tapmobi.athome.adapter.TabsPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		// Set up action bar.
//		final ActionBar actionBar = getSupportFragmentManager();

		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
//		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAdapter);
		
		
		// CallLogs l1 =new CallLogs();
		// l1.setCallDuration("20 mins ago");
		// l1.setContactName("Rajnikanth Jr");
		// l1.setContactNumber("1234345345");
		// sCallLogs.add(l1);
		//
		// CallLogs l2 = new CallLogs();
		// l2.setContactName("Leonardo");
		// l2.setContactNumber("560560");
		// l2.setCallDuration("10 mins ago");
		// sCallLogs.add(l2);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}
}