package in.tapmobi.athome;

import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.CallLog;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.sip.SipRegisteration;
import in.tapmobi.athome.util.Utility;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CallLogsFragment extends Fragment {
	ListView lvCallLogs;
	CallLogsAdapter mLogAdapter;
	DataBaseHandler db;
	// SipRegisteration sip;
	ArrayList<ContactsModel> mContacts = new ArrayList<ContactsModel>();
	public static ArrayList<CallLog> logs = new ArrayList<CallLog>();
	// HashMap<String, CallLogs> groupedCallLogs = new HashMap<String,
	// CallLogs>();

	// Display information for incall activity
	static String Msisdn, ContactName = null;
	public static Bitmap sPhotoImg;
	public static Uri sPhotoUri;

	/**
	 * Fragment creation for call logs
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);
		// Init Sip Manager for the first time if not it will not register.
		MainActivity.initSipManager();

		// sip = new SipRegisteration(getActivity().getApplicationContext());
		db = new DataBaseHandler(getActivity().getApplicationContext());

		if (Utility.CallLogs.size() > 0)
			Utility.CallLogs.clear();
		Utility.CallLogs.addAll(Utility.getCallLogs());

		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), Utility.CallLogs);
		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
		lvCallLogs.setAdapter(mLogAdapter);
		mLogAdapter.notifyDataSetChanged();

		// Refresh call logs array.
		lvCallLogs.invalidateViews();

		lvCallLogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
				Msisdn = Utility.CallLogs.get(pos).getContactNumber();
				ContactName = Utility.CallLogs.get(pos).getContactName();
				sPhotoImg = Utility.CallLogs.get(pos).getContactPhoto();
				sPhotoUri = Utility.CallLogs.get(pos).getContactPhotoUri();
				if (Msisdn != null) {
					new RegisterCallLogsAsync().execute();
				}
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
		if (Utility.CallLogs.size() > 0)
			Utility.CallLogs.clear();
		Utility.CallLogs.addAll(Utility.getCallLogs());

		// ((BaseAdapter) lvCallLogs.getAdapter()).notifyDataSetChanged();
		mLogAdapter.notifyDataSetChanged();
	}

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {
				Utility.regInCallLogs(Msisdn, 1);
				// MainActivity.initSipManager();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {
				// sip.initiateCall(Msisdn);
				Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
				i.putExtra("CONTACT_NAME", ContactName);
				i.putExtra("CONTACT_NUMBER", Msisdn);
				i.putExtra("BITMAP", sPhotoImg);
				// i.putExtra("IMAGE_URI", sPhotoUri.toString());
				startActivity(i);
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "SIP Registration failed.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}

	public void refresh() {
		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), Utility.CallLogs);
		lvCallLogs.setAdapter(mLogAdapter);
	}
}
