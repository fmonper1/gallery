/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import uk.ac.shef.oak.com4510.database.MyDAO;
import uk.ac.shef.oak.com4510.database.MyRoomDatabase;
import uk.ac.shef.oak.com4510.database.PhotoData;

public class ImageRepository extends ViewModel {
    private final MyDAO mDBDao;
    private PhotoData photoDataLiveData;

    public ImageRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
//        photoDataLiveData = mDBDao.retrievePictureData("");
    }

    /**
     * this finds an entry in the DB using the path, if none is find it
     * creates a new entry in the DB
     */
    public LiveData<PhotoData> getPhotoData(String path) {
        Log.e("ImageRepository", "getPhotoData - Youre trying to invoke me papi");
        LiveData<PhotoData> foundItem = mDBDao.retrievePictureDataLiveData(path);

        return foundItem;
    }

    private static class retrievePictureDataAsyncTask extends AsyncTask<String, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private PhotoData foundPhotoData;

        retrievePictureDataAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(String... params) {
            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
            foundPhotoData = mAsyncTaskDao.retrievePictureData(params[0]);

            Log.i("ImageRepository", "image found: "+foundPhotoData.getPath()+"");
            // you may want to uncomment this to check if numbers have been inserted
            return null;
        }

    }

    public void updatePhotoData() {
        Log.e("ImageRepository", "Youre trying to invoke me papi");
    }

    /**
     * called by the UI to request the generation of a new random number
     */
    public void createNewPhotoData(String path, String title, String date, String lat, String lon) {
        new createAsyncTask(mDBDao).execute(new PhotoData(path, title, date, lat, lon));
    }

    private static class createAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private PhotoData photoDataLiveData;

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
