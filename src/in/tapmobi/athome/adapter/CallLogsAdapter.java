package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.CallLog;
import in.tapmobi.athome.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallLogsAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<CallLog> mCallLogs = new ArrayList<CallLog>();
	String Name, number = null;
	private LayoutInflater mInflater;
	String checkDate, today = null;

	// Flag to check if the date occurrence is first time

	public CallLogsAdapter(Context c, ArrayList<CallLog> callLogs) {
		mContext = c;
		this.mCallLogs = callLogs;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCallLogs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.call_log_items, null);
			holder = new ViewHolder();
			holder.ContactName = (TextView) convertView.findViewById(R.id.txtContactName);
			holder.contactNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);
			holder.timeDuration = (TextView) convertView.findViewById(R.id.txtCallTime);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.count = (TextView) convertView.findViewById(R.id.txtCallCount);
			holder.callType = (ImageView) convertView.findViewById(R.id.ivCallType);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CallLog Logs = mCallLogs.get(position);
		// Set Image for contact
		holder.thumb.setImageURI(Logs.getContactPhotoUri());
		if (holder.thumb.getDrawable() == null)
			holder.thumb.setImageResource(R.drawable.def_contact);
		// Set Count
		holder.count.setVisibility(View.GONE);
		if (Logs.getCount() == 0) {
			holder.count.setVisibility(View.GONE);
		} else {
			holder.count.setVisibility(View.VISIBLE);
			holder.count.setText("(" + String.valueOf(Logs.getCount()) + ")");
		}
		// Set Name if exists else set Number
		Name = Logs.getContactName();
		number = Logs.getContactNumber();
		if (Name != null) {
			holder.ContactName.setText(Name);
			holder.contactNumber.setText(number);
		} else {
			holder.ContactName.setText(number);
			holder.contactNumber.setText("");
		}
		// if (Logs.isIsIncoming()) {
		// holder.callType.setImageResource(R.drawable.incoming);
		// } else if (!Logs.isIsIncoming()) {
		// holder.callType.setImageResource(R.drawable.outgoing);
		// }
		if (Logs.getCallType() == 0) {
			holder.callType.setImageResource(R.drawable.incoming);
		} else if (Logs.getCallType() == 1) {
			holder.callType.setImageResource(R.drawable.outgoing);
		} else if (Logs.getCallType() == 2) {
			holder.callType.setImageResource(R.drawable.missed);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		Date dt = Logs.getmDateTimeStamp();
		System.out.println("---------------RegDate---------"+dt);
		// String timeStamp = sdf.format(dt);
		String mDateStr = sdf1.format(dt);

		Date todayDate = Utility.getCurrentDateTime();
		System.out.println("----------------CurrentTime-----------"+ todayDate);
		today = sdf1.format(todayDate);

		long diff = todayDate.getTime() - dt.getTime();

		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffMinutes < 60) {
			holder.timeDuration.setText(diffMinutes + " mins ago");
		} else if (diffHours < 23) {
			holder.timeDuration.setText(diffHours + " hours ago");
		} else if (diffDays < 7) {
			holder.timeDuration.setText(diffDays + " days ago");
		} else {
			holder.timeDuration.setText(mDateStr);
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView ContactName;
		TextView contactNumber;
		TextView timeDuration;
		ImageView thumb;
		TextView count;
		ImageView callType;

	}

}
