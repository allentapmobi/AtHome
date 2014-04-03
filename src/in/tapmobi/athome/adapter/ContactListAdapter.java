package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.ContactsModel;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
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

public class ContactListAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	private ArrayList<ContactsModel> contactsList;
	/** This is mainly for listview search **/
	private ArrayList<ContactsModel> contactsListForSearch;
	private LayoutInflater mInflater;
	private AlphabetIndexer mIndexer;
	public static Uri sPhotoUri;

	public ContactListAdapter(Context context, ArrayList<ContactsModel> contacts) {
		super();
		this.mContext = context;
		this.contactsList = contacts;
		contactsListForSearch = contacts;
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
			convertView = mInflater.inflate(R.layout.contact_item, null);
			holder = new ViewHolder();
			holder.sortKeyLayout = (LinearLayout) convertView.findViewById(R.id.sort_key_layout);
			holder.sortKey = (TextView) convertView.findViewById(R.id.sort_key);
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
		int section = mIndexer.getSectionForPosition(position);
		if (position == mIndexer.getPositionForSection(section)) {
			holder.sortKey.setText(glossary.getSortKey());
			holder.sortKeyLayout.setVisibility(View.VISIBLE);
		} else {
			holder.sortKeyLayout.setVisibility(View.GONE);
		}
		return convertView;
	}

	/*
	 * @Override public Object[] getSections() { return null; }
	 * 
	 * @Override public int getPositionForSection(int section) { return 0; }
	 * 
	 * @Override public int getSectionForPosition(int position) { return 0; }
	 */

	/**
	 * 
	 * @param indexer
	 */
	public void setIndexer(AlphabetIndexer indexer) {
		mIndexer = indexer;
	}

	private static class ViewHolder {
		LinearLayout sortKeyLayout;
		TextView sortKey;
		TextView contactName;
		TextView contactNumber;
		ImageView thumb;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return myFilter;
	}

	Filter myFilter = new Filter() {

		@SuppressWarnings("unchecked")
		@Override
		public void publishResults(CharSequence constraint, FilterResults results) {
			contactsList = (ArrayList<ContactsModel>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

		@Override
		public FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			ArrayList<ContactsModel> tempContactsList = new ArrayList<ContactsModel>();

			if (constraint != null && contactsListForSearch != null) {
				int length = contactsListForSearch.size();
				Log.i("Filtering", "glossaries size" + length);
				int i = 0;
				while (i < length) {
					ContactsModel item = contactsListForSearch.get(i);
					// Real filtering:
					if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
						tempContactsList.add(item);
					}
					i++;
				}

				filterResults.values = tempContactsList;
				filterResults.count = tempContactsList.size();
				Log.i("Filtering", "Filter result count size" + filterResults.count);
			}
			return filterResults;
		}
	};
}
