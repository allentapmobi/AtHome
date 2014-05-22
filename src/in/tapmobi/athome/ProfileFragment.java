package in.tapmobi.athome;

import in.tapmobi.athome.models.UserProfile;
import in.tapmobi.athome.server.ServerAPI;
import in.tapmobi.athome.session.SessionManager;
import in.tapmobi.athome.sip.SipRegisteration;
import in.tapmobi.athome.util.ImageCropperUtil;
import in.tapmobi.athome.util.ImageUtil;
import in.tapmobi.athome.util.RoundedImage;
import in.tapmobi.athome.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.camera.library.CameraLibrary;
import com.camera.library.CameraOptions;

public class ProfileFragment extends Fragment {
	TextView tvRegNumber, tvRegisterationStatus, tvRegName, tvSubValidity;
	ImageView ivRegIcon;
	Button btnReferesh;
	Bitmap bitmapProfile;
	ToggleButton Activate;

	ImageView _image;
	CameraOptions options;
	public static Bitmap cropperImage;
	public static Drawable businessLogo;
	private static final int RE_GET_LOGO_CAMERA = 777;
	private static final int RE_GET_LOGO_GALLERY = 999;
	private static final int RE_GET_CROPPED_IMAGE = 666;

	public static boolean isprofileImageSet = false;

	SessionManager session;
	SipRegisteration sipReg;
	ProgressDialog pd;
	String ValidDate;
	String base64ProfileImage = null;

	public static boolean isActivated;

	Handler myHandler = new Handler();
	Runnable runnableUpdatePic, runnableUpdate;

	String userName;
	Boolean result;
	Date date;
	UserProfile user;

