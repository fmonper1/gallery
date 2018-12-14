/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import oak.shef.ac.uk.week6.database.PhotoData;

public class SinglePictureViewModel extends AndroidViewModel {

    private MyRepository mRepository;
//     The LiveData for the database details of the
    private MutableLiveData<PhotoData> photoDataLiveData;
    private MutableLiveData<ImageElement> theImage;

    public SinglePictureViewModel(Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
//        photoDataLiveData = mRepository.getPhotoData("");
    }


    /**
     * getter for the live data
     * @return
     */
    public LiveData<PhotoData> getImageDetails() {
        if (photoDataLiveData == null) {
            photoDataLiveData = new MutableLiveData<>();
        }
        return photoDataLiveData;
    }

    public MutableLiveData<ImageElement> getImageDetails(Bundle b) {
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
                    theImage.setValue(new ImageElement(element.file, element.title, element.date, element.bucket_id));
                }
            }
        }
        return theImage;
    }

    /**
     * request by the UI to generate a new random number
     */
//    public void generateNewNumber() {
//        mRepository.generateNewNumber();
//    }
}
