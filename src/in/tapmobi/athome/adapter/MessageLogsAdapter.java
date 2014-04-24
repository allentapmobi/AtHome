package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.Message;
import in.tapmobi.athome.util.Utility;

import java.text.ParseException;
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

public class MessageLogsAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Message> mMessage = new ArrayList<Message>();
	String Name, number = null;
	private LayoutInflater mInflater;
	String checkDate, today = null;

	// Flag to check if the date occurrence is first time

	public MessageLogsAdapter(Context c, ArrayList<Message> msg) {
		mContext = c;
		this.mMessage = msg;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMessage.size();
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

			convertView = mInflater.inflate(R.layout.msg_log_item, null);
			holder = new ViewHolder();
			holder.ContactName = (TextView) convertView.findViewById(R.id.txtContactName);
			holder.timeDuration = (TextView) convertView.findViewById(R.id.txtCallTime);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.count = (TextView) convertView.findViewById(R.id.txtCallCount);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Message msgsLogs = mMessage.get(position);
		// Set Image for contact

		holder.thumb.setImageResource(R.drawable.def_contact);
		// Set Count
		holder.count.setVisibility(View.GONE);

		// Set Name if exists else set Number
		Name = msgsLogs.getSenderName();
		number = msgsLogs.getSenderNumber();
		if (Name != null) {
			holder.ContactName.setText(Name);

		} else {
			holder.ContactName.setText(number);

		}
		// if (Logs.isIsIncoming()) {
		// holder.callType.setImageResource(R.drawable.incoming);
		// } else if (!Logs.isIsIncoming()) {
		// holder.callType.setImageResource(R.drawable.outgoing);
		// }

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		String dtTime = msgsLogs.getTextDateTime();
		Date dt = null;
		try {
			dt = sdf1.parse(dtTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Date dt = Logs.getmDateTimeStamp();
		// String timeStamp = sdf.format(dt);
		String mDateStr = sdf1.format(dt);

		Date todayDate = Utility.getCurrentDateTime();
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
		TextView timeDuration;
		ImageView thumb;
		TextView count;

	}

}
