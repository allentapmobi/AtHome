package in.tapmobi.athome.sip;

import in.tapmobi.athome.ProfileFragment;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.subscription.SubscriptionActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;

public class SipRegisteration {

	public Context mContext;
	public static String sipAddress = null;

	public static SipManager mSipManager = null;
	public static SipProfile mProfile = null;
	public static SipAudioCall mCall = null;
	public static String sUpdateStatus = null;
	public static boolean isRegisteredWithSip;

	private SessionManager session;

	// private static final int CALL_ADDRESS = 1;
	// private static final int SET_AUTH_INFO = 2;
	// private static final int UPDATE_SETTINGS_DIALOG = 3;
	// private static final int HANG_UP = 4;

	public SipRegisteration(Context ctx) {
		this.mContext = ctx;
		session = new SessionManager(mContext);

	}

	public void initializeManager() throws java.text.ParseException {
		if (mSipManager == null) {
			mSipManager = SipManager.newInstance(mContext);

		}
		if (ProfileFragment.isActivated) {
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
				mSipManager.open(mProfile, pi, null);

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
			}
		} catch (Exception ee) {
			Log.d("onDestroy", "Failed to close local profile.", ee);
		}
	}

	/**
	 * Make an outgoing call.
	 */
	public void initiateCall(String msisdn) {

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
					call.startAudio();
					Log.i("InitiateCall", "Call Established");

					// call.setSpeakerMode(true);
					// call.toggleMute();
					// updateStatus(mCall);
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					sUpdateStatus = "Ready";
					Log.i("InitiateCall", "Call Ended");
				}
			};

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
