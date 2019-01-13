/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import uk.ac.shef.oak.com4510.database.PhotoDAO;
import uk.ac.shef.oak.com4510.database.MyRoomDatabase;
import uk.ac.shef.oak.com4510.database.PhotoData;

class MyRepository extends ViewModel {
    private final PhotoDAO mDBDao;
    private PhotoData photoDataLiveData;

    public MyRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
        photoDataLiveData = mDBDao.retrievePictureData("");
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return
     */
    public PhotoData getPhotoData(String bucketID) {
        return mDBDao.retrievePictureData(bucketID);
    }

//    /**
//     * called by the UI to request the generation of a new random number
//     */
//    public void generateNewNumber() {
//        Random r = new Random();
//        int i1 = r.nextInt(10000 - 1) + 1;
//        new insertAsyncTask(mDBDao).execute(new NumberData(i1));
//    }

//    private static class getPhotoDataAsyncTask extends AsyncTask<PhotoData, Void, Void> {
//        private PhotoDAO mAsyncTaskDao;
//        private LiveData<PhotoData> photoDataLiveData;
//
////        getPhotoDataAsyncTask(PhotoDAO dao) {
////            mAsyncTaskDao = dao;
////        }
//        @Override
//        protected Void doInBackground(PhotoData... params) {
//            // THIS HAS TO TAKE PASS IN THE BUCKET_ID FOR THE PICTURE
//            mAsyncTaskDao.retrievePictureData(params[0]);
//
//            Log.i("MyRepository", "number generated: "+params[0].getNumber()+"");
//            // you may want to uncomment this to check if numbers have been inserted
//            //            int ix=mAsyncTaskDao.howManyElements();
//            //            Log.i("TAG", ix+"");
//            return null;
//        }
//    }
}
