package in.tapmobi.athome.calllogs;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CallLogsAdapter;
import in.tapmobi.athome.dialpad.DialpadFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CallLogsFragment extends Fragment {
	ListView lvCallLogs;
	CallLogsAdapter mLogAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calllogs, container, false);

		mLogAdapter = new CallLogsAdapter(getActivity().getApplicationContext(), DialpadFragment.sLogs);
		lvCallLogs = (ListView) rootView.findViewById(R.id.lvCallLogs);
		lvCallLogs.setAdapter(mLogAdapter);

		return rootView;
	}

}
