package in.tapmobi.athome.adapter;

import in.tapmobi.athome.R;
import in.tapmobi.athome.models.Message;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {
	private TextView messages;
	private List<Message> msg = new ArrayList<Message>();
	LinearLayout wrapper;

	public MessageAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public void add(Message object) {
		msg.add(object);
		super.add(object);
	}

	@Override
	public int getCount() {
		return this.msg.size();
	}

	@Override
	public Message getItem(int position) {
		// TODO Auto-generated method stub
		return this.msg.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflator.inflate(R.layout.msg_list_item, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		Message msg = getItem(position);
		messages = (TextView) row.findViewById(R.id.comment);
		
		messages.setText(msg.comment);
		
		messages.setBackgroundResource(msg.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(msg.left ? Gravity.LEFT : Gravity.RIGHT);
		
		return row;
		
		
		
	}
	
	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}
