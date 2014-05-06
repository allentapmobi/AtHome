package in.tapmobi.athome.registration;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import in.tapmobi.athome.util.Utility;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Register {

	private String virtualNumber = "+919793015544";
	String message;

	SessionManager session;

	public Context mContext;
	Boolean isVerified = false;
	public static Boolean sIsVerifying = false;
	// private int times = 2;
	private String mServerResponse;

	Runnable updateUserVerifyRunnable, updateRunnable;
	Handler myHandler = new Handler();

	public Register(Context ctx) {
		this.mContext = ctx;
		session = new SessionManager(mContext);
	}

	public void RegisterMsisdn(String phoneNumber) {

		// check if number exists in defaults phone status
		try {

			TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
			phoneNumber = tm.getLine1Number();

			Utility.hideSoftKeyboard((Activity) mContext);

			if (phoneNumber.length() > 12)
				phoneNumber = phoneNumber.substring(phoneNumber.length() - 12);
			int phoneNo;
			try {
				phoneNo = Integer.parseInt(phoneNumber);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				phoneNo = 100;
			}
			if (phoneNumber == null || phoneNumber.equals("") || phoneNo == 0) {
				// Do nothing
				// Toast.makeText(VerifyMSISDNActivity.this,
				// "Mobile Number is null", Toast.LENGTH_LONG).show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setInverseBackgroundForced(true);
				builder.setMessage("Please confirm that your mobile no is " + phoneNumber + ".").setCancelable(false)

				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// number verified store the phone
						// number
						// session.createPhoneNumber(phoneNumber.replace("+",
						// ""));
						// get operator for MSISDN
						updateUI();

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						isVerified = false;
						// Do nothing
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		} catch (Exception ex) {
			// Toast.makeText(VerifyMSISDNActivity.this, "Exception",
			// Toast.LENGTH_LONG).show();
		}

		// if (phoneNumber != null) {
		if (phoneNumber == null && phoneNumber == null || phoneNumber.length() < 9) {

			RegisterationActivity.progressLayout.setVisibility(View.VISIBLE);
			// ((TextView) findViewById(R.id.progressText)).setText("Verifying...");
			Toast.makeText(mContext, "Verifying...", Toast.LENGTH_SHORT).show();
			Utility.hideSoftKeyboard((Activity) mContext);
			// phoneNumber = RegisterationActivity.etMsisdn.getText().toString() + txtPhoneNumber.getText().toString();
			phoneNumber = RegisterationActivity.etMsisdn.getText().toString();

			Toast.makeText(mContext, "verifying mobile number: " + phoneNumber, Toast.LENGTH_LONG).show();

			if (phoneNumber != null && phoneNumber != "" && phoneNumber.length() != 0) {
				int min = 1000000;
				int max = 3000000;

				Random r = new Random();
				int i1 = r.nextInt(max - min + 1) + min;
				message = String.valueOf(i1);

				// send an SMS to virtual number

				// sendSMS(virtualNumber, message);
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.setData(Uri.parse("sms:" + virtualNumber));
				sendIntent.putExtra("sms_body", message);
				sendIntent.putExtra("exit_on_sent", true);
				mContext.startActivity(sendIntent);
				SendSms(phoneNumber);
				Log.d("VERIFY", "To: " + virtualNumber + " Message: " + message);
			}
		}
		/*
		 * }else { Toast.makeText(VerifyMSISDNActivity.this, "PhoneNumber is null", Toast.LENGTH_LONG).show(); }
		 */

	}

	private void updateUI() {
		SessionManager session = new SessionManager(mContext);
		if (session.isUserRegisteredinSip()) {
			Intent i = new Intent(mContext, MainActivity.class);
			mContext.startActivity(i);
		} else {
			Intent e = new Intent(mContext, SubscriptionActivity.class);
			mContext.startActivity(e);
		}

	}

	protected void SendSms(final String phoneNumber) {
		if (!sIsVerifying) {
			sIsVerifying = true;
			isVerified = false;
			if (Utility.isNetworkAvailable(mContext)) {
				updateUserVerifyRunnable = new Runnable() {
					public void run() {
						// Log.d("VERIFY", "Times: " + times);
						// call the activity method that updates the
						// UI
						// userVerfiyProgressDialog.dismiss();
						sIsVerifying = false;
						// check server for verification
						new Thread(new Runnable() {
							@Override
							public void run() {
								// mServerResponse = ServerAPI.verifyMSISDN(message);
								myHandler.post(updateRunnable);
							}

						}).start();
						updateRunnable = new Runnable() {
							@Override
							public void run() {

								Log.d("VERIFY", "Server Response: " + mServerResponse);

								boolean success;
								try {
									success = mServerResponse.contains(phoneNumber.replace("+", ""));
								} catch (Exception e) {
									success = false;
								}

								if (success) {
									isVerified = true;
								}

								if (!isVerified) {
									// if (times != 0) {
									// // recall the server api and
									// // set
									// // 'isverified'
									// myHandler.postDelayed(updateUserVerifyRunnable, 20000);
									// times -= 1;
									// return;
									// }
									new AlertDialog.Builder(mContext).setMessage("Could not verify your number. Please try again.").setTitle("").setCancelable(true)
											.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													RegisterationActivity.progressLayout.setVisibility(View.GONE);
												}
											}).show();

								} else {
									RegisterationActivity.progressLayout.setVisibility(View.GONE);
									// get operator for MSISDN
									// createRealm(phoneNumber);
									session.createPhoneNumber(phoneNumber);
									updateUI();
								}
							}

						};
					}
				};
			} else {
				RegisterationActivity.progressLayout.setVisibility(View.GONE);
				Toast.makeText(mContext, "Check connectivity", Toast.LENGTH_LONG).show();
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					myHandler.postDelayed(updateUserVerifyRunnable, 20000);
				}

			}).start();
		}
	}

}
