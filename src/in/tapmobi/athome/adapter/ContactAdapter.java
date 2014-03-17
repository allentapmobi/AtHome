package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.ContactsModel;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements Filterable {
	private ArrayList<ContactsModel> contactList;
	public ArrayList<ContactsModel> originalList;
	private Context mContext;
	private LayoutInflater mInflater;
	private AlphabetIndexer mIndexer;

	public ContactAdapter(Context context, ArrayList<ContactsModel> items) {
		super();
		this.mContext = context;
		this.contactList = new ArrayList<ContactsModel>();
		this.originalList = new ArrayList<ContactsModel>();

		this.contactList.addAll(items);
		this.originalList.addAll(items);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_item, null);
			holder = new ViewHolder();
			holder.sortKeyLayout = (LinearLayout) convertView.findViewById(R.id.sort_key_layout);
			holder.sortKey = (TextView) convertView.findViewById(R.id.sort_key);
			holder.Name = (TextView) convertView.findViewById(R.id.Name);
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ContactsModel contact = contactList.get(position);
		holder.thumb.setImageURI(contact.getContactPhotoUri());
		if (holder.thumb.getDrawable() == null)
			holder.thumb.setImageResource(R.drawable.def_contact);

		holder.Name.setText(contact.getName());

		return convertView;
	}

	@Override
	public int getCount() {
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		LinearLayout sortKeyLayout;
		TextView sortKey;
		TextView Name;
		ImageView thumb;

	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return myFilter;
	}

	/**
	 * 
	 * @param indexer
	 */
	public void setIndexer(AlphabetIndexer indexer) {
		mIndexer = indexer;
	}

	Filter myFilter = new Filter() {

		@SuppressWarnings("unchecked")
		@Override
		public void publishResults(CharSequence constraint, FilterResults results) {
			contactList = (ArrayList<ContactsModel>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

		@Override
		public FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			ArrayList<ContactsModel> tempContactList = new ArrayList<ContactsModel>();

			if (constraint != null && contactList != null) {
				int length = contactList.size();
				Log.i("Filtering", "glossaries size" + length);
				int i = 0;
				while (i < length) {
					ContactsModel item = contactList.get(i);
					// Real filtering:
					if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
						tempContactList.add(item);
					}
					i++;
				}

				filterResults.values = tempContactList;
				filterResults.count = tempContactList.size();
				Log.i("Filtering", "Filter result count size" + filterResults.count);
			}
			return filterResults;
		}
	};

}
