package in.tapmobi.athome.userprofile;

import in.tapmobi.athome.R;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	public static TextView txtSipStatus;

	private static Runnable runnable;
	private static Handler mHandler;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		mHandler = new Handler();
		txtSipStatus = (TextView) rootView.findViewById(R.id.txtUpdateStatus);

		return rootView;
	}

	/**
	 * Updates the status box at the top of the UI with a messege of your
	 * choice.
	 * 
	 * @param status
	 *            The String to display in the status box.
	 */
	public static void updateStatus(String status) {
		final String st = status;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				try {
					txtSipStatus.setText(st);
				} catch (NullPointerException ne) {
					ne.printStackTrace();
				}
			}
		});
		new Thread(runnable).start();
	};
}
