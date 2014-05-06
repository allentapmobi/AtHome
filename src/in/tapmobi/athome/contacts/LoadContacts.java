//package in.tapmobi.athome.contacts;
//
//import in.tapmobi.athome.models.ContactsModel;
//
//import java.util.ArrayList;
//
//import android.content.ContentUris;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.CommonDataKinds.Phone;
//import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Data;
//import android.util.Log;
//
//public class LoadContacts {
//	static Context mContext;
//	static Cursor cur;
//	
//	public LoadContacts(Context ctx){
//		mContext = ctx;
//	}
//
//	public static ArrayList<ContactsModel> mContactsList = new ArrayList<ContactsModel>();
//
//public static void contactsReterive(){
//	if (mContactsList == null && mContactsList.size() < 0){
//		mContactsList.addAll(getContactsFromPhone());
//	}
//}
//	public static ArrayList<ContactsModel> getContactsFromPhone() {
//
//		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//		cur = mContext.getContentResolver().query(uri, new String[] { "display_name", "sort_key", Data.CONTACT_ID, Phone.NUMBER }, null, null,
//				"sort_key");
//
//		if (cur.moveToFirst()) {
//			try {
//				do {
//					String id = cur.getString(cur.getColumnIndex(Data.CONTACT_ID));
//					String name = cur.getString(0);
//					String number = cur.getString(cur.getColumnIndex(Phone.NUMBER));
//					String sortKey = getSortKey(cur.getString(1));
//
//					Log.e("sortKey from cursor", "" + sortKey);
//					ContactsModel contacts = new ContactsModel();
//					contacts.setContactPhotoUri(getContactPhotoUri(Long.parseLong(id)));
//					contacts.setName(name);
//					contacts.setNumber(number);
//					contacts.setSortKey(sortKey);
//					mContactsList.add(contacts);
//				} while (cur.moveToNext());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return mContactsList;
//	}
//	
//	private static String getSortKey(String sortKeyString) {
//		String key = sortKeyString.substring(0, 1).toUpperCase();
//		if (key.matches("[A-Z]")) {
//			return key;
//		}
//		return "#";
//	}
//
//	public static Uri getContactPhotoUri(long contactId) {
//		Uri photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
//		photoUri = Uri.withAppendedPath(photoUri, Contacts.Photo.CONTENT_DIRECTORY);
//		return photoUri;
//	}
//
// }
