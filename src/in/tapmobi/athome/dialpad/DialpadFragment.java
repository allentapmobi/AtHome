package in.tapmobi.athome.dialpad;

import in.tapmobi.athome.R;
import in.tapmobi.athome.contacts.ContactsFragment;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.incall.InCallActivity;
import in.tapmobi.athome.models.CallLogs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DialpadFragment extends Fragment implements View.OnClickListener {
	Date date;
	TextView txtPhoneNo;
	Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnCall, btnClear, btnHash, btnAstrix;
	private String phoneNumber = "";
	String UserName = null;
	ArrayList<CallLogs> sLogs = new ArrayList<CallLogs>();
	int count = 0;
	public DataBaseHandler db;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dialpad, container, false);
		txtPhoneNo = (TextView) rootView.findViewById(R.id.txtPhoneNo);
		txtPhoneNo.setText("");
		db = new DataBaseHandler(getActivity().getApplicationContext());

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
			if (phoneNumber != null) {
				new RegisterCallLogsAsync().execute();
			}
			break;
		}

	}

	private void regInCallLogs(String Msisdn) {

		Bitmap contactImg = null;
		Uri contactImgUri = null;
		CallLogs callLogs = new CallLogs();
		String currentTime = getCurrentTime();

		for (int i = 0; i < ContactsFragment.mContactsList.size(); i++) {
			String ContactNumber = ContactsFragment.mContactsList.get(i).getNumber();
			if (ContactNumber.contains(Msisdn)) {
				UserName = ContactsFragment.mContactsList.get(i).getName();
				contactImg = ContactsFragment.mContactsList.get(i).getContactPhoto();
				contactImgUri = ContactsFragment.mContactsList.get(i).getContactPhotoUri();
			}
		}

		if (sLogs.size() > 0) {
			int pos = sLogs.size() - 1;
			String prevContactNumber = sLogs.get(pos).getContactNumber();

			count = 1;
			if (prevContactNumber.contains(Msisdn)) {
				count++;
				callLogs.setContactName(UserName);
				callLogs.setContactNumber(Msisdn);
				callLogs.setContactPhoto(contactImg);
				callLogs.setCount(count);
				callLogs.setContactPhotoUri(contactImgUri);
				callLogs.setCallDuration(currentTime);
				sLogs.add(pos, callLogs);
			} else {
				count = 0;
			}
		}
		callLogs.setContactName(UserName);
		callLogs.setContactNumber(Msisdn);
		callLogs.setContactPhoto(contactImg);
		callLogs.setContactPhotoUri(contactImgUri);
		callLogs.setCallDuration(currentTime);
		sLogs.add(callLogs);

		// logs.get(count).setContactName(contactName);

		Log.d("Insert: ", "Inserting ..");
		// ADDING ALL THE VALUES FROM THE ARRAY TO DB
		db.addCallLogs(new CallLogs(UserName, Msisdn, currentTime));
	}

	@SuppressLint("SimpleDateFormat")
	private String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		// SimpleDateFormat sdf = new
		// SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
		String strTime = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			strTime = sdf.format(c.getTime());

			SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
			String strDate = sdf1.format(c.getTime());
			date = sdf.parse(strDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("DATE EXCEPTION", "PARSING ISSUE IN  DATE FORMAT");
		}
		return strTime;
	}

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			regInCallLogs(phoneNumber);
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
			i.putExtra("CONTACT_NAME", UserName);
			i.putExtra("CONTACT_NUMBER", phoneNumber);
			startActivity(i);
		}
	}

}
