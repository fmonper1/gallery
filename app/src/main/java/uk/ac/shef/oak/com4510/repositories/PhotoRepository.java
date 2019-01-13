/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import uk.ac.shef.oak.com4510.database.PhotoDAO;
import uk.ac.shef.oak.com4510.database.MyRoomDatabase;
import uk.ac.shef.oak.com4510.database.PhotoData;

public class PhotoRepository extends ViewModel {
    private final PhotoDAO mDBDao;
    private PhotoData photoDataLiveData;

    public PhotoRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
//        photoDataLiveData = mDBDao.retrievePictureData("");
    }

    /**
     * this finds an entry in the DB using the path, if none is found it
     * creates a new entry in the DB
     */
    public LiveData<PhotoData> getPhotoData(String path) {
        Log.e("PhotoRepository", "getPhotoData - Youre trying to invoke me papi");
        LiveData<PhotoData> foundItem = mDBDao.retrievePictureDataLiveData(path);

        return foundItem;
    }

    private static class retrievePictureDataAsyncTask extends AsyncTask<String, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private PhotoData foundPhotoData;

        retrievePictureDataAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(String... params) {
            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
            foundPhotoData = mAsyncTaskDao.retrievePictureData(params[0]);

            Log.i("PhotoRepository", "image found: "+foundPhotoData.getPath()+"");
            // you may want to uncomment this to check if numbers have been inserted
            return null;
        }

    }

    public void updatePhotoData(PhotoData photoData) {
        Log.e("Repo - updatePhotoData", "Youre trying to invoke me papi");
//        mDBDao.updatePhotoData(photoData);
        new updatePhotoDataAsyncTask(mDBDao).execute(photoData);

    }

    private static class updatePhotoDataAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private PhotoData photoDataLiveData;

        updatePhotoDataAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(PhotoData... photoData) {
            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
            mAsyncTaskDao.updatePhotoData(photoData[0]);

            Log.i("PhotoRepository", "image updated: "+photoData[0].getPath()+"");
            // you may want to uncomment this to check if numbers have been inserted
            int ix=mAsyncTaskDao.howManyElements();
            Log.i("TAG", ix+"");
            return null;
        }
    }

    /**
     * called by the UI to request the generation of a new random number
     */
    public void createNewPhotoData(String path, String title, String date, String lat, String lon) {
        new createAsyncTask(mDBDao).execute(new PhotoData(path, title, date, lat, lon));
    }

    private static class createAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private PhotoData photoDataLiveData;

        createAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(PhotoData... params) {
            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
            mAsyncTaskDao.insert(params[0]);

            Log.i("PhotoRepository", "image created: "+params[0].getPath()+"");
            // you may want to uncomment this to check if numbers have been inserted
                        int ix=mAsyncTaskDao.howManyElements();
                        Log.i("TAG", ix+"");
            return null;
        }
    }

    public LiveData<List<PhotoData>> findImagesByTitle(String title) {
        return mDBDao.findImagesByTitle(title);
    }

    public LiveData<List<PhotoData>> findImagesByDescription(String description) {
        return mDBDao.findImagesByDescription(description);
    }

    public LiveData<List<PhotoData>> findImagesByTitleAndDescription(String title, String description) {
        return mDBDao.findImagesByTitleAndDescription(title, description);
    }

    public LiveData<List<PhotoData>> findGeoLocatedImages(){
        return mDBDao.findGeoLocatedImages();
    }

//    private static class findByTitleAsync extends AsyncTask<PhotoData, Void, Void> {
//        private PhotoDAO mAsyncTaskDao;
//        private PhotoData photoDataLiveData;
//
//        createAsyncTask(PhotoDAO dao) {
//            mAsyncTaskDao = dao;
//        }
//        @Override
//        protected Void doInBackground(PhotoData... params) {
//            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
//            mAsyncTaskDao.insert(params[0]);
//
//            Log.i("PhotoRepository", "image created: "+params[0].getPath()+"");
//            // you may want to uncomment this to check if numbers have been inserted
//            int ix=mAsyncTaskDao.howManyElements();
//            Log.i("TAG", ix+"");
//            return null;
//        }
    }
