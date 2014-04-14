package in.tapmobi.athome;

import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.sip.SipRegisteration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	TextView tvRegNumber, tvRegisterationStatus;
	ImageView ivRegIcon;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		tvRegNumber = (TextView) rootView.findViewById(R.id.txtRegNumber);
		tvRegisterationStatus = (TextView) rootView.findViewById(R.id.txtRegisterationStatus);
		ivRegIcon = (ImageView) rootView.findViewById(R.id.regStatus);
		
		
		SessionManager s = new SessionManager(getActivity());
		String userName = s.getSipUserName();
		tvRegNumber.setText(userName);

		if (SipRegisteration.isRegisteredWithSip) {
			ivRegIcon.setImageResource(R.drawable.success);
		} else {
			ivRegIcon.setImageResource(R.drawable.failure);
		}
		if (SipRegisteration.sUpdateStatus != null) {
			tvRegisterationStatus.setText(SipRegisteration.sUpdateStatus);
		}
		return rootView;
	}

}
