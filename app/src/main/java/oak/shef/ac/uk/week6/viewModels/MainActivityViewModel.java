package oak.shef.ac.uk.week6.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import oak.shef.ac.uk.week6.ImageElement;
import oak.shef.ac.uk.week6.database.PhotoData;

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
        int column_index_data, column_index_name,column_index_date,column_index_bucket_id;
        List<ImageElement> foundImages = new ArrayList<>();
        String absolutePathOfImage = null;
        String imageTitle, imageDate, bucket_id;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        allTheImages = getAllTheImages();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.BUCKET_ID};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplication().getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_name       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
        column_index_date       = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
        column_index_bucket_id  = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            imageTitle          = cursor.getString(column_index_name);
            imageDate           = cursor.getString(column_index_date);
            bucket_id           = cursor.getString(column_index_bucket_id);
            Log.e("Column", absolutePathOfImage);
            File imgFile = new File(absolutePathOfImage);

            foundImages.add(new ImageElement(imgFile, imageTitle, imageDate, bucket_id));
        }

        allTheImages.postValue(foundImages);
        return allTheImages;
    }
}
