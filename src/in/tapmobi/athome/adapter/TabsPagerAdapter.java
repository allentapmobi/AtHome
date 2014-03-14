package in.tapmobi.athome.adapter;

import in.tapmobi.athome.calllogs.CallLogsFragment;
import in.tapmobi.athome.contacts.ContactsFragment;
import in.tapmobi.athome.dialpad.DialpadFragment;
import in.tapmobi.athome.userprofile.ProfileFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	private static final String[] CONTENT = { "Call Logs", "DialPad", "Contacts", "Profile" };

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
		return CONTENT[position % CONTENT.length];
	}

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
