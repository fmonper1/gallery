/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 *
 * some inspiration taken from https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */

package uk.ac.shef.oak.com4510;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.View;
import android.widget.Toast;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.shef.oak.com4510.ui.EditPictureDetailsActivity;
import uk.ac.shef.oak.com4510.ui.SearchActivity;
import uk.ac.shef.oak.com4510.viewModels.MainActivityViewModel;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final int ACCESS_FINE_LOCATION = 123;
    private static final int ACCESS_COARSE_LOCATION = 456;
    private static final String TAG = "MainActivity";
    private List<ImageElement> myPictureList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainActivityViewModel viewModel;

    private LocationManager locationManager;
    private LocationListener listener;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    Location location;
    double latitude; // Latitude
    double longitude;

    public static LruCache<String, Bitmap> mMemoryCache;

    private Activity activity;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 4 * maxMemory / 10;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        activity= this;

        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        // set up the RecyclerView
        int numberOfColumns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter= new MainActivityGridAdapter(myPictureList);
        mRecyclerView.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            viewModel.getImages().observe(this, (allTheImages) -> {
                myPictureList = allTheImages;
//            Log.e("images", String.valueOf(allTheImages));
                // TODO: this isnt done like this... probably...
                mAdapter= new MainActivityGridAdapter(myPictureList);
                mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
            });
        }

        else {
            // let's request permission.
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, REQUEST_READ_EXTERNAL_STORAGE);
        }

//        startLocationUpdates();
        //getImages();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("cameraaa", "cameraaa");
                    invokeCamera();

                } else {
                    // let's request permission.
                    String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });

        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_COARSE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startLocationUpdates();
    }


    private Location mCurrentLocation;
    private String mLastUpdateTime;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.e("Location", mCurrentLocation.toString());
        }
    };


    File photoFile = null;
    private void invokeCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                startLocationUpdates();
                photoFile = createImageFile();
                displayMessage(getBaseContext(),photoFile.getAbsolutePath());

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "uk.ac.shef.oak.com4510.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(photoFile)));
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }
        } else {
            displayMessage(getBaseContext(),"Nullll");
        }

    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private File createImageFile() {
        // the public picture director
        File picturesDirectory = new File( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        // timestamp makes unique name.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        // put together the directory and the timestamp to make a unique image location.
        File imageFile = new File(picturesDirectory, "IMG_" + timestamp + ".jpg");
        return imageFile;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                            mLocationCallback, null /* Looper */);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getImages().observe(this, (allTheImages) -> {
                        myPictureList = allTheImages;
//            Log.e("images", String.valueOf(allTheImages));
                        // TODO: this isnt done like this... probably...
                        mAdapter= new MainActivityGridAdapter(myPictureList);
                        mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
                    });

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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: displaying");
            long time = System.currentTimeMillis();
            Log.d("currentTimeMs", String.valueOf(time));
            ImageElement newImg = new ImageElement(photoFile.getAbsoluteFile(), photoFile.getName(), String.valueOf(time), photoFile.getAbsolutePath(), null, null );
            Intent intent = new Intent(activity, SinglePictureActivity.class);
            intent.putExtra("position", 0);
            activity.startActivity(intent);
            myPictureList.add(0,newImg);
            mAdapter.notifyDataSetChanged();
//            mRecyclerView.scrollToPosition(0);
            Log.d(TAG, "onActivityResult: done");
        }
    }


    /**
     * add to the grid
     * @param returnedPhotos
     */
    private void onPhotosReturned(List<File> returnedPhotos) {
        myPictureList.addAll(getImageElements(returnedPhotos));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);
    }


    /**
     * given a list of photos, it creates a list of myElements
     * @param returnedPhotos
     * @return
     */
    private List<ImageElement> getImageElements(List<File> returnedPhotos) {
        List<ImageElement> imageElementList= new ArrayList<>();
        for (File file: returnedPhotos){
            ImageElement element= new ImageElement(file);
            imageElementList.add(element);
        }
        return imageElementList;
    }

    public Activity getActivity() {
        return activity;
    }



}
