package in.tapmobi.athome.calllogs;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.incall.InCallActivity;
import in.tapmobi.athome.models.CallLogs;

import java.util.ArrayList;

import android.content.Intent;
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
	ArrayList<CallLogs> logs;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);
		mSectionHeader = (LinearLayout) rootView.findViewById(R.id.sort_by_date);

		db = new DataBaseHandler(getActivity().getApplicationContext());
		// List<CallLogs> contacts = db.getAllContacts();

		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		logs = db.getAllCallLogs();

		// Sort based on time;

		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), logs);
		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
		lvCallLogs.setAdapter(mLogAdapter);
		lvCallLogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
				Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
				i.putExtra("CONTACT_NAME", logs.get(pos).getContactName());
				i.putExtra("CONTACT_NUMBER", logs.get(pos).getContactNumber());
				startActivity(i);
			}
		});

		return rootView;
	}

}
