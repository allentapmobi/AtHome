package in.tapmobi.athome.database;

import in.tapmobi.athome.models.CallLogs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "CallLogManager";

	// Contacts table name
	private static final String TABLE_CALL_LOGS = "callogs";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_TIME_DURATION = "call_duration";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CALL_LOGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CALL_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + KEY_NAME
				+ "  TEXT, " + KEY_PH_NO + "  TEXT," + KEY_TIME_DURATION + "  TEXT" + ")";
		System.out.println("---------------------------Table has been created------------------------------");
		System.out.println("CREATE TABLE IF NOT EXISTS " + TABLE_CALL_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + KEY_NAME + "  TEXT, "
				+ KEY_PH_NO + "  TEXT ," + KEY_TIME_DURATION + "  TEXT " + ")");
		db.execSQL(CREATE_CALL_LOGS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_LOGS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding CALL LOGS
	public void addCallLogs(CallLogs logs) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, logs.getContactName());
		values.put(KEY_PH_NO, logs.getContactNumber());
		// values.put(KEY_CONTACT_IMG, contact.getContactPhoto());
		values.put(KEY_TIME_DURATION, logs.getCallDuration());

		// Inserting Row
		db.insert(TABLE_CALL_LOGS, null, values);
		db.close(); // Closing database connection
	}

	// Getting All Contacts
	@SuppressLint("SimpleDateFormat")
	public ArrayList<CallLogs> getAllCallLogs() {
		ArrayList<CallLogs> contactList = new ArrayList<CallLogs>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CALL_LOGS;

		String callDateTime = null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Date date = new Date();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CallLogs logs = new CallLogs();
				// logs.setID(Integer.parseInt(cursor.getString(0)));
				// logs.setName(cursor.getString(1));
				// logs.setPhoneNumber(cursor.getString(2));

				// logs.setCallId(Integer.parseInt(cursor.getString(0)));
				logs.setContactName(cursor.getString(1));
				logs.setContactNumber(cursor.getString(2));
				callDateTime = cursor.getString(3);
				// SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				try {
					date = formatter.parse(callDateTime);
					Date date1 = formatter.parse(callDateTime);
					System.out.println(date);
					System.out.println(date1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				logs.setmDateTimeStamp(date);
				// Adding contact to list
				contactList.add(logs);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Getting contacts Count
	public int getcallLogsCount() {

		String countQuery = "SELECT  * FROM " + TABLE_CALL_LOGS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
