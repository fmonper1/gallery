package uk.ac.shef.oak.com4510.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.ImageElement;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<ImageElement>> allTheImages;

    public MainActivityViewModel(Application application) {
        super(application);
//        photoDataLiveData = mRepository.getPhotoData("");
        // Eventually this will be called from a repo? maybe? idk...
        allTheImages = getAllTheImages();
    }

    /**
     * getter for the live data
     * @return
     */
    public MutableLiveData<List<ImageElement>> getAllTheImages() {
        if (allTheImages == null) {
            allTheImages = new MutableLiveData<List<ImageElement>>();
        }
        return allTheImages;
    }

    public MutableLiveData<List<ImageElement>> getImages() {
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_name,column_index_date,column_index_bucket_id,column_index_latitude,column_index_longitude;
        List<ImageElement> foundImages = new ArrayList<>();
        String absolutePathOfImage = null;
        String imageTitle, imageDate, bucket_id, latitude, longitude;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        allTheImages = getAllTheImages();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.BUCKET_ID, MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplication().getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_name       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
        column_index_date       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
        column_index_bucket_id  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID);
        column_index_latitude  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LATITUDE);
        column_index_longitude  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LONGITUDE);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            imageTitle          = cursor.getString(column_index_name);
            imageDate           = cursor.getString(column_index_date);
            bucket_id           = cursor.getString(column_index_bucket_id);
            latitude            = cursor.getString(column_index_latitude);
            longitude           = cursor.getString(column_index_longitude);
            File imgFile = new File(absolutePathOfImage);

            foundImages.add(new ImageElement(imgFile, imageTitle, imageDate, bucket_id, latitude, longitude));
        }

        allTheImages.postValue(foundImages);
        return allTheImages;
    }
}
