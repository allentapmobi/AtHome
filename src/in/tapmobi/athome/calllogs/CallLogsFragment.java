package in.tapmobi.athome.calllogs;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.models.CallLogs;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CallLogsFragment extends Fragment {
	ListView lvCallLogs;
	CallLogsAdapter mLogAdapter;
	ArrayList<CallLogs> mCallLogs = new ArrayList<CallLogs>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);
		
		
		CallLogs logs =new CallLogs();
		logs.setCallDuration("20 mins ago");
		logs.setContactName("Rajnikanth Jr");
		logs.setContactNumber("1234345345");
		mCallLogs.add(logs);
		
//		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(),mCallLogs);
//		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
//		lvCallLogs.setAdapter(mLogAdapter);

		return rootView;
	}

}
