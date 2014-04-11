package in.tapmobi.athome.models;

import java.util.Comparator;
import java.util.Date;

import android.graphics.Bitmap;
import android.net.Uri;

public class CallLog implements Comparable<CallLog> {

	private String callId;
	private String contactName;
	private int count;
	private String contactNumber;
	private String contactEmail;
	private String callDuration;
	private Bitmap contactPhoto;
	private Uri contactPhotoUri;
	private boolean IsIncoming;
	private Date mDateTimeStamp;

	public CallLog() {

	}

	public CallLog(String Name, String Number, String time, boolean incoming) {
		this.contactName = Name;
		this.contactNumber = Number;
		this.callDuration = time;
		this.IsIncoming = incoming;

	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Bitmap getContactPhoto() {
		return contactPhoto;
	}

	public void setContactPhoto(Bitmap contactPhoto) {
		this.contactPhoto = contactPhoto;
	}

	public Uri getContactPhotoUri() {
		return contactPhotoUri;
	}

	public void setContactPhotoUri(Uri contactPhotoUri) {
		this.contactPhotoUri = contactPhotoUri;
	}

	public boolean isIsIncoming() {
		return IsIncoming;
	}

	public void setIsIncoming(boolean isIncoming) {
		IsIncoming = isIncoming;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getmDateTimeStamp() {
		return mDateTimeStamp;
	}

	public void setmDateTimeStamp(Date mDateTimeStamp) {
		this.mDateTimeStamp = mDateTimeStamp;
	}

	@Override
	public int compareTo(CallLog cl) {

		return getmDateTimeStamp().compareTo(cl.getmDateTimeStamp());
	}

	public static class DateComparator implements Comparator<CallLog> {
		@Override
		public int compare(CallLog lhs, CallLog rhs) {

			if (lhs.getmDateTimeStamp().getTime() < rhs.getmDateTimeStamp().getTime())
				return 1;
			else if (lhs.getmDateTimeStamp().getTime() == rhs.getmDateTimeStamp().getTime())
				return 0;
			else
				return -1;
		}
	}
}
