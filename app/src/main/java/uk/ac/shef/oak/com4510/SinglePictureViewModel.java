/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.util.Log;

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
     * getter for the live data
     * @return - photoDataLiveData
     */
    public LiveData<PhotoData> getImageDetails(String path) {
        Log.d("ImageRe ViewM", "getImageDetails() calling the repo...");
        photoDataLiveData = mRepository.getPhotoData(path);

        return photoDataLiveData;
    }

    /**
     * request by the UI to inspect a single image
     */
    public LiveData<PhotoData> getImageDetailsDAO(Bundle b) {
        int position = -1;
        if (theImage == null) {
            theImage = new MutableLiveData<>();
        }
        // TODO: this has to find an image using the path and not the position so it can be called from
        // TODO: the SearchViewModel
        if (b != null) {
            position = b.getInt("position");
            String path = b.getString("path");

            // Position will be -1 when the activity is launched from the SearchResults
            if (position == -1) {
                photoDataLiveData = mRepository.getPhotoData(path);
            } else {
                ImageElement element = MainActivityGridAdapter.getItems().get(position);
                if (element.image != -1) {
                    // This is for stuff in res/drawables, we dont need it
                    // imageView.setImageResource(element.image);
                } else if (element.file != null) {
                    // Create an ImageElement for data-binding in the view
                    photoDataLiveData = mRepository.getPhotoData(element.file.getAbsolutePath());
                }
            }
        }
        return photoDataLiveData;
    }

    public LiveData<PhotoData> getLocalPhotoData() {
        return photoDataLiveData;
    }

    /**
     * request by the UI to generate a new entry for an image in the DB
     */
    public void createNewEntry(Bundle b) {
        int position = b.getInt("position");
        ImageElement element = MainActivityGridAdapter.getItems().get(position);

        mRepository.createNewPhotoData(element.file.getAbsolutePath(), element.title, element.date, element.latitude, element.longitude);
    }

    /**
     * request by the UI to generate a new entry for an image in the DB
     */
    public LiveData<PhotoData> createNewEntryAndReturn(Bundle b) {
        int position = b.getInt("position");
        ImageElement element = MainActivityGridAdapter.getItems().get(position);

        mRepository.createNewPhotoData(element.file.getAbsolutePath(), element.title, element.date, element.latitude, element.longitude);

        photoDataLiveData = mRepository.getPhotoData(element.file.getAbsolutePath());
        return photoDataLiveData;
    }

    public void submitFormData() {
        String nTitle, nDescription;
        Log.d("submitFormData",photoDataLiveData.getValue().getTitle());
        mRepository.updatePhotoData(photoDataLiveData.getValue());
    }
}
