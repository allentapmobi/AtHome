package in.tapmobi.athome.models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * 
 * @author Allen Moses
 */
public class ContactsModel {

	private String name;
	private String sortKey;
	private String number;
	private Bitmap contactPhoto;
	private Uri contactPhotoUri;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public Bitmap getContactPhoto() {
		return contactPhoto;
	}

	public void setContactPhoto(Bitmap contactPhoto) {
		this.contactPhoto = contactPhoto;
	}

	public Uri getContactPhotoUri() {
		return contactPhotoUri;
	}

	public void setContactPhotoUri(Uri contactPhotoUri) {
		this.contactPhotoUri = contactPhotoUri;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
