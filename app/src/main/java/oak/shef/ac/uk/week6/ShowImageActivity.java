/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import oak.shef.ac.uk.week6.ImageElement;
import oak.shef.ac.uk.week6.MyAdapter;
import oak.shef.ac.uk.week6.R;
import oak.shef.ac.uk.week6.databinding.ShowPictureAndDataBinding;

/*
 * THIS IS A VIEW in MVVM!!
 */

public class ShowImageActivity extends AppCompatActivity {

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
        model.getImageDetails(b).observe(this, theImage -> {
            // update UI
            // Bind the ImageElement to the variable in the view
            binding.setImageElement(theImage);

            // TODO: Data binding is not working for this, will look into it later...
            Bitmap myBitmap = BitmapFactory.decodeFile(theImage.file.getAbsolutePath());
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(myBitmap);

            Log.e("IMAGENAME", theImage.title);
            Log.e("IMAGENAME", theImage.bucket_id);
        });


        // gets and intent from another activity and used the data to find the image



    }

}
