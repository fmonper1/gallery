/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.pojo.ImageElement;
import uk.ac.shef.oak.com4510.database.PhotoDAO;
import uk.ac.shef.oak.com4510.database.MyRoomDatabase;
import uk.ac.shef.oak.com4510.database.PhotoData;


public class PhotoRepository extends ViewModel {
    private final PhotoDAO mDBDao;
    private PhotoData photoDataLiveData;
    private MutableLiveData<List<ImageElement>> allTheImages;
    private Application context;

    public PhotoRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
        context = application;
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

    public LiveData<List<PhotoData>> findImagesInsideBounds(LatLngBounds latLngBounds) {
        Double a = latLngBounds.southwest.latitude;
        Double b = latLngBounds.southwest.longitude;
        Double c = latLngBounds.northeast.latitude;
        Double d = latLngBounds.northeast.longitude;
        return mDBDao.findImagesInsideBounds(a, b, c, d);
    }

    public List<ImageElement> getImagesFromStorage() {
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_name,column_index_date,column_index_bucket_id,column_index_latitude,column_index_longitude;
        List<ImageElement> foundImages = new ArrayList<>();
        String absolutePathOfImage = null;
        String imageTitle, imageDate, bucket_id, latitude, longitude;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        allTheImages = new MutableLiveData<List<ImageElement>>();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.BUCKET_ID, MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

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
        cursor.close();

//        allTheImages.postValue(foundImages);

        return foundImages;
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
