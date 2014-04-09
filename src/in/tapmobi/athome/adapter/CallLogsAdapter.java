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
import android.widget.RelativeLayout;
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

	@SuppressWarnings("null")
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.call_log_items, null);
			holder = new ViewHolder();
			holder.sectionHeaderDate = (RelativeLayout) convertView.findViewById(R.id.sort_by_date);
			holder.sectionHeaderText = (TextView) convertView.findViewById(R.id.sort_date_text);
			holder.ContactName = (TextView) convertView.findViewById(R.id.txtContactName);
			holder.contactNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);
			holder.timeDuration = (TextView) convertView.findViewById(R.id.txtCallTime);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.count = (TextView) convertView.findViewById(R.id.txtCallCount);
			holder.sectionHeaderDate.setVisibility(View.GONE);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CallLog Logs = mCallLogs.get(position);
		holder.thumb.setImageURI(Logs.getContactPhotoUri());
		if (holder.thumb.getDrawable() == null)
			holder.thumb.setImageResource(R.drawable.def_contact);

		holder.count.setVisibility(View.GONE);
		if (Logs.getCount() == 0) {
			holder.count.setVisibility(View.GONE);
		} else {
			holder.count.setVisibility(View.VISIBLE);
			holder.count.setText("(" + String.valueOf(Logs.getCount()) + ")");
		}
		Name = Logs.getContactName();
		number = Logs.getContactNumber();
		if (Name != null) {
			holder.ContactName.setText(Name);
			holder.contactNumber.setText(number);
		} else {
			holder.ContactName.setText(number);
			holder.contactNumber.setText("");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		Date dt = Logs.getmDateTimeStamp();
		String timeStamp = sdf.format(dt);
		String mDateStr = sdf1.format(dt);
		Date todayDate = Utility.getCurrentDateTime();
		today = sdf1.format(todayDate);
		System.out.println("Today Date" + today);

		holder.timeDuration.setText(timeStamp);

		try {
			if (position == 0) {
				checkDate = mDateStr;
				System.out.println("Printing the Check date -----------\n" + checkDate);
				// if (today.equals(checkDate) && flag) {
				//
				// holder.sectionHeaderDate.setVisibility(View.VISIBLE);
				// holder.sectionHeaderText.setText("Today");
				// flag = false;
				// } else {
				// holder.sectionHeaderDate.setVisibility(View.VISIBLE);
				// holder.sectionHeaderText.setText(mDateStr);
				// flag = false;
				// }
			} else if (checkDate == null || !checkDate.equals(mDateStr)) {

				checkDate = mDateStr;
				holder.sectionHeaderDate.setVisibility(View.VISIBLE);
				holder.sectionHeaderText.setText(mDateStr);

			} else {
				holder.sectionHeaderDate.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return convertView;
	}

	private static class ViewHolder {
		RelativeLayout sectionHeaderDate;
		TextView sectionHeaderText;
		TextView ContactName;
		TextView contactNumber;
		TextView timeDuration;
		ImageView thumb;
		TextView count;

	}

}
