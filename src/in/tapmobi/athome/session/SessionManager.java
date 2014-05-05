package in.tapmobi.athome.session;

import in.tapmobi.athome.models.UserProfile;
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

	private static final String IS_SIP_USER_REGISTERED = "IsRegistered";

	public static final String KEY_PHOTO = "UserPhoto";
	public static final String KEY_STATUS = "UserStatus";
	// User Profile
	public static final String KEY_NAME = "UserName";
	public static final String KEY_PHONE_NUMBER = "PhoneNumber";
	public static final String KEY_EMAIL = "UserEmail";
	public static final String KEY_SUBSCRIPTION_DATE = "SubscriptioDate";
	public static final String KEY_VALIDITY_DATE = "ValidityDate";
	public static final String SIP_MSISDN = "subMsisdn";
	public static final String SIP_PORT = "SipPort";
	public static final String SIP_PASSWORD = "SipPassword";
	public static final String SIP_DOMAIN = "SipDomian";
	public static final String SIP_USERNAME = "SipUsername";
	public static final String SIP_USERPROFIL_IMG = "SipProfilePicture";
	public static final String IS_TOGGLE_ACTIVE = "IsAppActive";

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
		// commit changes
		editor.commit();
	}

	/**
	 * Get stored sip session user
	 */
	// public String getSipUserName() {
	// String username = pref.getString(SIP_USERNAME, null);
	// editor.commit();
	//
	// return username;
	// }
	//
	// /**
	// * Get stored session data
	// * */
	// public HashMap<String, String> getUserDetails() {
	// HashMap<String, String> user = new HashMap<String, String>();
	//
	// // user email id
	// user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
	//
	// editor.commit();
	//
	// // return user
	// return user;
	// }
	//
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
	public boolean isMsisdnVerfied() {

		return pref.getBoolean(IS_PHONE_NUMBER_VERIFIED, false);
	}

	// ----------------------------------------------------------------------

	/**
	 * Create Sip sessions
	 */
	public void createSipUserProfile(UserProfile usrProfile) {

		editor.putBoolean(IS_SIP_USER_REGISTERED, true);

		editor.putString(SIP_MSISDN, usrProfile.Msisdn);

		editor.putString(KEY_NAME, usrProfile.Name);

		editor.putString(KEY_EMAIL, usrProfile.Email);

		editor.putString(KEY_SUBSCRIPTION_DATE, usrProfile.SubscribedDate);

		editor.putString(KEY_VALIDITY_DATE, usrProfile.ValidityDate);

		editor.putString(SIP_PORT, usrProfile.SipPort);
		// Storing username
		editor.putString(SIP_USERNAME, usrProfile.Msisdn);
		// Storing domain
		editor.putString(SIP_DOMAIN, usrProfile.SipServer);
		// Storing password
		editor.putString(SIP_PASSWORD, usrProfile.SipPassword);

		editor.putString(SIP_USERPROFIL_IMG, usrProfile.Base64ProfilePhoto);

		// commit changes
		editor.commit();
	}

	// Get Sip registeration state
	public boolean isUserRegisteredinSip() {

		return pref.getBoolean(IS_SIP_USER_REGISTERED, false);
	}

	/**
	 * Get stored sip session user
	 */
	public String getSipUserName() {

		String username = pref.getString(SIP_USERNAME, null);
		editor.commit();

		return username;
	}

	public String getSipPassword() {

		String password = pref.getString(SIP_PASSWORD, null);
		editor.commit();

		return password;
	}

	public String getSipDomain() {

		String domain = pref.getString(SIP_DOMAIN, null);
		editor.commit();

		return domain;
	}

	public String getSipPort() {

		String port = pref.getString(SIP_PORT, null);
		editor.commit();

		return port;
	}

	public String getName() {

		String name = pref.getString(KEY_NAME, null);
		editor.commit();

		return name;
	}

	public String getEmail() {

		String email = pref.getString(KEY_EMAIL, null);
		editor.commit();

		return email;
	}

	public String getSubscribedMsisdn() {

		// Subscribed MSISDN = subMsisdn
		String subMsisdn = pref.getString(SIP_MSISDN, null);
		editor.commit();

		return subMsisdn;
	}

	public String getValidityDate() {

		String validUpto = pref.getString(KEY_VALIDITY_DATE, null);
		editor.commit();

		return validUpto;
	}

	public String getProfileImage() {

		String profileImage = pref.getString(SIP_USERPROFIL_IMG, null);

		editor.commit();

		return profileImage;
	}

	public void createToggleState(Boolean state) {
		editor.putBoolean(IS_TOGGLE_ACTIVE, state);

		editor.commit();

	}

	public boolean getToggleState() {
		return pref.getBoolean(IS_TOGGLE_ACTIVE, false);
	}

	public void createProfileImage(String base64Image) {
		editor.putString(SIP_USERPROFIL_IMG, base64Image);

		editor.commit();
	}
}
