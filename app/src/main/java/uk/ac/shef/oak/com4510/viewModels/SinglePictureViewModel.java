/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.util.Log;

import uk.ac.shef.oak.com4510.pojo.ImageElement;
import uk.ac.shef.oak.com4510.adapters.MainActivityGridAdapter;
import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;

public class SinglePictureViewModel extends AndroidViewModel {

    private PhotoRepository mRepository;
//     The LiveData for the database details of the image
    private LiveData<PhotoData> photoDataLiveData;
    private MutableLiveData<ImageElement> theImage;

    public SinglePictureViewModel(Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new PhotoRepository(application);
//        photoDataLiveData = mRepository.getPhotoData("");
    }


    /**
     * getter for the live data
     * @return - photoDataLiveData
     */
    public LiveData<PhotoData> getImageDetails() {
        if (photoDataLiveData == null) {
            photoDataLiveData = new MutableLiveData<PhotoData>();
        }
        return photoDataLiveData;
    }

    /**
     * gets the details for an image from the repository given an image path
     * @param path the path of the desired image
     * @return - photoDataLiveData
     */
    public LiveData<PhotoData> getImageDetails(String path) {
        Log.d("ImageRe ViewM", "getImageDetails() calling the repo...");
        photoDataLiveData = mRepository.getPhotoData(path);

        return photoDataLiveData;
    }

    /**
     * Passes the bundle from the MainActivity's adapter with keys position and path.
     * If the value of position is -1 the path is used to query the database, else the position is
     * used to retrieve an ImageElement from the gridAdapter and we get the path from that object
     * @param b bundle that should contain a Int position and String path
     * @return the live data for the specified image
     */
    public LiveData<PhotoData> getImageDetailsDAO(Bundle b) {
        int position = -1;
        if (theImage == null) {
            theImage = new MutableLiveData<>();
        }
        if (b != null) {
            position = b.getInt("position");
            String path = b.getString("path");

            // Position will be -1 when the activity is launched from the SearchResults
            if (position == -1) {
                photoDataLiveData = mRepository.getPhotoData(path);
            } else {
                ImageElement element = MainActivityGridAdapter.getItems().get(position);
                if (element.getFile() != null) {
                    // Create an ImageElement for data-binding in the view
                    photoDataLiveData = mRepository.getPhotoData(element.getFile().getAbsolutePath());
                }
            }
        }
        return photoDataLiveData;
    }

    /**
     * getter for the LiveData
     * @return the liveData stored in the viewmodel
     */
    public LiveData<PhotoData> getLocalPhotoData() {
        return photoDataLiveData;
    }

    /**
     * request by the UI to generate a new entry for an image in the DB
     */
    public void createNewEntry(Bundle b) {
        int position = b.getInt("position");
        ImageElement element = MainActivityGridAdapter.getItems().get(position);

        mRepository.createNewPhotoData(element.getFile().getAbsolutePath(), element.getTitle(), element.getDate(), element.getLatitude(), element.getLongitude());
    }

    /**
     * request by the UI to generate a new entry for an image in the DB
     */
    public LiveData<PhotoData> createNewEntryAndReturn(Bundle b) {
        int position = b.getInt("position");
        ImageElement element = MainActivityGridAdapter.getItems().get(position);

        mRepository.createNewPhotoData(element.getFile().getAbsolutePath(), element.getTitle(), element.getDate(), element.getLatitude(), element.getLongitude());

        photoDataLiveData = mRepository.getPhotoData(element.getFile().getAbsolutePath());
        return photoDataLiveData;
    }

    public void submitFormData() {
        String nTitle, nDescription;
        Log.d("submitFormData",photoDataLiveData.getValue().getTitle());
        mRepository.updatePhotoData(photoDataLiveData.getValue());
    }
}
