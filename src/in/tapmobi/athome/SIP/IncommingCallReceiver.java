package in.tapmobi.athome.SIP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipProfile;

public class IncommingCallReceiver extends BroadcastReceiver {
	SipAudioCall.Listener mCallListener;

	@Override
	public void onReceive(Context context, Intent intent) {

		SipAudioCall mIncomingCall = null;
		try {
			mCallListener = new SipAudioCall.Listener() {
				public void onRinging(SipAudioCall call, SipProfile caller) {
					try {
						call.answerCall(30);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		} catch (Exception e) {
			e.printStackTrace();

		}
		SipRegisteration mSipReg = new SipRegisteration(context);
		try {
			mIncomingCall = mSipReg.mSipManager.takeAudioCall(intent, mCallListener);

			mIncomingCall.answerCall(30);
			mIncomingCall.startAudio();
			mIncomingCall.setSpeakerMode(true);
			// if (mIncomingCall.isMuted()) {
			// mIncomingCall.toggleMute();
			// }
		} catch (SipException e) {
			e.printStackTrace();
		}
	}

}
