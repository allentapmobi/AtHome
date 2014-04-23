package in.tapmobi.athome.adapter;

import in.tapmobi.athome.CallLogsFragment;
import in.tapmobi.athome.ContactsFragment;
import in.tapmobi.athome.DialpadFragment;
import in.tapmobi.athome.MessageLogsFragment;
import in.tapmobi.athome.ProfileFragment;
import in.tapmobi.athome.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	private static final String[] CONTENT = { "Call Logs", "DialPad", "Contacts", "Messages", "Profile" };
	private static final int[] ICONS = new int[] { R.drawable.pager_icon_clogs, R.drawable.pager_icon_dialpad, R.drawable.pager_icon_contacts, R.drawable.pager_icon_message,
			R.drawable.pager_icon_profile };

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
			// Message fragments
			return new MessageLogsFragment();

		case 4:
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
