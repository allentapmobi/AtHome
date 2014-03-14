package in.tapmobi.athome.session;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "LoginPref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// All Shared Preferences Keys
	private static final String IS_PHONE_NUMBER_VERIFIED = "IsVerified";

	// Email address
	public static final String KEY_EMAIL = "UserEmail";

	// Email address
	public static final String KEY_PHONE_NUMBER = "PhoneNumber";

	// User Profile
	public static final String KEY_NAME = "UserName";
	public static final String KEY_PHOTO = "UserPhoto";
	public static final String KEY_STATUS = "UserStatus";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String email) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		editor.commit();

		// return user
		return user;
	}


	
	public String getUserPhoneNumber() {

		String phone = pref.getString(KEY_PHONE_NUMBER, null);
		editor.commit();

		// return Phone Number
		return phone;
	}


	public void createPhoneNumber(String number) {
		// Storing login value as TRUE
		editor.putBoolean(IS_PHONE_NUMBER_VERIFIED, true);

		// Storing email in pref
		editor.putString(KEY_PHONE_NUMBER, number);

		// commit changes
		editor.commit();
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	// Get Login State
	public boolean isVerfied() {
		return pref.getBoolean(IS_PHONE_NUMBER_VERIFIED, false);
	}
}
