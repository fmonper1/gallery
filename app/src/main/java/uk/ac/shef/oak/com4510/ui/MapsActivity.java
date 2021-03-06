package uk.ac.shef.oak.com4510.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.adapters.MapResultsAdapter;
import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.viewModels.MapsViewModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

    private GoogleMap mMap;
    private MapsViewModel model;
    private Activity context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        model = ViewModelProviders.of(this).get(MapsViewModel.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.search_results_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // specify an adapter (see also next example)
        mAdapter = new MapResultsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraIdleListener(this);

        // set custom info window to display the picture details
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v =  inflater.inflate(R.layout.info_window_layout, null);

                TextView title = v.findViewById(R.id.info_window_title);
                title.setText(marker.getTitle());
                TextView date = v.findViewById(R.id.info_window_date);
                date.setText(marker.getSnippet());

                return v;
            }
        });
        //
        model.getGeoLocatedImages().observe(this, foundItems -> {
            Log.e("search map results", foundItems.toString());
            if (foundItems.isEmpty()) {
                Toast.makeText(this, "No images found :(", Toast.LENGTH_SHORT).show();
            }
            for (PhotoData temp : foundItems) {

                mMap.addMarker(new MarkerOptions().position(new LatLng(temp.getLatitudeDouble(),temp.getLongitudeDouble())).title(temp.getTitle())
                        .snippet("Date Taken: " + temp.getDateTaken() + "\n" + "Description: " + temp.getDescription()));

            }
        });
    }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        model.getImagesInsideBounds(bounds).observe(this, foundItems -> {
            Log.e("search map results", foundItems.toString());
            if (foundItems.isEmpty()) {
                Log.d("failll", "failll");
            }
            Log.e("search results", foundItems.toString());
            ((MapResultsAdapter) mAdapter).setResults(foundItems);

            for (PhotoData temp : foundItems) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(temp.getLatitudeDouble(),temp.getLongitudeDouble())).title(temp.getTitle())
                        .snippet("Date Taken: " + temp.getDateTaken() + "\n" + "Description: " + temp.getDescription()));

            }
        });
    }


    /*
     * Switch for options in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Make the up navigation acta like pressing back button
                this.onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        mMap.clear();

    }
}