	@SuppressLint("SimpleDateFormat")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		session = new SessionManager(getActivity());
		ValidDate = session.getValidityDate();
		System.out.println(ValidDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		// sipReg = new SipRegisteration(getActivity().getApplicationContext(),getParent());
		sipReg = new SipRegisteration(getActivity().getApplicationContext(), getActivity());

		try {
			date = sdf.parse(ValidDate);
			ValidDate = sdf1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tvRegNumber = (TextView) rootView.findViewById(R.id.txtRegNumber);
		tvRegisterationStatus = (TextView) rootView.findViewById(R.id.txtRegisterationStatus);
		tvRegName = (TextView) rootView.findViewById(R.id.txtRegName);
		ivRegIcon = (ImageView) rootView.findViewById(R.id.regStatus);
		_image = (ImageView) rootView.findViewById(R.id.accImage);
		btnReferesh = (Button) rootView.findViewById(R.id.buttonReferesh);
		tvSubValidity = (TextView) rootView.findViewById(R.id.tvSubValidity);
		Activate = (ToggleButton) rootView.findViewById(R.id.tbActivate);
		_image.setImageBitmap(Utility.decodeBase64(session.getProfileImage()));
		// Get the toggle state from preferences
		isActivated = session.getToggleState();
		Activate.setChecked(isActivated);

		Activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {

					isActivated = true;
					session.createToggleState(isActivated);
					MainActivity.initSipManager();

				} else {
					sipReg.closeLocalProfile();
					isActivated = false;
					session.createToggleState(isActivated);
				}

			}
		});

		userName = session.getSipUserName();
		String regName = session.getName();
		tvRegNumber.setText(userName);
		tvRegName.setText(regName);
		tvSubValidity.setText("Your validity expires on:" + ValidDate);

		if (SipRegisteration.isRegisteredWithSip) {
			ivRegIcon.setImageResource(R.drawable.success);
		} else {
			ivRegIcon.setImageResource(R.drawable.failure);
		}
		if (SipRegisteration.sUpdateStatus != null) {
			tvRegisterationStatus.setText(SipRegisteration.sUpdateStatus);
		}

		btnReferesh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(getActivity(), ValidDate,
				// Toast.LENGTH_SHORT).show();
				Update();
			}
		});

		// Get image from shared preferences and set as profile image
		if (!isprofileImageSet) {
			setProfileImage();
		}

		_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeImageSelection();
			}
		});
		return rootView;
	}

	protected void Update() {

		pd = ProgressDialog.show(getActivity(), "", "Updating..", false, false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (session.getUserPhoneNumber() != null)
					user = ServerAPI.updateSubscription(session.getUserPhoneNumber());
				myHandler.post(runnableUpdate);
			}
		}).start();

		runnableUpdate = new Runnable() {

			@Override
			public void run() {
				if (user != null) {
					Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Updation failed.try Agian Later", Toast.LENGTH_SHORT).show();
				}
				pd.dismiss();
			}
		};
	}

	private void setProfileImage() {
		base64ProfileImage = session.getProfileImage();

		if (!base64ProfileImage.equals("") && base64ProfileImage != null && !base64ProfileImage.equals("null")) {
			try {
				bitmapProfile = Utility.decodeBase64(base64ProfileImage);
				if (bitmapProfile != null)
					bitmapProfile = Utility.getRoundedCornerImage(bitmapProfile);

				// businessLogo = new RoundedImage(cropperImage);
				// final float scale =
				// this.getResources().getDisplayMetrics().density;
				// int pixels = (int) (68 * scale + 0.5f);
				// _image.getLayoutParams().height = pixels;
				// _image.getLayoutParams().width = pixels;
				// _image.setImageDrawable(businessLogo);

				_image.setImageBitmap(bitmapProfile);
				isprofileImageSet = true;
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Base64 decoding failed..", Toast.LENGTH_LONG).show();
			}
		}

	}

	public void changeImageSelection() {

		CharSequence[] items = { "Camera", "Gallery" };
		ContextThemeWrapper cw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme);
		AlertDialog.Builder builder = new AlertDialog.Builder(cw);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == 0)
					getCameraPic();
				else
					getGalleryPic();
			}

			private void getGalleryPic() {
				Intent galIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(galIntent, RE_GET_LOGO_GALLERY);
			}

			private void getCameraPic() {
				options = CameraOptions.getInstance(getActivity());
				options.takePicture();
				options.setRequesCode(RE_GET_LOGO_CAMERA);

				Intent intent = new Intent(getActivity(), CameraLibrary.class);
				startActivityForResult(intent, RE_GET_LOGO_CAMERA);
			}
		});

		builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.getListView().setFastScrollEnabled(true);
		dialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case RE_GET_LOGO_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				Intent cropper = new Intent(getActivity(), ImageCropperUtil.class);
				cropperImage = ImageUtil.getScaledBitmap(options.getBitmapFile(), 960, 960);
				startActivityForResult(cropper, RE_GET_CROPPED_IMAGE);
				// Utility.SaveImage(cropperImage, "UserProfileImage");
			}

			break;

		case RE_GET_LOGO_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };
				Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap bitmap = ImageUtil.decodeScaledBitmapFromSdCard(filePath, 960, 960);

				Intent cropper = new Intent(getActivity(), ImageCropperUtil.class);
				// cropper.putExtra("ImageBitmap", finalBitmapLogo);
				cropperImage = bitmap;
				startActivityForResult(cropper, RE_GET_CROPPED_IMAGE);

				// Utility.SaveImage(cropperImage, "UserProfileImage");
				/*
				 * Bitmap Image = Utility.ResizeBitmap(cropperImage); base64ProfileImage = Utility.encodeTobase64(Image); int len =
				 * base64ProfileImage.length(); Log.d("Length of Base64", String.valueOf(len)); uploadImageToServer(base64ProfileImage);
				 * session.createProfileImage(base64ProfileImage);
				 */
			}

			break;

		case RE_GET_CROPPED_IMAGE:

			if (resultCode == Activity.RESULT_OK) {
				businessLogo = new RoundedImage(cropperImage);
				final float scale = this.getResources().getDisplayMetrics().density;
				int pixels = (int) (68 * scale + 0.5f);
				_image.getLayoutParams().height = pixels;
				_image.getLayoutParams().width = pixels;
				_image.setImageDrawable(businessLogo);
				Bitmap Image = Utility.ResizeBitmap(cropperImage);
				base64ProfileImage = Utility.encodeTobase64(Image);
				int len = base64ProfileImage.length();
				Log.d("Length of Base64", String.valueOf(len));
				uploadImageToServer(base64ProfileImage);
				session.createProfileImage(base64ProfileImage);
			}

			break;
		default:
			break;
		}

	}

	private void uploadImageToServer(String base64ProfileImage) {
		final String profilePicBase64 = base64ProfileImage;
		new Thread(new Runnable() {

			@Override
			public void run() {

				boolean success;
				try {
					success = ServerAPI.updateProfileImage(userName, profilePicBase64);
					bitmapProfile = Utility.decodeBase64(profilePicBase64);

					if (!success) {
						Toast.makeText(getActivity(), "Unable to upload image to server.Please try again.", Toast.LENGTH_SHORT).show();
					} else {
						myHandler.post(runnableUpdatePic);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		runnableUpdatePic = new Runnable() {

			@Override
			public void run() {

				_image.setImageBitmap(bitmapProfile);
				isprofileImageSet = true;
			}
		};

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setProfileImage();
	}

}
