/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 *
 * some inspiration taken from https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */

package uk.ac.shef.oak.com4510.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.adapters.MainActivityGridAdapter;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.pojo.ImageElement;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;
import uk.ac.shef.oak.com4510.viewModels.MainActivityViewModel;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final int TAG_ACCESS_FINE_LOCATION = 123;
    private static final String TAG = "MainActivity";
    private List<ImageElement> myPictureList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainActivityViewModel viewModel;

    private LocationManager locationManager;

    private Activity activity;

    // number of columns in recyclerview's grid
    private int numberOfColumns = 4;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity= this;

        PhotoRepository.cache();

        // set up the RecyclerView
        mRecyclerView = findViewById(R.id.grid_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter= new MainActivityGridAdapter(myPictureList);
        mRecyclerView.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadAndDisplayImages();
        } else {
            // let's request permission.
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, REQUEST_READ_EXTERNAL_STORAGE);
        }

        startLocationUpdates();
        /*
         * Setup the FloatingActionButtons
         */
        FloatingActionButton fab = findViewById(R.id.fab_camera);
        fab.setOnClickListener(view -> {
            startLocationUpdates();
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("cameraaa", "cameraaa");
                invokeCamera();
            } else {
                // let's request permission.
                String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });

        FloatingActionButton fabMap = findViewById(R.id.fab_map);
        fabMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }

    /*
     * Setup the action bar to add a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Switch for options in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Log.d("MenuItem", "Go to search");
                openSearchActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Called from the UI when the user taps the Search button on action bar
     */
    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        activity.startActivity(intent);
    }

    Location bestLocation = null;
    @SuppressLint("NewApi")
    private void startLocationUpdates() {
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // Create location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            // check the various providers to see which one contains the last known location
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                Log.d(TAG,"last known location, provider:" + provider + "location: "+ l);

                if (l == null) {
                    continue;
                }
                if (bestLocation == null
                        || l.getAccuracy() < bestLocation.getAccuracy()) {
                    Log.d(TAG,"found best last known location:" +  l);
                    bestLocation = l;

                }
            }
            // if no last location has been found return null
            if (bestLocation == null) {
                Log.d(TAG, "startLocationUpdates: nullll");
            }
        }
        else {
            String[] permissionRequest = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissionRequest, TAG_ACCESS_FINE_LOCATION);
        }
    }


    File photoFile = null;
    private void invokeCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "uk.ac.shef.oak.com4510.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }
        } else {
            displayMessage(getBaseContext(),"Couldn't resolve intent");
        }

    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private File createImageFile() {
        return viewModel.createImageFile();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TAG_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay!
                    startLocationUpdates();
                } else {
                    // permission denied, boo!
                    Toast.makeText(getApplicationContext(),
                        "Application will not add location to the pictures without location services!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAndDisplayImages();
                }
                return;
            }

            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    invokeCamera();
                }
                return;
            }

        }
    }

    private void loadAndDisplayImages() {
        viewModel.getImages().observe(this, (allTheImages) -> {
            myPictureList = allTheImages;
            mAdapter= new MainActivityGridAdapter(myPictureList);
            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: displaying");
            // Divide here by 1000 cos were multiplying by that in the PhotoData to convert the epoch date
            long time = System.currentTimeMillis()/1000;
            Log.d("currentTimeMs", String.valueOf(time));
            ImageElement newImg;
            // if location is not null then save the lat and long values
            if (bestLocation != null) {
                 newImg = new ImageElement(photoFile.getAbsoluteFile(), photoFile.getName(), String.valueOf(time), photoFile.getAbsolutePath(), String.valueOf(bestLocation.getLatitude()), String.valueOf(bestLocation.getLongitude()));
            }
            else {
                 newImg = new ImageElement(photoFile.getAbsoluteFile(), photoFile.getName(), String.valueOf(time), photoFile.getAbsolutePath(), null, null);
            }
            Intent intent = new Intent(activity, SinglePictureActivity.class);
            intent.putExtra("position", 0);
            activity.startActivity(intent);
            myPictureList.add(0,newImg);
            mAdapter.notifyDataSetChanged();
//            mRecyclerView.scrollToPosition(0);
            Log.d(TAG, "onActivityResult: done");
        }
    }


    public Activity getActivity() {
        return activity;
    }

}
