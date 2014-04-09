package in.tapmobi.athome.adapter;

import in.tapmobi.athome.models.ContactsModel;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class DialpadAdapter extends BaseAdapter implements Filterable {

	private ArrayList<ContactsModel> contactsList;
	private ArrayList<ContactsModel> contactsListForSearch;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public Filter getFilter() {
		// TODO Auto-generated method stub
		return myFilter;
	}

	Filter myFilter = new Filter() {
		@SuppressWarnings("unchecked")
		public void publishResults(CharSequence constraints, FilterResults results) {
			contactsList = (ArrayList<ContactsModel>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

		public FilterResults performFiltering(CharSequence constrains) {
			FilterResults filterResults = new FilterResults();
			ArrayList<ContactsModel> tempContacts = new ArrayList<ContactsModel>();

			if (constrains != null && contactsListForSearch != null) {
				int lenght = contactsListForSearch.size();
				int i = 0;
				while (i < lenght) {
					ContactsModel item = contactsListForSearch.get(i);
					if (item.getNumber().contains(constrains.toString())) {

					}
				}

			}
			return null;

		}
	};

}
