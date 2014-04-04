package in.tapmobi.athome.util;

import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.CallLog;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.models.GroupedLogs;
import in.tapmobi.athome.registration.RegisterationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Utility {
	public static Context mContext;
	public static Date date;
	public static int count = 0;
	public static DataBaseHandler db;
	public static ArrayList<CallLog> sLogs = new ArrayList<CallLog>();
	public static ArrayList<ContactsModel> sContacts = new ArrayList<ContactsModel>();
	public static ArrayList<CallLog> CallLogs = new ArrayList<CallLog>();
	static ArrayList<GroupedLogs> groupedCallLogs = new ArrayList<GroupedLogs>();
	public static Cursor cur;
	public static String prevMsisdn;

	public Utility(Context ctx) {
		mContext = ctx;

	}

	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	static String UserName = null;

	public static void regInCallLogs(String Msisdn) {

		// Bitmap contactImg = null;
		// Uri contactImgUri = null;
		// CallLogs callLogs = new CallLogs();
		String currentTime = getCurrentTime();
		db = new DataBaseHandler(mContext);

		for (int i = 0; i < sContacts.size(); i++) {
			String ContactNumber = sContacts.get(i).getNumber();
			if (ContactNumber.contains(Msisdn)) {
				UserName = sContacts.get(i).getName();
				// contactImg =
				// ContactsFragment.sContacts.get(i).getContactPhoto();
				// contactImgUri =
				// ContactsFragment.sContacts.get(i).getContactPhotoUri();
				break;
			}
		}

		Log.d("Insert: ", "Inserting ..");
		// ADDING ALL THE VALUES FROM THE ARRAY TO DB
		db.addCallLogs(new CallLog(UserName, Msisdn, currentTime));
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		String strTime = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			strTime = sdf.format(c.getTime());

			SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
			String strDate = sdf1.format(c.getTime());
			date = sdf.parse(strDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("DATE EXCEPTION", "PARSING ISSUE IN  DATE FORMAT");
		}
		return strTime;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getCurrentDateTime() {
		Calendar c = Calendar.getInstance();
		try {

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String strDate = sdf1.format(c.getTime());
			date = sdf1.parse(strDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("DATE EXCEPTION", "PARSING ISSUE IN  DATE FORMAT");
		}
		return date;
	}

	public static ArrayList<ContactsModel> getContactsList() {

		if (sContacts.size() == 0) {
			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			cur = mContext.getContentResolver().query(uri, new String[] { "display_name", "sort_key", Data.CONTACT_ID, Phone.NUMBER }, null, null, "sort_key");

			if (cur.moveToFirst()) {
				try {
					do {
						String id = cur.getString(cur.getColumnIndex(Data.CONTACT_ID));
						String name = cur.getString(0);
						String number = cur.getString(cur.getColumnIndex(Phone.NUMBER));
						String sortKey = getSortKey(cur.getString(1));

						Log.e("sortKey from cursor", "" + sortKey);
						ContactsModel contacts = new ContactsModel();

						// long userId =
						// cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
						// photoUri =
						// ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
						// userId);

						contacts.setContactPhotoUri(getContactPhotoUri(Long.parseLong(id)));
						contacts.setName(name);
						contacts.setNumber(number);
						contacts.setSortKey(sortKey);
						sContacts.add(contacts);
					} while (cur.moveToNext());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return sContacts;
	}

	public static Uri getContactPhotoUri(long contactId) {
		Uri photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		photoUri = Uri.withAppendedPath(photoUri, Contacts.Photo.CONTENT_DIRECTORY);
		return photoUri;
	}

	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	public static ArrayList<CallLog> getCallLogs() {
		db = new DataBaseHandler(mContext);
		try {
			if (CallLogs.size() == 0) {

				CallLogs = db.getAllCallLogs();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < CallLogs.size(); i++) {
			for (int j = 0; j < RegisterationActivity.mContact.size(); j++) {

				String logNumber = CallLogs.get(i).getContactNumber();
				String contactNumber = RegisterationActivity.mContact.get(j).getNumber();
				if (logNumber.equals(contactNumber)) {
					CallLogs.get(i).setContactPhoto(RegisterationActivity.mContact.get(j).getContactPhoto());
					CallLogs.get(i).setContactPhotoUri(RegisterationActivity.mContact.get(j).getContactPhotoUri());
					break;
				}
			}
		}

		Collections.sort(CallLogs, new CallLog.DateComparator());

		// TODO:Needs fine tunning
		// GroupedLogs gpLogs = new GroupedLogs();
		//
		// for (int i = 0; i < CallLogs.size(); i++) {
		// if (groupedCallLogs.size() == 0) {
		// prevMsisdn = CallLogs.get(i).getContactNumber();
		//
		// gpLogs.callLog.add(CallLogs.get(i));
		// gpLogs.setMsisdn(prevMsisdn);
		// groupedCallLogs.add(gpLogs);
		//
		// } else if (prevMsisdn.equals(CallLogs.get(i).getContactNumber())) {
		// // TODO : Adding/grouping logic in call logs still an issue
		// for (int j = 0; j < groupedCallLogs.size(); j++) {
		//
		// // groupedCallLogs.get(j).getCallLog().set(j, CallLogs.get(i));
		// groupedCallLogs.get(j).setCallLog();
		// prevMsisdn = CallLogs.get(i).getContactNumber();
		// break;
		// }
		//
		// } else {
		// gpLogs.callLog.add(CallLogs.get(i));
		// gpLogs.setMsisdn(prevMsisdn);
		// groupedCallLogs.add(gpLogs);
		// }
		// }

		return CallLogs;
	}
}
