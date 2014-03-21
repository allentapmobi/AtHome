package in.tapmobi.athome.SIP;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.util.Log;

public class SipRegisteration {

	Context mContext;
	public String sipAddress = null;

	public SipManager mSipManager = null;
	public SipProfile mProfile = null;
	public SipAudioCall mCall = null;
	// public IncomingCallReceiver callReceiver;

	private static final int CALL_ADDRESS = 1;
	private static final int SET_AUTH_INFO = 2;
	private static final int UPDATE_SETTINGS_DIALOG = 3;
	private static final int HANG_UP = 4;

	public SipRegisteration(Context ctx) {
		this.mContext = ctx;
	}

	public void initializeManager() {
		if (mSipManager == null) {
			mSipManager = SipManager.newInstance(mContext);
		}
		initializeLocalProfile();
	}

	/**
	 * Logs you into your SIP provider, registering this device as the location
	 * to send SIP calls to for your SIP address.
	 */
	public void initializeLocalProfile() {
		if (mSipManager == null) {
			return;

		}

		if (mProfile != null) {
			closeLocalProfile();
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
			Log.d("WalkieTalkieActivity/onDestroy", "Failed to close local profile.", ee);
		}
	}

	/**
	 * Make an outgoing call.
	 */
	public void initiateCall() {

		updateStatus(sipAddress);

		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				// Much of the client's interaction with the SIP Stack will
				// happen via listeners. Even making an outgoing call, don't
				// forget to set up a listener to set things up once the call is
				// established.
				@Override
				public void onCallEstablished(SipAudioCall call) {
					call.startAudio();
					call.setSpeakerMode(true);
					call.toggleMute();
					updateStatus(mCall);
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					updateStatus("Ready.");
				}
			};

			mCall = mSipManager.makeAudioCall(mProfile.getUriString(), sipAddress, listener, 30);

		} catch (Exception e) {
			Log.i("WalkieTalkieActivity/InitiateCall", "Error when trying to close manager.", e);
			if (mProfile != null) {
				try {
					mSipManager.close(mProfile.getUriString());
				} catch (Exception ee) {
					Log.i("WalkieTalkieActivity/InitiateCall", "Error when trying to close manager.", ee);
					ee.printStackTrace();
				}
			}
			if (mCall != null) {
				mCall.close();
			}
		}
	}

	/**
	 * Updates the status box at the top of the UI with a messege of your
	 * choice.
	 * 
	 * @param status
	 *            The String to display in the status box.
	 */
	public void updateStatus(final String status) {
		// Be a good citizen. Make sure UI changes fire on the UI thread.
		// this.runOnUiThread(new Runnable() {
		// public void run() {
		// TextView labelView = (TextView) findViewById(R.id.sipLabel);
		// labelView.setText(status);
		// }
		// });
	}

	/**
	 * Updates the status box with the SIP address of the current call.
	 * 
	 * @param call
	 *            The current, active call.
	 */
	public void updateStatus(SipAudioCall call) {
		String useName = call.getPeerProfile().getDisplayName();
		if (useName == null) {
			useName = call.getPeerProfile().getUserName();
		}
		updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());
	}

	public void updatePreferences() {
		Intent settingsActivity = new Intent(mContext, SipSettings.class);
		mContext.startActivity(settingsActivity);
	}

}
