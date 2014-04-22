package in.tapmobi.athome;

import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.sip.SipRegisteration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
	TextView tvRegNumber, tvRegisterationStatus, tvRegName;
	ImageView ivRegIcon;
	Button btnReferesh;
	SessionManager session;
	String ValidDate;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		session = new SessionManager(getActivity());
		ValidDate = session.getValidityDate();
		tvRegNumber = (TextView) rootView.findViewById(R.id.txtRegNumber);
		tvRegisterationStatus = (TextView) rootView.findViewById(R.id.txtRegisterationStatus);
		tvRegName = (TextView) rootView.findViewById(R.id.txtRegName);
		ivRegIcon = (ImageView) rootView.findViewById(R.id.regStatus);
		btnReferesh = (Button) rootView.findViewById(R.id.buttonReferesh);
		btnReferesh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), ValidDate, Toast.LENGTH_SHORT).show();
			}
		});

		SessionManager s = new SessionManager(getActivity());
		String userName = s.getSipUserName();
		String regName = s.getName();
		tvRegNumber.setText(userName);
		tvRegName.setText(regName);

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
