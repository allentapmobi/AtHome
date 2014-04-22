package in.tapmobi.athome;

import in.tapmobi.athome.adapter.ContactPickerAdapter;
import in.tapmobi.athome.registration.RegisterationActivity;
import in.tapmobi.athome.util.Utility;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SelectContactsForMsgActivity extends Activity {
	private ContactPickerAdapter mAdapter;
	ListView mListViewContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_picker_msg);

		initViews();
	}

	private void initViews() {

		mListViewContacts = (ListView) findViewById(R.id.lvContactPicker);
		if (RegisterationActivity.mContact.size() == 0) {
			RegisterationActivity.mContact.addAll(Utility.getContactsList());
		}
		mAdapter = new ContactPickerAdapter(SelectContactsForMsgActivity.this, RegisterationActivity.mContact);
		mListViewContacts.setAdapter(mAdapter);
	}

}
