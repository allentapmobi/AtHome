package in.tapmobi.athome.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Message implements Comparable<Message> {

	private String SenderName;
	private String SenderNumber;
	private String textDateTime;
	private String TxtMsg;

	public Message() {

	}

	public Message(String Name, String Number, String time, String txtmsg) {
		this.SenderName = Name;
		this.SenderNumber = Number;
		this.textDateTime = time;
		this.TxtMsg = txtmsg;

	}

	public String getSenderName() {
		return SenderName;
	}

	public void setSenderName(String senderName) {
		SenderName = senderName;
	}

	public String getSenderNumber() {
		return SenderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		SenderNumber = senderNumber;
	}

	public String getTextDateTime() {
		return textDateTime;
	}

	public void setTextDateTime(String textDateTime) {
		this.textDateTime = textDateTime;
	}

	public String getTxtMsg() {
		return TxtMsg;
	}

	public void setTxtMsg(String txtMsg) {
		TxtMsg = txtMsg;
	}

	@Override
	public int compareTo(Message msg) {
		return getTextDateTime().compareTo(msg.getTextDateTime());
	}

	public static class DateComparatorText implements Comparator<Message> {

		@Override
		public int compare(Message lhs, Message rhs) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date dateLhs = null, dateRhs = null;
			try {
				dateLhs = sdf.parse(lhs.getTextDateTime());
				dateRhs = sdf.parse(rhs.getTextDateTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("" +e);
			}
			if (dateLhs.getTime() < dateRhs.getTime())
				return 1;
			else if (dateLhs.getTime() == dateRhs.getTime())
				return 0;
			else
				return -1;

		}

	}

}
