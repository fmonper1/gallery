///*
// * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
// */
//
//package oak.shef.ac.uk.week6;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.Observer;
//import android.arch.lifecycle.ViewModelProviders;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import oak.shef.ac.uk.week6.database.PhotoData;
//
//public class SinglePictureView extends AppCompatActivity {
//    LiveData<PhotoData> photoDataLiveData;
//    private SinglePictureViewModel singlePictureViewModel;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_single_picture);
//
//        // Get a new or existing ViewModel from the ViewModelProvider.
//        singlePictureViewModel = ViewModelProviders.of(this).get(SinglePictureViewModel.class);
//        // Add an observer on the LiveData. The onChanged() method fires
//        // when the observed data changes and the activity is
//        // in the foreground.
//        singlePictureViewModel.getNumberDataToDisplay().observe(this, new Observer<NumberData>(){
//            @Override
//            public void onChanged(@Nullable final NumberData newValue) {
//                TextView tv= findViewById(R.id.textView);
//                // if database is empty
//                if (newValue==null)
//                    tv.setText("click button");
//                else
//                    tv.setText(newValue.getNumber()+"");
//            }});
//
//
//        // it generates a request to generate a new random number
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                singlePictureViewModel.generateNewNumber();
//            }
//        });
//
//    }
//}
//
