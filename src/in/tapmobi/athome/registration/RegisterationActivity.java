package in.tapmobi.athome.registration;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.CountriesListAdapter;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import in.tapmobi.athome.util.Utility;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
	String message, verificationCode;

	Runnable updateUserVerifyRunnable, updateRunnable, updateRunnableProfile, runnableReg, runnableVerify;
	Handler myHandler = new Handler();
	private String mServerResponse;
	boolean success;
	boolean isNumberReg = false;
	String registeringMsisdn;

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

	// Initialize all view used in the layout
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

	// Listening to click events from the view
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnCountryCode:
			inflateCustomDialogOfCountryCodes();

			break;

		case R.id.btnRegister:

			Utility.hideSoftKeyboard(RegisterationActivity.this);

			if (Utility.isNetworkAvailable(RegisterationActivity.this)) {
				registeringMsisdn = etMsisdn.getEditableText().toString();
				// getProfileForMsisdn(mEtMsisdn);
				registerNumber(registeringMsisdn);
			} else {
				Toast.makeText(RegisterationActivity.this, "No Network connectivity.Please try later", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private void registerNumber(final String phoneNumber) {

		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterationActivity.this);
		builder.setInverseBackgroundForced(true);
		builder.setMessage("Please confirm that your mobile no is " + phoneNumber + ".").setCancelable(false)

		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				// get operator for MSISDN
				// updateUI();
				registerNumberBackground(phoneNumber);
				progressLayout.setVisibility(View.VISIBLE);
			}

		}).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Do nothing
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	private void registerNumberBackground(final String phoneNumber) {

		updateUserVerifyRunnable = new Runnable() {

			@Override
			public void run() {

				int min = 1000000;
				int max = 3000000;

				Random r = new Random();
				int i1 = r.nextInt(max - min + 1) + min;
				message = "Please enter" + String.valueOf(i1) + "into @home app.";
				verificationCode = String.valueOf(i1);
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {

						try {

							success = ServerAPI.sendSms(phoneNumber, message);
							myHandler.post(updateRunnable);
							// if (success) {
							// updateActivity();
							// }
						} catch (Exception e) {

							e.printStackTrace();

						}
					}

				});

				thread.start();
				updateRunnable = new Runnable() {

					@Override
					public void run() {

						try {

							if (success) {
								// session.createPhoneNumber(phoneNumber);
								session.createVerificationCode(verificationCode);
								updateActivity();

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};

			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {

				myHandler.post(updateUserVerifyRunnable);

			}

		}).start();

	}

	private void updateActivity() {

		Intent i = new Intent(RegisterationActivity.this, VerificationActivity.class);
		i.putExtra("MSISDN", registeringMsisdn);

		startActivity(i);
		progressLayout.setVisibility(View.GONE);

	}

	// Inflate a custom dialogs with country code and flags
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

	// Here we check if the number is verified and the sip user profile has been registered
	@Override
	protected void onResume() {
		// Check if number is verified and stored
		super.onResume();

		if (session.isMsisdnVerfied()) {
			if (session.isUserRegisteredinSip()) {
				new getContactsAsync().execute();
				Intent i = new Intent(RegisterationActivity.this, MainActivity.class);
				startActivity(i);
				RegisterationActivity.this.finish();
			} else {
				Intent i = new Intent(RegisterationActivity.this, SubscriptionActivity.class);
				startActivity(i);
				RegisterationActivity.this.finish();
			}
		}

	}

	// TODO: Need to shift this some where else inorder to release to many activities starting from this activity
	private class getContactsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			mContact.addAll(Utility.getContactsList());
			Utility.sMsgs.addAll(Utility.getAllMsgs());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// Intent i = new Intent(RegisterationActivity.this, MainActivity.class);
			// startActivity(i);
			progressLayout.setVisibility(View.GONE);
			// RegisterationActivity.this.finish();
			super.onPostExecute(result);
		}

	}

}
