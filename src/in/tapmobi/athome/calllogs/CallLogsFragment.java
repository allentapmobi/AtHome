package in.tapmobi.athome.calllogs;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.CallLogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CallLogsFragment extends Fragment {
	ListView lvCallLogs;
	CallLogsAdapter mLogAdapter;
	DataBaseHandler db;
	LinearLayout mSectionHeader;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);
		mSectionHeader = (LinearLayout) rootView.findViewById(R.id.sort_by_date);

		db = new DataBaseHandler(getActivity().getApplicationContext());
		// List<CallLogs> contacts = db.getAllContacts();

		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		ArrayList<CallLogs> logs = db.getAllCallLogs();

		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), logs);
		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
		lvCallLogs.setAdapter(mLogAdapter);

		return rootView;
	}

}
