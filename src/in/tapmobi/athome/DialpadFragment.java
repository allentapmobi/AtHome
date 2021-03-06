package in.tapmobi.athome;

import in.tapmobi.athome.adapter.DialpadAdapter;
import in.tapmobi.athome.sip.SipRegisteration;
import in.tapmobi.athome.util.Utility;

import java.util.Date;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialpadFragment extends Fragment implements View.OnClickListener {
	Date date;
	// TextView txtPhoneNo;
	EditText txtPhoneNo;
	Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnCall, btnClear, btnHash, btnAstrix;
	private String phoneNumber = "";
	String UserName = null;
	Utility util;
	DialpadAdapter dialAdapter;

	// SipRegisteration sip;

	// ArrayList<CallLogs> sLogs = new ArrayList<CallLogs>();
	// int count = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dialpad, container, false);

		// sip = new SipRegisteration(getActivity().getApplicationContext());

		txtPhoneNo = (EditText) rootView.findViewById(R.id.txtPhoneNo);
		txtPhoneNo.setText("");
		txtPhoneNo.setKeyListener(null);

		// txtPhoneNo.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int count) {
		// dialAdapter.getFilter().filter(s.toString());
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		util = new Utility(getActivity().getApplicationContext());
		btn1 = (Button) rootView.findViewById(R.id.btnNum1);
		btn1.setOnClickListener(this);
		btn2 = (Button) rootView.findViewById(R.id.btnNum2);
		btn2.setOnClickListener(this);
		btn3 = (Button) rootView.findViewById(R.id.btnNum3);
		btn3.setOnClickListener(this);
		btn4 = (Button) rootView.findViewById(R.id.btnNum4);
		btn4.setOnClickListener(this);
		btn5 = (Button) rootView.findViewById(R.id.btnNum5);
		btn5.setOnClickListener(this);
		btn6 = (Button) rootView.findViewById(R.id.btnNum6);
		btn6.setOnClickListener(this);
		btn7 = (Button) rootView.findViewById(R.id.btnNum7);
		btn7.setOnClickListener(this);
		btn8 = (Button) rootView.findViewById(R.id.btnNum8);
		btn8.setOnClickListener(this);
		btn9 = (Button) rootView.findViewById(R.id.btnNum9);
		btn9.setOnClickListener(this);
		btn0 = (Button) rootView.findViewById(R.id.btnNum0);
		btn0.setOnClickListener(this);
		btnCall = (Button) rootView.findViewById(R.id.btnCall);
		btnCall.setOnClickListener(this);
		btnClear = (Button) rootView.findViewById(R.id.btnClear);
		btnClear.setOnClickListener(this);
		btnHash = (Button) rootView.findViewById(R.id.btnHash);
		btnHash.setOnClickListener(this);
		btnAstrix = (Button) rootView.findViewById(R.id.btnAstrix);
		btnAstrix.setOnClickListener(this);

		return rootView;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnNum0:
		case R.id.btnNum1:
		case R.id.btnNum2:
		case R.id.btnNum3:
		case R.id.btnNum4:
		case R.id.btnNum5:
		case R.id.btnNum6:
		case R.id.btnNum7:
		case R.id.btnNum8:
		case R.id.btnNum9:
		case R.id.btnHash:
		case R.id.btnAstrix:
			String inputMsisdn = ((Button) v).getText().toString();
			if (phoneNumber == null && phoneNumber == " ") {
				phoneNumber = "";
				phoneNumber = inputMsisdn;
				txtPhoneNo.setText(phoneNumber);
			} else {
				phoneNumber += inputMsisdn;
				txtPhoneNo.setText(phoneNumber);
			}

			break;

		case R.id.btnClear:
			if (phoneNumber.length() > 0) {
				phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
				txtPhoneNo.setText(phoneNumber);
			}
			break;

		case R.id.btnCall:
			txtPhoneNo.setText("");
			if (phoneNumber != null && !phoneNumber.equals("")) {
				new RegisterCallLogsAsync().execute();
			}
			break;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		txtPhoneNo.setText("");
	}

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {

				Utility.regInCallLogs(phoneNumber, 1);
				// MainActivity.initSipManager();
				// Initialize SipManager
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			txtPhoneNo.setText("");
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {
				// sip.initiateCall(phoneNumber);
				Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
				i.putExtra("CONTACT_NUMBER", phoneNumber);
				startActivity(i);
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "SIP Registration failed.", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
