package in.tapmobi.athome.sip;

import in.tapmobi.athome.InCallActivity;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SipRegisteration {

	public Context mContext;
	public Activity mActivity;
	public static String sipAddress = null;

	public static SipManager mSipManager = null;
	public static SipProfile mProfile = null;
	public static SipAudioCall mCall = null;
	public static String sUpdateStatus = null;
	public static boolean isRegisteredWithSip;

	final Handler handler = new Handler();

	private SessionManager session;

	// private static final int CALL_ADDRESS = 1;
	// private static final int SET_AUTH_INFO = 2;
	// private static final int UPDATE_SETTINGS_DIALOG = 3;
	// private static final int HANG_UP = 4;

	public SipRegisteration(Context ctx, Activity activity) {
		this.mContext = ctx;
		this.mActivity = activity;
		session = new SessionManager(mContext);

	}

	public void initializeManager() throws java.text.ParseException {
		if (mSipManager == null) {
			mSipManager = SipManager.newInstance(mContext);

		}
		// Checks from the preferences wether the sip profile has been activated or deactivated.
		if (session.getToggleState()) {
			initializeLocalProfile();
		}
	}

	/**
	 * Logs you into your SIP provider, registering this device as the location to send SIP calls to for your SIP address.
	 * 
	 * @throws java.text.ParseException
	 */
	public void initializeLocalProfile() throws java.text.ParseException {
		if (mSipManager == null) {
			return;
		}

		if (mProfile != null) {
			closeLocalProfile();
		}

		String password = session.getSipPassword();
		String username = session.getSipUserName();
		String domain = session.getSipDomain();

		if (username == null || domain == null || password == null) {

			Intent intent = new Intent(mContext, SubscriptionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		} else {

			try {

				SipProfile.Builder builder = new SipProfile.Builder(username, domain);
				builder.setPassword(password);
				builder.setProtocol("TCP");
				builder.setOutboundProxy("home.tapmobi.in");
				mProfile = builder.build();

				Intent i = new Intent();
				i.setAction("android.AtHome.INCOMING_CALL");
				PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, Intent.FILL_IN_DATA);
				try {
					mSipManager.open(mProfile, pi, null);
				} catch (Exception e) {
					System.out.println("SIP MANAGER ERROR" + e);
				}

				// This listener must be added AFTER manager.open is
				// called,Otherwise the methods arent guaranteed to fire.

				mSipManager.setRegistrationListener(mProfile.getUriString(), new SipRegistrationListener() {

					@Override
					public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {

						sUpdateStatus = "Disonnected";
						Log.v("UpdateStatus---->", "Registration failed.  Please check your settings.");
						isRegisteredWithSip = false;
						// Toast.makeText(mContext, sUpdateStatus, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onRegistrationDone(String localProfileUri, long expiryTime) {

						sUpdateStatus = "Connected";
						Log.v("UpdateStatus---->", "Ready - Registered with SIP server");
						isRegisteredWithSip = true;
						// Toast.makeText(mContext, sUpdateStatus, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onRegistering(String localProfileUri) {

						sUpdateStatus = "Disconnected";
						Log.v("UpdateStatus---->", "Registering with SIP Server...");
						// Toast.makeText(mContext, sUpdateStatus, Toast.LENGTH_SHORT).show();
					}
				});

			} catch (ParseException pe) {
				pe.printStackTrace();
			} catch (SipException se) {
				se.printStackTrace();
			}
		}
	}

	public void closeLocalProfile() {
		if (mSipManager == null) {
			return;
		}

		try {
			if (mProfile != null) {
				mSipManager.close(mProfile.getUriString());
				System.out.println("Closed local profile");
			}
		} catch (Exception ee) {
			Log.d("onDestroy", "Failed to close local profile.", ee);
		}
	}

	/**
	 * Make an outgoing call.with a post delayed of 4 seconds. You can not call initiateCall() right after initializeManager(). You have to wait about
	 * 4 seconds when the SIP components have initialized.
	 */
	public void initiateCall(final String msisdn) {
		Log.v("INITIATE CALL", "initiate call timer starts");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.v("Timed CALL", "initiate call timer ends");
				makeOutGoingCall(msisdn);
			}
		}, 4000);

	}

	private void makeOutGoingCall(String msisdn) {

		sipAddress = msisdn + "@home.tapmobi.in";
		// ProfileFragment.updateStatus(sipAddress);
		Log.e("Initiating Call", sipAddress + "-----------------------");

		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				// Much of the client's interaction with the SIP Stack will
				// happen via listeners. Even making an outgoing call, don't
				// forget to set up a listener to set things up once the call is
				// established.
				@Override
				public void onCallEstablished(SipAudioCall call) {

					// Getting the beeper to play and stop before call getting established
					// InCallActivity.beeper.stop();
					// InCallActivity.beeper.release();
					InCallActivity.txtCallStatus.setText("In Call...");

					call.startAudio();
					Log.v("InitiateCall", "Call Established");

					// call.toggleMute();
					// updateStatus(mCall);
					// Start the timer in IncallActivity
					InCallActivity.StartTimer();
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					sUpdateStatus = "Ready";
					Log.v("InitiateCall", "Call Ended" + call.getPeerProfile().getDisplayName());
					// ((Activity) mContext).finish();
					mActivity.finish();
					InCallActivity.txtCallStatus.setText("Call ended...");
				}

				@Override
				public void onCalling(SipAudioCall call) {
					// TODO Auto-generated method stub
					super.onCalling(call);
					Log.v("on Calling Method Listener", "Calling tring tring");

				}

				@Override
				public void onError(SipAudioCall call, int errorCode, String errorMessage) {
					// TODO Auto-generated method stub
					super.onError(call, errorCode, errorMessage);
					Log.v("onCall Error", errorMessage);
					Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
					// ((InCallActivity) mContext).finish();
					mActivity.finish();

				}

				@Override
				public void onCallBusy(SipAudioCall call) {
					// TODO Auto-generated method stub
					super.onCallBusy(call);
					Log.v("onCallBusy", "Call Busy");
				}

				@Override
				public void onCallHeld(SipAudioCall call) {
					// TODO Auto-generated method stub
					super.onCallHeld(call);
					Log.v("onCallHeld", "Call is on Hold");
				}

				@Override
				public void onChanged(SipAudioCall call) {
					// TODO Auto-generated method stub
					super.onChanged(call);
					Log.v("onChanged", "Call changed");
				}
			};
			Log.v("Profile---->", mProfile.getUriString());
			Log.v("Sip Address---->", sipAddress);
			Log.v("Listener---->", listener.toString());

			mCall = mSipManager.makeAudioCall(mProfile.getUriString(), sipAddress, listener, 30);

		} catch (Exception e) {
			Log.i("InitiateCall", "Error when trying to close manager.", e);
			if (mProfile != null) {
				try {
					mSipManager.close(mProfile.getUriString());
				} catch (Exception ee) {
					Log.i("InitiateCall", "Error when trying to close manager.", ee);
					ee.printStackTrace();
				}
			}
			if (mCall != null) {
				mCall.close();
			}
		}

	}

	public void endCall() {
		if (mCall != null) {
			try {
				mCall.endCall();
			} catch (SipException se) {
				Log.d("onOptionsItemSelected", "Error ending call.", se);
			}
			mCall.close();
		}
	}

}
