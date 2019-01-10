package uk.ac.shef.oak.com4510.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.adapters.SearchResultsAdapter;
import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.pojo.FormData;
import uk.ac.shef.oak.com4510.viewModels.SearchViewModel;

public class SearchResultsActivity extends AppCompatActivity {

    private SearchViewModel model;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        final SearchResultsAdapter mAdapter = new SearchResultsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        model = ViewModelProviders.of(this).get(SearchViewModel.class);
        FormData formData = model.getFormData();
        Log.e("search results", formData.getTitle());

//        model.getAllImages().observe(this, new Observer<List<PhotoData>>() {
//            @Override
//            public void onChanged(@Nullable final List<PhotoData> foundItems) {
//                // Update the cached copy of the words in the adapter.
////                adapter.setWords(words);
////                Log.e("search results", foundItems.toString());
//                mAdapter.setResults(foundItems);
//            }
//        });
//        model.getAllImages().observe(this, foundItem -> {
//            Log.e("search results", foundItem.toString());
//        });

//        FormData formData = model.getFormData();
    }
}
