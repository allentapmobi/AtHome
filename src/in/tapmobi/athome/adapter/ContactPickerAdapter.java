package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.ContactsModel;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactPickerAdapter extends BaseAdapter {

	private Context mContext;
	public static Uri sPhotoUri;
	private LayoutInflater mInflater;
	private ArrayList<ContactsModel> contactsList;

	public ContactPickerAdapter(Context context, ArrayList<ContactsModel> contacts) {
		super();
		this.mContext = context;
		this.contactsList = contacts;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return contactsList.size();
	}

	@Override
	public Object getItem(int position) {
		return contactsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_msg_item, null);
			holder = new ViewHolder();

			holder.contactName = (TextView) convertView.findViewById(R.id.Name);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.contactNumber = (TextView) convertView.findViewById(R.id.Number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ContactsModel glossary = contactsList.get(position);
		holder.contactName.setText(glossary.getName());
		holder.contactNumber.setText(glossary.getNumber());
		holder.thumb.setImageURI(glossary.getContactPhotoUri());
		if (holder.thumb.getDrawable() == null)
			holder.thumb.setImageResource(R.drawable.def_contact);
		return convertView;
	}

	private static class ViewHolder {

		TextView contactName;
		TextView contactNumber;
		ImageView thumb;
	}
}
