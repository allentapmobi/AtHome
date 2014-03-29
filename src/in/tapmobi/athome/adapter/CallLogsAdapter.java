package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.CallLogs;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CallLogsAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<CallLogs> mCallLogs = new ArrayList<CallLogs>();
	String Name, number = null;
	private LayoutInflater mInflater;

	public CallLogsAdapter(Context c, ArrayList<CallLogs> callLogs) {
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.call_log_items, null);
			holder = new ViewHolder();
			holder.sectionHeaderDate = (LinearLayout) convertView.findViewById(R.id.sort_by_date);
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

		final CallLogs Logs = mCallLogs.get(position);
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

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
		Date dt = Logs.getmDateTimeStamp();
		String timeNow = sdf.format(dt);
		holder.timeDuration.setText(timeNow);
		Date currentTime = Utility.getCurrentDateTime();
		if (dt.before(currentTime)) {// returns true if current greater
			holder.sectionHeaderDate.setVisibility(View.GONE);
			holder.sectionHeaderText.setText("Yesterday");

		} else if (dt.after(currentTime)) {
			holder.sectionHeaderDate.setVisibility(View.GONE);
		}

		return convertView;
	}

	private static class ViewHolder {
		LinearLayout sectionHeaderDate;
		TextView sectionHeaderText;
		TextView ContactName;
		TextView contactNumber;
		TextView timeDuration;
		ImageView thumb;
		TextView count;

	}

}
