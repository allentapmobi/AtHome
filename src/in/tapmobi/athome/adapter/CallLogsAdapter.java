package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.CallLogs;

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
			holder.ContactName = (TextView) convertView.findViewById(R.id.txtContactName);
			holder.contactNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);
			holder.timeDuration = (TextView) convertView.findViewById(R.id.txtCallTime);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.sectionHeaderDate.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CallLogs Logs = mCallLogs.get(position);
		holder.thumb.setImageURI(Logs.getContactPhotoUri());
		if (holder.thumb.getDrawable() == null)
			holder.thumb.setImageResource(R.drawable.def_contact);
		holder.ContactName.setText(Logs.getContactName());
		holder.contactNumber.setText(Logs.getContactNumber());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		Date dt = Logs.getmDateTimeStamp();
		String now = sdf.format(dt);
		holder.timeDuration.setText(now);

		return convertView;
	}

	private static class ViewHolder {
		LinearLayout sectionHeaderDate;
		TextView ContactName;
		TextView contactNumber;
		TextView timeDuration;
		ImageView thumb;

	}

}
