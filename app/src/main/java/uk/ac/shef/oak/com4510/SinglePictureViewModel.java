/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;

import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.repositories.ImageRepository;

public class SinglePictureViewModel extends AndroidViewModel {

    private ImageRepository mRepository;
//     The LiveData for the database details of the image
    private LiveData<PhotoData> photoDataLiveData;
    private MutableLiveData<ImageElement> theImage;

    public SinglePictureViewModel(Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new ImageRepository(application);
//        photoDataLiveData = mRepository.getPhotoData("");
    }


    /**
     * getter for the live data
     * @return
     */
    public LiveData<PhotoData> getImageDetails() {
        if (photoDataLiveData == null) {
            photoDataLiveData = new MutableLiveData<PhotoData>();
        }
        return photoDataLiveData;
    }

    /**
     * request by the UI to inspect a single image
     */
//    public MutableLiveData<ImageElement> getImageDetails(Bundle b) {
//        int position = -1;
//        if (theImage == null) {
//            theImage = new MutableLiveData<>();
//        }
//        if (b != null) {
//            position = b.getInt("position");
//            if (position != -1) {
//                ImageElement element = MyAdapter.getItems().get(position);
//                if (element.image != -1) {
//                    // This is for stuff in res/drawables, we dont need it
//                    // imageView.setImageResource(element.image);
//                } else if (element.file != null) {
//                    // Create an ImageElement for data-binding in the view
//                    theImage.setValue(new ImageElement(element.file, element.title, element.date, element.bucket_id));
//                    mRepository.getPhotoData(element.file.getAbsolutePath());
//                    photoDataLiveData = mRepository.getPhotoData(element.file.getAbsolutePath());
//                }
//            }
//        }
//        return theImage;
//    }

    /**
     * request by the UI to inspect a single image
     */
    public LiveData<PhotoData> getImageDetailsDAO(Bundle b) {
        int position = -1;
        if (theImage == null) {
            theImage = new MutableLiveData<>();
        }
        if (b != null) {
            position = b.getInt("position");
            if (position != -1) {
                ImageElement element = MyAdapter.getItems().get(position);
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

    /**
     * request by the UI to generate a new entry for an image in the DB
     */
    public void createNewEntry(Bundle b) {
        int position = b.getInt("position");
        ImageElement element = MyAdapter.getItems().get(position);

        mRepository.createNewPhotoData(element.file.getAbsolutePath(), element.title, element.date, element.latitude, element.longitude);
    }
}
