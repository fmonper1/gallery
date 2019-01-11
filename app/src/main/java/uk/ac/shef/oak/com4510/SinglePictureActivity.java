

package uk.ac.shef.oak.com4510;



import android.app.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import uk.ac.shef.oak.com4510.databinding.ShowPictureAndDataBinding;
import uk.ac.shef.oak.com4510.ui.EditPictureDetailsActivity;


/*
 * THIS IS A VIEW in MVVM!!
 */

public class SinglePictureActivity extends AppCompatActivity {

    private ImageElement theImage;
    private Toolbar toolbar;
    private String imagePath;
    private SinglePictureViewModel model;
    private Bundle b;
    static private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture_and_data);

        setUpToolbar();

        context = this;
        // Setup Data Binding in the XML file
        ShowPictureAndDataBinding binding = DataBindingUtil.setContentView(this, R.layout.show_picture_and_data);
        // Read extras from Intent from other activity
        b = getIntent().getExtras();

        model = ViewModelProviders.of(this).get(SinglePictureViewModel.class);

        //  Observe LiveData for the selected image
        model.getImageDetailsDAO(b).observe(this, foundItem -> {
            // if database is empty
            if (foundItem==null) {
                Log.e("ImageRepository", "The liveData for the image is Null, attempting to create entry");
                model.createNewEntry(b);
            } else {
                // bind the returned object to the variables in the template
                binding.setPhotoData(foundItem);
                // decode the bitmap from the path and set it to and element in the view
                Bitmap myBitmap = BitmapFactory.decodeFile(foundItem.getPath());
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(myBitmap);

                // Set path to a variable so that it can be passed to another activity in an intent

                // Some logging for stuff :)
                Log.e("ImageRepository", "Image was found in the database");
                Log.e("ImageRepository - Path", foundItem.getPath());
                Log.e("ImageRepository - Desc", foundItem.getDescription());
                if (foundItem.getLatitude()!= null &&  foundItem.getLongitude()!= null) {
                    Log.e("ImageRepository - Lat", foundItem.getLatitude());
                    Log.e("ImageRepository - Lon", foundItem.getLongitude());
                }
            }
        });

        LinearLayout details_container = findViewById(R.id.details_container);
        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_search);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (details_container.getVisibility()==View.VISIBLE ){
                    details_container.setVisibility(View.INVISIBLE);
                } else {
                    details_container.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_picture, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    /*
     * Switch for options in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_details:
                Log.d("MenuItem", "Go edit details");
                editImageDetails();
                break;
            case R.id.view_exif_data:
                Log.d("MenuItem", "Go exif details");
                break;
            case android.R.id.home:
                // Make the up navigation acta like pressing back button
                this.onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
     * Called when the user taps the Send button on the UI
     */
    public void editImageDetails() {
        Intent intent = new Intent(this, EditPictureDetailsActivity.class);
        model.getImageDetails().observe(this, foundItem -> {
            assert foundItem != null;
            imagePath = foundItem.getPath();
            Log.d("ImageRe", imagePath);
        });
        intent.putExtra("ImagePath", imagePath);
        context.startActivity(intent);
    }

}
