package in.tapmobi.athome.subscription;

import in.tapmobi.athome.MainActivity;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.session.SessionManager;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Subscribe {

	private String virtualNumber = "+919793015544";
	String message;
	public String MSISDN = null;

	SessionManager session;

	public Context mContext;
	Boolean isVerified = false;
	public static Boolean sIsVerifying = false;
	// private int times = 2;
	private String mServerResponse;

	Runnable updateUserVerifyRunnable, updateRunnable;
	Handler myHandler = new Handler();

	public Subscribe(Context ctx) {
		this.mContext = ctx;
		session = new SessionManager(mContext);
	}

	public void RegisterMsisdn(final String msisdn) {

		MSISDN = msisdn;
		if (msisdn.length() > 2) {
			isVerified = false;
			SubscriptionActivity.progressLayout.setVisibility(View.VISIBLE);
			SubscriptionActivity.progressLayout.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});

			SubscriptionActivity.progressLayout.setVisibility(View.GONE);

			int phoneNo;
			try {
				phoneNo = Integer.parseInt(msisdn);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				phoneNo = 100;
			}

			if (msisdn == null || msisdn.equals("") || phoneNo == 0) {
				// Do nothing
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setInverseBackgroundForced(true);
				builder.setMessage("Please confirm that your mobile no is " + msisdn + ".").setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// number verified store the
								// phone
								// number
								createMsg(msisdn);
								dialog.cancel();

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
			// TODO:Implement the progress dialog
			// progressLayout.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(mContext, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateUI() {

		Intent i = new Intent(mContext, MainActivity.class);
		mContext.startActivity(i);

	}

	private void createMsg(String msisdn) {
		SubscriptionActivity.progressLayout.setVisibility(View.VISIBLE);
		// phoneNumber = etPhoneNo.getText().toString();

		if (msisdn != null && msisdn != "" && msisdn.length() != 0) {
			int min = 1000;
			int max = 3000;

			Random r = new Random();
			int i1 = r.nextInt(max - min + 1) + min;
			message = String.valueOf(i1);

			// send an SMS to virtual number
			sendSMS(virtualNumber, message);
			Log.d("VERIFY", "To: " + virtualNumber + "   Message: " + message);

		}

	}

	private void sendSMS(final String toPhoneNumber, final String message) {

		String SENT = "SMS_SENT";

		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);

		// ---when the SMS has been sent---
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(mContext, "SMS sent", Toast.LENGTH_SHORT).show();
					if (!sIsVerifying) {
						sIsVerifying = true;
						isVerified = false;

						updateUserVerifyRunnable = new Runnable() {
							public void run() {
								// Log.d("VERIFY", "Times: " + times);
								// call the activity method that updates the UI
								// userVerfiyProgressDialog.dismiss();
								sIsVerifying = false;
								// check server for verification
								new Thread(new Runnable() {
									@Override
									public void run() {
										mServerResponse = ServerAPI.verifyMSISDN(message);
										myHandler.post(updateRunnable);
									}

								}).start();
								updateRunnable = new Runnable() {
									@Override
									public void run() {

										Log.d("VERIFY", "Server Response: " + mServerResponse);
										// Check for server response and
										// boolean success =

										// --------------------------------------------------------------------------------\\
										// phoneNumber.contains(serverResponse);
										boolean success;
										try {
											success = mServerResponse.contains(MSISDN.replace("+", ""));
											System.out.println("Phone Number:" + MSISDN);
										} catch (Exception e) {
											success = false;
										}
										if (success) {
											isVerified = true;

										}

										if (!isVerified) {

											// if (times != 0) {
											// // recall the server api and set
											// // 'isverified'
											// //
											// myHandler.postDelayed(updateUserVerifyRunnable,
											// 20000);
											// // times -= 1;
											// return;
											// }
											new AlertDialog.Builder(mContext).setMessage("Could not verify your number. Please try again.")
													.setTitle("").setCancelable(true)
													.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int whichButton) {
															SubscriptionActivity.progressLayout.setVisibility(View.GONE);
														}
													}).show();

										} else {
											SubscriptionActivity.progressLayout.setVisibility(View.GONE);
											// createRealm(phoneNumber);
											session.createPhoneNumber(MSISDN);
											updateUI();
										}
									}
								};
							}
						};
						new Thread(new Runnable() {
							@Override
							public void run() {
								myHandler.postDelayed(updateUserVerifyRunnable, 20000);
							}

						}).start();
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(mContext, "Verification failed!", Toast.LENGTH_SHORT).show();
					SubscriptionActivity.progressLayout.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(mContext, "No service", Toast.LENGTH_SHORT).show();
					SubscriptionActivity.progressLayout.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(mContext, "Verification failed!", Toast.LENGTH_SHORT).show();
					SubscriptionActivity.progressLayout.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(mContext, "Verification failed!", Toast.LENGTH_SHORT).show();
					SubscriptionActivity.progressLayout.setVisibility(View.GONE);
					break;
				}
			}
		}, new IntentFilter(SENT));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(toPhoneNumber, null, message, sentPI, null);

	}


}
