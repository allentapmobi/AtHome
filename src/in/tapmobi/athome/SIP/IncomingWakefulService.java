package in.tapmobi.athome.sip;

import in.tapmobi.athome.incomming.IncomingCallActivity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.util.Log;

public class IncomingWakefulService extends IntentService {

	static SipAudioCall incomingCall = null;
	String userName = null;

	public IncomingWakefulService() {
		super("IncomingWakefulService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		SipAudioCall incomingCall = null;
		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				@Override
				public void onRinging(SipAudioCall call, SipProfile caller) {
					super.onRinging(call, caller);
				}
			};
			incomingCall = SipRegisteration.mSipManager.takeAudioCall(intent, listener);

			userName = incomingCall.getPeerProfile().getDisplayName();
			if (userName == null) {
				userName = incomingCall.getPeerProfile().getUserName();
			}

			showIncomingCall(intent, IncomingCallReceiver.c);
			SipRegisteration.mCall = incomingCall;
			System.out.println(incomingCall);

			// sip..updateStatus(incomingCall);

		} catch (Exception e) {

			if (incomingCall != null) {
				incomingCall.close();
			}
			Log.e("IncomingWakeful service", "" + e);
		}

		// Release the wake lock provided by the WakefulBroadcastReceiver.
		IncomingCallReceiver.completeWakefulIntent(intent);
	}

	/**
	 * Handling incoming call and passing it to the IncomingCalActivity
	 * 
	 * @param intent
	 * @param context
	 */

	private void showIncomingCall(Intent intent, Context context) {

		Intent intentCall = new Intent(context, IncomingCallActivity.class);
		intentCall.putExtra("INCALL_USER_NAME", userName);
		context.startActivity(intentCall);

	}

	public static void answerIncomingCall() {

		try {
			incomingCall.answerCall(30);
			incomingCall.startAudio();
			if (incomingCall.isMuted()) {
				incomingCall.toggleMute();

			}

		}

		catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public static void rejectIncomingCall() {

		try {
			if (incomingCall != null) {

				incomingCall.endCall();
				incomingCall.close();

			}

		} catch (Exception e) {

			System.out.println(e.toString());
		}
	}

}
