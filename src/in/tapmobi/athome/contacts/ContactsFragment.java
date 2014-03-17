package in.tapmobi.athome.contacts;

import in.tapmobi.athome.R;
import in.tapmobi.athome.adapter.ContactListAdapter;
import in.tapmobi.athome.models.ContactsModel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactsFragment extends Fragment {

	protected static final String TAG = "ContactsFragment";
	private LinearLayout mIndexerLayout;
	private ListView mListView;
	private FrameLayout mTitleLayout;
	private TextView mTitleText;
	private RelativeLayout mSectionToastLayout;
	private TextView mSectionToastText;
	public static ArrayList<ContactsModel> mContactsList = new ArrayList<ContactsModel>();
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private AlphabetIndexer mIndexer;
	private int lastSelectedPosition = -1;
	private ContactListAdapter mAdapter;
	RelativeLayout progressLayout;
	private EditText myFilter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
		mIndexerLayout = (LinearLayout) rootView.findViewById(R.id.indexer_layout);
		mListView = (ListView) rootView.findViewById(R.id.contactList);
		mTitleLayout = (FrameLayout) rootView.findViewById(R.id.title_layout);
		mTitleText = (TextView) rootView.findViewById(R.id.title_text);
		mSectionToastLayout = (RelativeLayout) rootView.findViewById(R.id.section_toast_layout);
		mSectionToastText = (TextView) rootView.findViewById(R.id.section_toast_text);
		myFilter = (EditText) rootView.findViewById(R.id.search_txt);

		// /////////// Custom progress Layout //////////////////////
		progressLayout = (RelativeLayout) rootView.findViewById(R.id.progress_layout);

		progressLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		progressLayout.setVisibility(View.GONE); // by default progress view to
													// GONE
		// /////////////////////////////////////

		for (int i = 0; i < alphabet.length(); i++) {
			
			TextView letterTextView = new TextView(getActivity().getApplicationContext());
			letterTextView.setText(alphabet.charAt(i) + "");
			letterTextView.setTextSize(14f);
			letterTextView.setGravity(Gravity.CENTER);
			LayoutParams params = new LinearLayout.LayoutParams(28, 0, 1.0f);
			letterTextView.setLayoutParams(params);
			letterTextView.setPadding(4, 0, 2, 0);
			mIndexerLayout.addView(letterTextView);
			mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
		}

		initView();

		return rootView;
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		// Register handler for UI elements
		// Add text listner to the edit text for filtering the List
		myFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// call the filter with the current text on the editbox
				 mAdapter.getFilter().filter(s.toString());
			}
		});

		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Cursor cur = getActivity().getContentResolver().query(uri, new String[] { "display_name", "sort_key", Data.CONTACT_ID ,Phone.NUMBER}, null, null,
				"sort_key");

		if (cur.moveToFirst()) {
			try {
				do {
					String id = cur.getString(cur.getColumnIndex(Data.CONTACT_ID));
					String name = cur.getString(0);
					String number = cur.getString(cur.getColumnIndex(Phone.NUMBER));
					String sortKey = getSortKey(cur.getString(1));
				
					Log.e("sortKey from cursor", "" + sortKey);
					ContactsModel contacts = new ContactsModel();
					

					// long userId =
					// cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
					// photoUri =
					// ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
					// userId);

					contacts.setContactPhotoUri(getContactPhotoUri(Long.parseLong(id)));
					contacts.setName(name);
					contacts.setNumber(number);
					contacts.setSortKey(sortKey);
					mContactsList.add(contacts);
				} while (cur.moveToNext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		mAdapter = new ContactListAdapter(getActivity().getApplicationContext(), mContactsList);

		mIndexer = new AlphabetIndexer(cur, 1, alphabet);
		mAdapter.setIndexer(mIndexer);

		if (mContactsList != null && mContactsList.size() > 0) {
			mListView.setAdapter(mAdapter);
			mListView.setOnScrollListener(mOnScrollListener);
			mIndexerLayout.setOnTouchListener(mOnTouchListener);
		}

	}

	public Uri getContactPhotoUri(long contactId) {
		Uri photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		photoUri = Uri.withAppendedPath(photoUri, Contacts.Photo.CONTENT_DIRECTORY);
		return photoUri;
	}

	private String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		private int lastFirstVisibleItem = -1;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				// mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				// mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
			} else {
				// mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// firstVisibleItem corresponding to the index of
			// AlphabetIndexer(eg, B-->Alphabet index is 2)
			int sectionIndex = mIndexer.getSectionForPosition(firstVisibleItem);
			// next section Index corresponding to the positon in the listview
			int nextSectionPosition = mIndexer.getPositionForSection(sectionIndex + 1);
			Log.d(TAG, "onScroll()-->firstVisibleItem=" + firstVisibleItem + ", sectionIndex=" + sectionIndex + ", nextSectionPosition="
					+ nextSectionPosition);
			if (firstVisibleItem != lastFirstVisibleItem) {
				MarginLayoutParams params = (MarginLayoutParams) mTitleLayout.getLayoutParams();
				params.topMargin = 0;
				mTitleLayout.setLayoutParams(params);
				mTitleText.setText(String.valueOf(alphabet.charAt(sectionIndex)));
				((TextView) mIndexerLayout.getChildAt(sectionIndex)).setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
				lastFirstVisibleItem = firstVisibleItem;
			}

			// update AlphabetIndexer background
			if (sectionIndex != lastSelectedPosition) {
				if (lastSelectedPosition != -1) {
					((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(
							android.R.color.transparent));
				}
				lastSelectedPosition = sectionIndex;
			}

			if (nextSectionPosition == firstVisibleItem + 1) {
				View childView = view.getChildAt(0);
				if (childView != null) {
					int sortKeyHeight = mTitleLayout.getHeight();
					int bottom = childView.getBottom();
					MarginLayoutParams params = (MarginLayoutParams) mTitleLayout.getLayoutParams();
					/*
					 * if(bottom < sortKeyHeight) { float pushedDistance =
					 * bottom - sortKeyHeight; params.topMargin = (int)
					 * pushedDistance; mTitleLayout.setLayoutParams(params); }
					 * else {
					 */
					if (params.topMargin != 0) {
						params.topMargin = 0;
						mTitleLayout.setLayoutParams(params);
					}
					// }
				}
			}

		}

	};

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			float alphabetHeight = mIndexerLayout.getHeight();
			float y = event.getY();
			int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
			if (sectionPosition < 0) {
				sectionPosition = 0;
			} else if (sectionPosition > 26) {
				sectionPosition = 26;
			}
			if (lastSelectedPosition != sectionPosition) {
				if (-1 != lastSelectedPosition) {
					((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(
							android.R.color.transparent));
				}
				lastSelectedPosition = sectionPosition;
			}
			String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
			int position = mIndexer.getPositionForSection(sectionPosition);
			TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
				textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
				mSectionToastLayout.setVisibility(View.VISIBLE);
				mSectionToastText.setText(sectionLetter);
				mListView.smoothScrollToPositionFromTop(position, 0, 1);
				break;
			case MotionEvent.ACTION_MOVE:
				mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
				textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
				mSectionToastLayout.setVisibility(View.VISIBLE);
				mSectionToastText.setText(sectionLetter);
				mListView.smoothScrollToPositionFromTop(position, 0, 1);
				break;
			case MotionEvent.ACTION_UP:
				// mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				mSectionToastLayout.setVisibility(View.GONE);
			default:
				mSectionToastLayout.setVisibility(View.GONE);
				break;
			}
			return true;
		}

	};

	public boolean onQueryTextChange(String newText) {
		// mStatusView.setText("Query = " + newText);
		Log.i("Action Search Query", newText);
		mAdapter.getFilter().filter(newText);
		Log.i("mAdapter", "" + newText);
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		// mStatusView.setText("Query = " + query + " : submitted");
		Log.i("Action Search Query", query);
		return false;
	}

	public boolean onClose() {
		// mStatusView.setText("Closed!");
		Log.i("Action Search Query", "^%^%^%^%^");
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

}
