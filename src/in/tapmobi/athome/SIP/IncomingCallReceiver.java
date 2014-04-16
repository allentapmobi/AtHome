/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.tapmobi.athome.sip;

import in.tapmobi.athome.incomming.IncomingCallActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/**
 * Listens for incoming SIP calls, intercepts and hands them off to WalkieTalkieActivity.
 */
public class IncomingCallReceiver extends WakefulBroadcastReceiver {
	/**
	 * Processes the incoming call, answers it, and hands it over to the WalkieTalkieActivity.
	 * 
	 * @param context
	 *            The context under which the receiver is running.
	 * @param intent
	 *            The intent being received.
	 */
	static Uri notification;
	static Ringtone r;
	static SipAudioCall incomingCall = null;
	String userName = null;

	// static WakeLock screenOn;

	@Override
	public void onReceive(final Context context, Intent intent) {
		SipAudioCall incomingCall = null;
		try {

			// screenOn = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
			// PowerManager.ACQUIRE_CAUSES_WAKEUP,
			// "example");
			// screenOn.acquire();

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

			showIncomingCall(intent, context);
			SipRegisteration.mCall = incomingCall;
			System.out.println(incomingCall);

			// sip..updateStatus(incomingCall);

		} catch (Exception e) {
			if (incomingCall != null) {
				incomingCall.close();
			}
		}
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
			// screenOn.release();
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

				// screenOn.release();
			}

		} catch (Exception e) {

			System.out.println(e.toString());
		}
	}
}
