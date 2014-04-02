package in.tapmobi.athome;

import in.tapmobi.athome.util.ImageCropperUtil;
import in.tapmobi.athome.util.ImageUtil;
import in.tapmobi.athome.util.RoundedImage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camera.library.CameraLibrary;
import com.camera.library.CameraOptions;

public class ProfileFragment extends Fragment {

	ImageView _image;
	CameraOptions options;
	public static Bitmap cropperImage;
	public static Drawable businessLogo;
	private static final int RE_GET_LOGO_CAMERA = 777;
	private static final int RE_GET_LOGO_GALLERY = 999;
	private static final int RE_GET_CROPPED_IMAGE = 666;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		_image = (ImageView) rootView.findViewById(R.id.accImage);
		_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeImageSelection();
			}
		});
		return rootView;
	}

	private void changeImageSelection() {

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

			}

			break;
		default:
			break;
		}
	}
}
