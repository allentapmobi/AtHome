package in.tapmobi.athome;

import in.tapmobi.athome.adapter.ContactListAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.models.CallLog;
import in.tapmobi.athome.models.ContactsModel;
import in.tapmobi.athome.registration.RegisterationActivity;
import in.tapmobi.athome.sip.SipRegisteration;
import in.tapmobi.athome.util.Utility;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends Fragment {

	protected static final String TAG = "ContactsFragment";
	private LinearLayout mIndexerLayout;
	private ListView mListView;
	private FrameLayout mTitleLayout;
	private TextView mTitleText;
	private RelativeLayout mSectionToastLayout;
	private TextView mSectionToastText;
	// public static ArrayList<ContactsModel> mContactsList = new
	// ArrayList<ContactsModel>();
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private AlphabetIndexer mIndexer;
	private int lastSelectedPosition = -1;
	private ContactListAdapter mAdapter;
	RelativeLayout progressLayout;
	private EditText myFilter;
	String Msisdn, UserName = null;
	ArrayList<CallLog> logs;
	DataBaseHandler db;

	public static Bitmap sPhotoImg;
	public static Uri sPhotoUri;

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
		db = new DataBaseHandler(getActivity().getApplicationContext());

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
			if (MainActivity.height > 800) {

				letterTextView.setText(alphabet.charAt(i) + "");
				// letterTextView.setTextSize(12f);
				letterTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (MainActivity.height * 2.2 / 100));
				letterTextView.setGravity(Gravity.CENTER);
				LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				letterTextView.setLayoutParams(params);
				// letterTextView.setPadding(4, 0, 2, 0);
				mIndexerLayout.addView(letterTextView);
				mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);

			} else if (MainActivity.height <= 480) {
				// TODO:NEED TO DISABLE SCROLL HERE
				letterTextView.setText(alphabet.charAt(i) + "");
				letterTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (MainActivity.height * 1.5 / 100));
				letterTextView.setGravity(Gravity.CENTER);
				LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				letterTextView.setLayoutParams(params);
				mIndexerLayout.addView(letterTextView);
				mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);

			} else {
				letterTextView.setText(alphabet.charAt(i) + "");
				letterTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (MainActivity.height * 1.67 / 100));
				letterTextView.setGravity(Gravity.CENTER);
				LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				letterTextView.setLayoutParams(params);
				letterTextView.setPadding(4, 0, 2, 0);
				mIndexerLayout.addView(letterTextView);
				mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
			}
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

		if (RegisterationActivity.mContact.size() == 0) {
			RegisterationActivity.mContact.addAll(Utility.getContactsList());
		}
		mAdapter = new ContactListAdapter(getActivity().getApplicationContext(), RegisterationActivity.mContact);

		mIndexer = new AlphabetIndexer(Utility.cur, 1, alphabet);
		mAdapter.setIndexer(mIndexer);

		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mOnScrollListener);
		mIndexerLayout.setOnTouchListener(mOnTouchListener);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3) {

				ContactsModel data = (ContactsModel) parent.getItemAtPosition(pos);
				Msisdn = data.getNumber();
				UserName = data.getName();
				// Msisdn = RegisterationActivity.mContact.get(pos).getNumber();
				// UserName = RegisterationActivity.mContact.get(pos).getName();
				try {
					// sPhotoUri = Utility.CallLogs.get(pos).getContactPhotoUri();
					// sPhotoUri = RegisterationActivity.mContact.get(pos).getContactPhotoUri();
					sPhotoUri = data.getContactPhotoUri();
					// sPhotoImg = Utility.CallLogs.get(pos).getContactPhoto();
					// sPhotoImg = RegisterationActivity.mContact.get(pos).getContactPhoto();
					sPhotoImg = data.getContactPhoto();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Msisdn != null) {
					new RegisterCallLogsAsync().execute();
				}
			}
		});
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
			Log.d(TAG, "onScroll()-->firstVisibleItem=" + firstVisibleItem + ", sectionIndex=" + sectionIndex + ", nextSectionPosition=" + nextSectionPosition);
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
					((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
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
					 * if(bottom < sortKeyHeight) { float pushedDistance = bottom - sortKeyHeight; params.topMargin = (int) pushedDistance;
					 * mTitleLayout.setLayoutParams(params); } else {
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
					((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
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
				try {
					mListView.smoothScrollToPositionFromTop(position, 0, 1);
				} catch (Exception e) {
					System.out.println("It just wouldnt scroll");
					e.printStackTrace();
				}
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

	public class RegisterCallLogsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {
				Utility.regInCallLogs(Msisdn, 1);
				// MainActivity.initSipManager();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (ProfileFragment.isActivated && SipRegisteration.isRegisteredWithSip) {
				Intent i = new Intent(getActivity().getApplicationContext(), InCallActivity.class);
				i.putExtra("CONTACT_NAME", UserName);
				i.putExtra("CONTACT_NUMBER", Msisdn);
				i.putExtra("BITMAP", sPhotoImg);
				// i.putExtra("IMAGE_URI", sPhotoUri.toString());
				startActivity(i);
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "SIP Registration failed.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
