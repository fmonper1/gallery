package uk.ac.shef.oak.com4510.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.viewModels.SinglePictureViewModel;
import uk.ac.shef.oak.com4510.databinding.ActivityEditPictureDetailsBinding;

public class EditPictureDetailsActivity extends AppCompatActivity {

    private static String ImagePath;
    private Bundle extras;
    ActivityEditPictureDetailsBinding binding;
    SinglePictureViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture_details);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Get the Intent that started this activity and extract the string
        extras = getIntent().getExtras();
        String imagePath = extras.getString("ImagePath");
        ActivityEditPictureDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_picture_details);

        model = ViewModelProviders.of(this).get(SinglePictureViewModel.class);
        model.getImageDetails(imagePath).observe(this, foundItem -> {
            binding.setPhotoData(foundItem);
            Bitmap myBitmap = BitmapFactory.decodeFile(foundItem.getPath());
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(myBitmap);

            // Some logging for stuff :)
            Log.e("PhotoRepository", "Image was found in the database");
            Log.e("PhotoRepository - Path", foundItem.getPath());
            Log.e("PhotoRepository - Desc", foundItem.getDescription());
            if (foundItem.getLatitude()!= null &&  foundItem.getLongitude()!= null) {
                Log.e("PhotoRepository - Lat", foundItem.getLatitude());
                Log.e("PhotoRepository - Lon", foundItem.getLongitude());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // Make the up navigation acta like pressing back button
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called by the UI when the Submit button is clicked
     * Call fn in the ViewModel and closes the activity by pressing the back button
     */
    public void submitEditImageForm(View button) {
        Log.d("Activity", "submitEditImageForm was called!");
        model.submitFormData();
        super.onBackPressed();
    }

}
