/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import uk.ac.shef.oak.com4510.databinding.ShowPictureAndDataBinding;

/*
 * THIS IS A VIEW in MVVM!!
 */

public class SinglePictureActivity extends AppCompatActivity {

    private ImageElement theImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture_and_data);

        // Setup Data Binding in the XML file
        ShowPictureAndDataBinding binding = DataBindingUtil.setContentView(this, R.layout.show_picture_and_data);
        // Read extras from Intent from other activity
        Bundle b = getIntent().getExtras();

        SinglePictureViewModel model = ViewModelProviders.of(this).get(SinglePictureViewModel.class);

//        model.getImageDetails(b).observe(this, theImage -> {
//            // TODO: Data binding is not working for this, will look into it later...
//            Bitmap myBitmap = BitmapFactory.decodeFile(theImage.file.getAbsolutePath());
//            ImageView imageView = (ImageView) findViewById(R.id.image);
//            imageView.setImageBitmap(myBitmap);
////            Log.e("IMAGENAME", theImage.title);
////            Log.e("IMAGENAMEPATH", theImage.file.getAbsolutePath());
//        });

        model.getImageDetailsDAO(b).observe(this, foundItem -> {
            // if database is empty
            if (foundItem==null) {
                Log.e("ImageRepository", "The liveData for the image is Null, attempting to create entry");
                model.createNewEntry(b);
            } else {
                binding.setPhotoData(foundItem);
                // decode the bitmap from the path and set it to and element in the view
                Bitmap myBitmap = BitmapFactory.decodeFile(foundItem.getPath());
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(myBitmap);
                Log.e("ImageRepository", "Image was found in the database");
                Log.e("ImageRepository - Path", foundItem.getPath());
                Log.e("ImageRepository - Desc", foundItem.getDescription());
                if (foundItem.getLatitude()!= null &&  foundItem.getLongitude()!= null) {
                    Log.e("ImageRepository - Lat", foundItem.getLatitude());
                    Log.e("ImageRepository - Lon", foundItem.getLongitude());
                }
            }
        });
        // gets and intent from another activity and used the data to find the image
    }

}
