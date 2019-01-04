package oak.shef.ac.uk.week6.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import io.reactivex.Single;
import oak.shef.ac.uk.week6.R;
import oak.shef.ac.uk.week6.SinglePictureActivity;
import oak.shef.ac.uk.week6.SinglePictureViewModel;
import oak.shef.ac.uk.week6.databinding.ActivityEditPictureDetailsBinding;
import oak.shef.ac.uk.week6.databinding.ShowPictureAndDataBinding;

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
        setSupportActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the Intent that started this activity and extract the string
//        Intent intent = getIntent();
        extras = getIntent().getExtras();
        String imagePath = extras.getString("ImagePath");
        Log.d("ImageRe - Intent", imagePath);
        ActivityEditPictureDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_picture_details);

        model = ViewModelProviders.of(this).get(SinglePictureViewModel.class);

        model.getImageDetails(imagePath).observe(this, foundItem -> {
            binding.setPhotoData(foundItem);
            Bitmap myBitmap = BitmapFactory.decodeFile(foundItem.getPath());
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(myBitmap);

            // Some logging for stuff :)
            Log.e("ImageRepository", "Image was found in the database");
            Log.e("ImageRepository - Path", foundItem.getPath());
            Log.e("ImageRepository - Desc", foundItem.getDescription());
            if (foundItem.getLatitude()!= null &&  foundItem.getLongitude()!= null) {
                Log.e("ImageRepository - Lat", foundItem.getLatitude());
                Log.e("ImageRepository - Lon", foundItem.getLongitude());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
