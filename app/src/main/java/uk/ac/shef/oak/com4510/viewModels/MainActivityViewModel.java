package uk.ac.shef.oak.com4510.viewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.shef.oak.com4510.ImageElement;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;

public class MainActivityViewModel extends AndroidViewModel {
    private PhotoRepository mRepository;
    private MutableLiveData<List<ImageElement>> allTheImages;

    public MainActivityViewModel(Application application) {
        super(application);
//        photoDataLiveData = mRepository.getPhotoData("");
        mRepository = new PhotoRepository(application);

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
        allTheImages.postValue(mRepository.getImagesFromStorage());
        return allTheImages;
    }

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() {
        // the public picture director
        File picturesDirectory = new File( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        // timestamp makes unique name.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        // put together the directory and the timestamp to make a unique image location.
        File imageFile = new File(picturesDirectory, "IMG_" + timestamp + ".jpg");
        return imageFile;
    }
}
