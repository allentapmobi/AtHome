package in.tapmobi.athome;

import in.tapmobi.athome.sip.SipRegisteration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	public static TextView txtSipStatus;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

		txtSipStatus = (TextView) rootView.findViewById(R.id.txtUpdateStatus);
		txtSipStatus.setText(SipRegisteration.sUpdateStatus);
		return rootView;
	}

}
