package in.tapmobi.athome;

import in.tapmobi.athome.adapter.MessageLogsAdapter;
import in.tapmobi.athome.database.DataBaseHandler;
import in.tapmobi.athome.messaging.ConversationActivity;
import in.tapmobi.athome.messaging.SelectContactsForMsgActivity;
import in.tapmobi.athome.models.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MessageLogsFragment extends Fragment {
	Button btnCompose;
	LinearLayout displayIfNoMsgs;
	ArrayList<Message> getMsgs = new ArrayList<Message>();
	ArrayList<Message> getMsgsFiltered = new ArrayList<Message>();
	HashMap<String, Message> groupedMsgMap = new HashMap<String, Message>();
	MessageLogsAdapter mMsgLogAdapter;
	ListView lvMsgLogs;

	DataBaseHandler db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_msg, container, false);

		db = new DataBaseHandler(getActivity().getApplicationContext());
		btnCompose = (Button) rootView.findViewById(R.id.ComposeMsg);
		lvMsgLogs = (ListView) rootView.findViewById(R.id.lvMsgLogs);
		displayIfNoMsgs = (LinearLayout) rootView.findViewById(R.id.firstTimeLinear);

		if (getMsgs.size() > 0 || getMsgs.size() == 0) {
			getMsgs.clear();
			getMsgs.addAll(db.getMsgLogs());
		}
		// method 5 works finally hurreay

		getMsgsFiltered = clearListFromDuplicateFirstName(getMsgs);
		if (getMsgsFiltered.size() > 0) {
			displayIfNoMsgs.setVisibility(View.GONE);
			lvMsgLogs.setVisibility(View.VISIBLE);
		} else {
			lvMsgLogs.setVisibility(View.GONE);
			displayIfNoMsgs.setVisibility(View.VISIBLE);
		}
		mMsgLogAdapter = new MessageLogsAdapter(getActivity().getApplicationContext(), getMsgsFiltered);

		lvMsgLogs.setAdapter(mMsgLogAdapter);
		lvMsgLogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String Name = getMsgsFiltered.get(position).getSenderName();
				String Number = getMsgsFiltered.get(position).getSenderNumber();

				Intent i = new Intent(getActivity().getApplicationContext(), ConversationActivity.class);
				i.putExtra("TEXT_NAME", Name);
				i.putExtra("TEXT_CONTACT_NUMBER", Number);
				startActivity(i);

			}
		});

		btnCompose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), SelectContactsForMsgActivity.class);
				startActivity(i);
			}
		});
		return rootView;
	}

	// remove duplicates
	private ArrayList<Message> clearListFromDuplicateFirstName(ArrayList<Message> list1) {

		Map<String, Message> cleanMap = new LinkedHashMap<String, Message>();
		for (int i = 0; i < list1.size(); i++) {
			cleanMap.put(list1.get(i).getSenderNumber(), list1.get(i));
		}
		ArrayList<Message> list = new ArrayList<Message>(cleanMap.values());
		return list;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
