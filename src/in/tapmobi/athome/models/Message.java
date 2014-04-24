package in.tapmobi.athome.models;


public class Message {

	private String SenderName;
	private String SenderNumber;
	private String textDateTime;
	private String TxtMsg;
	
	public Message(){
		
	}
	
	public Message(String Name,String Number,String time,String txtmsg){
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

}
