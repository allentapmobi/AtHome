package in.tapmobi.athome.util;

import in.tapmobi.athome.ProfileFragment;
import in.tapmobi.athome.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.edmodo.cropper.CropImageView;

public class ImageCropperUtil extends SherlockActivity {

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	Bitmap originalImage;
	CropImageView cropImageView;
	ImageView ivPreview;

	// Saves the state upon rotating the screen/restarting the activity
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
	}

	// Restores the state upon rotating the screen/restarting the activity
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_cropper);

		// originalImage = (Bitmap)
		// getIntent().getParcelableExtra("BitmapImage");
		if (ProfileFragment.cropperImage != null)
			originalImage = ProfileFragment.cropperImage;

		ivPreview = (ImageView) findViewById(R.id.ivPreview);

		// Initialize components of the app
		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		cropImageView.setGuidelines(1);
		cropImageView.setFixedAspectRatio(true);
		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
		cropImageView.setImageBitmap(originalImage);

	}

	@Override
	public void onResume() {
		super.onResume();
		// Set title
		//getSupportActionBar().setTitle("Cropper");
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.cropper, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; finish activity to go home
			onBackPressed();
			break;
		case R.id.menu_rotate:
			// Rotate
			cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
			break;
		case R.id.menu_crop:
			// Crop
			// DashBoardActivity.cropperImage = cropImageView.getCroppedImage();
			// Intent intent = new Intent();
			// setResult(RESULT_OK, intent);
			// ImageCropperUtil.this.finish();
			ivPreview.setImageDrawable(new RoundedImage(cropImageView.getCroppedImage()));

			break;
		// case R.id.menu_done:
		// // Crop
		// DashBoardActivity.cropperImage = cropImageView.getCroppedImage();
		// Intent intent = new Intent();
		// setResult(RESULT_OK, intent);
		// ImageCropperUtil.this.finish();
		// break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (cropImageView.getCroppedImage() != null) {
			ProfileFragment.cropperImage = ImageUtil.getScaledBitmap(cropImageView.getCroppedImage(), 250, 250);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
		}
		super.onBackPressed();
		//overridePendingTransition(R.anim.fade_out_main, R.anim.close_next);
	}

}
