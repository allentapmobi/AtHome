package in.tapmobi.athome;

import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.sip.SipRegisteration;
import in.tapmobi.athome.util.Utility;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CallLogsFragment extends Fragment {
	ListView lvCallLogs;
	CallLogsAdapter mLogAdapter;
	DataBaseHandler db;
	LinearLayout mSectionHeader;
	SipRegisteration sip;
	ArrayList<ContactsModel> mContacts = new ArrayList<ContactsModel>();
	// HashMap<String, CallLogs> groupedCallLogs = new HashMap<String,
	// CallLogs>();
	static String Msisdn, ContactName = null;

	/**
	 * Fragment creation for call logs
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);
		mSectionHeader = (LinearLayout) rootView.findViewById(R.id.sort_by_date);
		// Init Sip Manager
		
		sip = new SipRegisteration(getActivity().getApplicationContext());

		db = new DataBaseHandler(getActivity().getApplicationContext());
		// List<CallLogs> contacts = db.getAllContacts();

		// Reading all contacts
		Log.d("Reading: ", ".......Reading all contacts........");
		// logs = db.getAllCallLogs();
		// logs.addAll(Utility.getCallLogs());
		if (Utility.CallLogs.size() > 0)
			Utility.CallLogs.clear();
		Utility.CallLogs.addAll(Utility.getCallLogs());
		/**
		 * Loop to iterate the call logs based on call type and and time
		 * TODO:Once working fine create a method in Utility
		 */
		// for (int i = 0; i < Utility.CallLogs.size(); i++) {
		//
		// groupedCallLogs.put(Utility.CallLogs.get(i).getContactNumber(),
		// Utility.CallLogs.get(i));
		// Log.v("Hash Map", Utility.CallLogs.get(i).getContactNumber());
		// }

		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), Utility.CallLogs);
		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
		lvCallLogs.setAdapter(mLogAdapter);

		// Refresh call logs array.
		lvCallLogs.invalidateViews();

		lvCallLogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
				Msisdn = Utility.CallLogs.get(pos).getContactNumber();
				ContactName = Utility.CallLogs.get(pos).getContactName();
				if (Msisdn != null) {
					new RegisterCallLogsAsync().execute();
				}
			}
		});

		return rootView;
	}

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Utility.regInCallLogs(Msisdn);
			MainActivity.initSipManager();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			sip.initiateCall(Msisdn);
			Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
			i.putExtra("CONTACT_NAME", ContactName);
			i.putExtra("CONTACT_NUMBER", Msisdn);
			startActivity(i);
		}
	}
}
