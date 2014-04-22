package in.tapmobi.athome.registration;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CountriesListAdapter;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import in.tapmobi.athome.util.Utility;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegisterationActivity extends Activity implements OnClickListener {

	static EditText etMsisdn;
	Button btnCountryCodes, btnContinue;
	public static String sCode = null;
	public static String sCountry = null;
	SessionManager session;
	Utility util;
	public static RelativeLayout progressLayout;
	public static ArrayList<ContactsModel> mContact = new ArrayList<ContactsModel>();

	Dialog dialog;
	Register mRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registger);
		mRegister = new Register(RegisterationActivity.this);
		util = new Utility(RegisterationActivity.this);

		initViews();
		session = new SessionManager(RegisterationActivity.this);

	}

	private void initViews() {

		progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
		progressLayout.setVisibility(View.GONE);
		dialog = new Dialog(getApplicationContext());
		etMsisdn = (EditText) findViewById(R.id.etAddPhone);
		btnCountryCodes = (Button) findViewById(R.id.btnCountryCode);
		btnCountryCodes.setOnClickListener(this);
		btnContinue = (Button) findViewById(R.id.btnRegister);
		btnContinue.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnCountryCode:
			inflateCustomDialogOfCountryCodes();

			break;

		case R.id.btnRegister:
			Utility.hideSoftKeyboard(RegisterationActivity.this);
			if (Utility.isNetworkAvailable(RegisterationActivity.this)) {
				mRegister.RegisterMsisdn(etMsisdn.getEditableText().toString());
			} else {
				Toast.makeText(RegisterationActivity.this, "No Network connectivity.Please try later", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private void inflateCustomDialogOfCountryCodes() {

		final String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterationActivity.this);

		LayoutInflater inflater = getLayoutInflater();
		alertDialog.setCancelable(true);

		View convertView = (View) inflater.inflate(R.layout.country_codes_list, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Country Codes");
		ListView lv = (ListView) convertView.findViewById(R.id.lv);
		lv.setAdapter(new CountriesListAdapter(this, recourseList));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				String CountryCode = null;
				CountryCode = recourseList[pos];
				sCode = CountryCode.substring(0, CountryCode.indexOf(","));
				sCountry = CountryCode.substring(CountryCode.indexOf(",") + 1);
				btnCountryCodes.setText("(+ " + sCode + ") " + sCountry);
				dialog.cancel();
			}

		});

		dialog = alertDialog.show();

	}

	@Override
	protected void onResume() {
		// Check if number is verified and stored
		super.onResume();
		if (session.isMsisdnVerfied()) {
			if (session.isUserRegisteredinSip()) {
				progressLayout.setVisibility(View.VISIBLE);
				new getContactsAsync().execute();
			} else {
				Intent i = new Intent(RegisterationActivity.this, SubscriptionActivity.class);
				startActivity(i);
				RegisterationActivity.this.finish();
			}
		}
	}

	private class getContactsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			mContact.addAll(Utility.getContactsList());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			Intent i = new Intent(RegisterationActivity.this, MainActivity.class);
			startActivity(i);
			progressLayout.setVisibility(View.GONE);
			RegisterationActivity.this.finish();
			super.onPostExecute(result);
		}

	}
}
