package in.tapmobi.athome.database;

import in.tapmobi.athome.models.CallLog;
import in.tapmobi.athome.models.Message;

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
	private static final String TABLE_MSG_LOGS = "msgLogs";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_TIME_DURATION = "call_duration";
	private static final String KEY_CALL_TYPE = "is_incoming";
	private static final String MSG_SENDER_NAME = "sender_name";
	private static final String MSG_SENDER_NUMBER = "sender_number";
	private static final String MSG_SENDER_TXTS = "sender_text";
	private static final String MSG_SENDER_TIME = "sender_time_sent";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CALL_LOGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CALL_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + KEY_NAME + "  TEXT, " + KEY_PH_NO
				+ "  TEXT," + KEY_TIME_DURATION + "  TEXT, " + KEY_CALL_TYPE + "  TEXT" + ")";

		String CREATE_MSG_LOGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MSG_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + MSG_SENDER_NAME + "  TEXT, "
				+ MSG_SENDER_NUMBER + "  TEXT, " + MSG_SENDER_TIME + "  TEXT, " + MSG_SENDER_TXTS + "  TEXT" + ")";

		System.out.println("---------------------------Table CALL_LOGS has been created------------------------------");
		System.out.println("CREATE TABLE IF NOT EXISTS " + TABLE_CALL_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + KEY_NAME + "  TEXT, " + KEY_PH_NO + "  TEXT,"
				+ KEY_TIME_DURATION + "  TEXT, " + KEY_CALL_TYPE + "  TEXT" + ")");

		db.execSQL(CREATE_CALL_LOGS_TABLE);
		System.out.println("---------------------------Table MSG_LOGS has been created------------------------------");
		db.execSQL(CREATE_MSG_LOGS_TABLE);
		System.out.println("CREATE TABLE IF NOT EXISTS " + TABLE_MSG_LOGS + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + MSG_SENDER_NAME + "  TEXT, " + MSG_SENDER_NUMBER
				+ "  TEXT, " + MSG_SENDER_TIME + "  TEXT, " + MSG_SENDER_TXTS + "  TEXT" + ")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_LOGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG_LOGS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding CALL LOGS
	public void addCallLogs(CallLog logs) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, logs.getContactName());
		values.put(KEY_PH_NO, logs.getContactNumber());
		// values.put(KEY_CONTACT_IMG, contact.getContactPhoto());
		values.put(KEY_TIME_DURATION, logs.getCallDuration());
		values.put(KEY_CALL_TYPE, logs.getCallType());

		// Inserting Row
		db.insert(TABLE_CALL_LOGS, null, values);
		db.close(); // Closing database connection
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

	// Getting All Contacts
	@SuppressLint("SimpleDateFormat")
	public ArrayList<CallLog> getAllCallLogs() {
		ArrayList<CallLog> contactList = new ArrayList<CallLog>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CALL_LOGS;

		String callDateTime = null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Date date = new Date();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CallLog logs = new CallLog();
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
				String CallType = cursor.getString(4);
				if (CallType != null) {
					logs.setCallType(Integer.parseInt(CallType));
				}
				contactList.add(logs);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	public void addMsgLogs(Message msg) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MSG_SENDER_NAME, msg.getSenderName());
		values.put(MSG_SENDER_NUMBER, msg.getSenderNumber());
		values.put(MSG_SENDER_TXTS, msg.getTxtMsg());
		values.put(MSG_SENDER_TIME, msg.getTextDateTime());

		// Inserting into DB
		db.insert(TABLE_MSG_LOGS, null, values);
		db.close();
	}

	// Getting all textMsgs from Db
	public ArrayList<Message> getMsgLogs() {
		ArrayList<Message> msgs = new ArrayList<Message>();
		String selectQuery = "SELECT * FROM " + TABLE_MSG_LOGS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery(selectQuery, null);
		// looping through all the rows
		if (cur.moveToFirst()) {
			do {
				Message msg = new Message();
				msg.setSenderName(cur.getString(1));
				msg.setSenderNumber(cur.getString(2));
				msg.setTextDateTime(cur.getString(3));
				msg.setTxtMsg(cur.getString(4));

				msgs.add(msg);
			} while (cur.moveToNext());
		}
		return msgs;
	}

}
