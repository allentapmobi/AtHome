package in.tapmobi.athome.models;

import java.util.ArrayList;

public class GroupedLogs {

	public ArrayList<CallLog> callLog = new ArrayList<CallLog>();
	public String Msisdn;
	

	public ArrayList<CallLog> getCallLog() {
		return callLog;
	}

	public void setCallLog(ArrayList<CallLog> callLog) {
		this.callLog = callLog;
	}

	public String getMsisdn() {
		return Msisdn;
	}

	public void setMsisdn(String msisdn) {
		Msisdn = msisdn;
	}


}
