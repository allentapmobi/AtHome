package in.tapmobi.athome;

import in.tapmobi.athome.messaging.SelectContactsForMsgActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MessageLogsFragment extends Fragment {
	Button btnCompose;
	LinearLayout displayIfNoMsgs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_msg, container, false);

		btnCompose = (Button) rootView.findViewById(R.id.ComposeMsg);
		displayIfNoMsgs = (LinearLayout) rootView.findViewById(R.id.firstTimeLinear);
		btnCompose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), SelectContactsForMsgActivity.class);
				startActivity(i);
			}
		});

		return rootView;

	}
}
