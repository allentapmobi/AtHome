package in.tapmobi.athome.util;

import in.tapmobi.athome.contacts.ContactsFragment;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.CallLogs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Utility {
	public static Context mContext;
	public static Date date;
	public static int count = 0;
	public static DataBaseHandler db;
	public static ArrayList<CallLogs> sLogs = new ArrayList<CallLogs>();

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

//		Bitmap contactImg = null;
//		Uri contactImgUri = null;
//		CallLogs callLogs = new CallLogs();
		String currentTime = getCurrentTime();
		db = new DataBaseHandler(mContext);

		for (int i = 0; i < ContactsFragment.mContactsList.size(); i++) {
			String ContactNumber = ContactsFragment.mContactsList.get(i).getNumber();
			if (ContactNumber.contains(Msisdn)) {
				UserName = ContactsFragment.mContactsList.get(i).getName();
				// contactImg =
				// ContactsFragment.mContactsList.get(i).getContactPhoto();
				// contactImgUri =
				// ContactsFragment.mContactsList.get(i).getContactPhotoUri();
				break;
			}
		}

		// if (sLogs.size() > 0) {
		// int pos = sLogs.size() - 1;
		// String prevContactNumber = sLogs.get(pos).getContactNumber();
		//
		// count = 1;
		// if (prevContactNumber.contains(Msisdn)) {
		// count++;
		// callLogs.setContactName(UserName);
		// callLogs.setContactNumber(Msisdn);
		// callLogs.setContactPhoto(contactImg);
		// callLogs.setCount(count);
		// callLogs.setContactPhotoUri(contactImgUri);
		// callLogs.setCallDuration(currentTime);
		// sLogs.add(pos, callLogs);
		// } else {
		// count = 0;
		// }
		// } else {
		// callLogs.setContactName(UserName);
		// callLogs.setContactNumber(Msisdn);
		// callLogs.setContactPhoto(contactImg);
		// callLogs.setContactPhotoUri(contactImgUri);
		// callLogs.setCallDuration(currentTime);
		// sLogs.add(callLogs);
		// }
		// logs.get(count).setContactName(contactName);

		Log.d("Insert: ", "Inserting ..");
		// ADDING ALL THE VALUES FROM THE ARRAY TO DB
		db.addCallLogs(new CallLogs(UserName, Msisdn, currentTime));
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
}
