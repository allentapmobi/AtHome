package in.tapmobi.athome.session;

import java.util.HashMap;

import android.annotation.SuppressLint;
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
	public static final String SIP_PASSWORD = "SipPassword";
	public static final String SIP_DOMAIN = "SipDomian";
	public static final String SIP_USERNAME = "SipUsername";

	// Constructor
	@SuppressLint("CommitPrefEdits")
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
	 * Create Sip sessions
	 */
	public void createSipProfile(String Name, String domain, String password) {
		// Storing username
		editor.putString(SIP_USERNAME, Name);
		// Storing domain
		editor.putString(SIP_DOMAIN, domain);
		// Storing password
		editor.putString(SIP_PASSWORD, password);
	}
	/**
	 * Get stored sip session user
	 */
	public String getSipUserName(){
		String username = pref.getString(SIP_USERNAME, null);
		editor.commit();
		
		return username;
	}
	
	public String getSipPassword(){
		String password = pref.getString(SIP_PASSWORD, null);
		editor.commit();
		
		return password;
	}
	
	public String getSipDomain(){
		String domain = pref.getString(SIP_DOMAIN, null);
		editor.commit();
		
		return domain;
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
