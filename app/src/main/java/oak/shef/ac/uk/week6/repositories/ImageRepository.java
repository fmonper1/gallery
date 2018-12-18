/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import oak.shef.ac.uk.week6.database.MyDAO;
import oak.shef.ac.uk.week6.database.MyRoomDatabase;
import oak.shef.ac.uk.week6.database.PhotoData;

public class ImageRepository extends ViewModel {
    private final MyDAO mDBDao;
    private LiveData<PhotoData> photoDataLiveData;

    public ImageRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
//        photoDataLiveData = mDBDao.retrievePictureData("");
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return
     */
    public LiveData<PhotoData> getPhotoData(String path) {
        Log.e("IMAGENAMELOG", "Youre trying to invoke me papi");
        LiveData<PhotoData> foundItem = mDBDao.retrievePictureData(path);

        // IF THE ELEMENT RETURNED BY THE DAO IS NULL,
        // WE CREATE A NEW RECORD USING THE GIVEN PATH
        if (foundItem.getValue() == null) {
            Log.e("IMAGENAMELOG", "Youre NULL papito");
            createNewPhotoData(path);
        } else {
            Log.e("IMAGENAMELOG", String.valueOf(foundItem));
        }
        return foundItem;
    }

    public void updatePhotoData() {
        Log.e("updatePhotoData", "Youre trying to invoke me papi");
    }

    /**
     * called by the UI to request the generation of a new random number
     */
    public void createNewPhotoData(String path) {
        new createAsyncTask(mDBDao).execute(new PhotoData(path));
    }

    private static class createAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private LiveData<PhotoData> photoDataLiveData;

        createAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(PhotoData... params) {
            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
            mAsyncTaskDao.insert(params[0]);

            Log.i("ImageRepository", "image created: "+params[0].getPath()+"");
            // you may want to uncomment this to check if numbers have been inserted
                        int ix=mAsyncTaskDao.howManyElements();
                        Log.i("TAG", ix+"");
            return null;
        }
    }
}
