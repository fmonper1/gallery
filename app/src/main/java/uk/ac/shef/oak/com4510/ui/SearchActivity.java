package uk.ac.shef.oak.com4510.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.adapters.SearchResultsAdapter;
import uk.ac.shef.oak.com4510.databinding.ActivitySearchBinding;
import uk.ac.shef.oak.com4510.pojo.FormData;
import uk.ac.shef.oak.com4510.viewModels.SearchViewModel;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel model;
    private Activity context;
    private FormData formData;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConstraintLayout form_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);

        context = this;

        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        model = ViewModelProviders.of(this).get(SearchViewModel.class);

        formData = model.getFormData();
        binding.setFormData(formData);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        // specify an adapter (see also next example)
        mAdapter = new SearchResultsAdapter(this);
//        ((SearchResultsAdapter) mAdapter).setResults(foundItems);
        mRecyclerView.setAdapter(mAdapter);

        form_container = findViewById(R.id.form_container);
        FloatingActionButton fabGallery = findViewById(R.id.fab_search);
        fabGallery.setOnClickListener(view -> {
            if (form_container.getVisibility()==View.VISIBLE && mAdapter.getItemCount()>0){
                form_container.setVisibility(View.GONE);
            } else {
                form_container.setVisibility(View.VISIBLE);
            }
        });

    }

    public void submitSearchForm(View view) {
        Log.d("Activity", "submitSearchForm was called!");
        if(model.isDataNull()) {

        } else {
            model.submitFormData();
            model.getAllImages();
            model.getAllImages().observe(this, foundItems -> {
                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

                Log.e("search results", foundItems.toString());
//                    mAdapter.setResults(foundItems);
                ((SearchResultsAdapter) mAdapter).setResults(foundItems);
                if (form_container.getVisibility()==View.VISIBLE && !foundItems.isEmpty()) {
                    form_container.setVisibility(View.GONE);
                }
            });
//            Intent intent = new Intent(this, SearchResultsActivity.class);
////            intent.putExtra("queryParams", FormData formData);
//            context.startActivity(intent);ç
        }
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

}
