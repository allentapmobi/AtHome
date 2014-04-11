package in.tapmobi.athome.adapter;

import com.viewpagerindicator.IconPagerAdapter;

import in.tapmobi.athome.CallLogsFragment;
import in.tapmobi.athome.ContactsFragment;
import in.tapmobi.athome.DialpadFragment;
import in.tapmobi.athome.ProfileFragment;
import in.tapmobi.athome.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	private static final String[] CONTENT = { "Call Logs", "DialPad", "Contacts", "Profile" };
	private static final int[] ICONS = new int[] { R.drawable.call_logs, R.drawable.call_logs, R.drawable.call_logs, R.drawable.call_logs };

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// RecentContacts fragment activity
			return new CallLogsFragment();
		case 1:
			// Favorites fragment activity
			return new DialpadFragment();
		case 2:
			// Contacts fragment activity
			return new ContactsFragment();
		case 3:
			// Profile fragments
			return new ProfileFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return CONTENT.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position % CONTENT.length].toUpperCase();

	}

	// public int getItemPosition(Object object) {
	// return POSITION_NONE;
	// }

	public int getIconResId(int index) {
		return ICONS[index];
	}

}
