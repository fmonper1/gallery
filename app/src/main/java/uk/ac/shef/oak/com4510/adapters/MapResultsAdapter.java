package uk.ac.shef.oak.com4510.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.ui.SinglePictureActivity;
import uk.ac.shef.oak.com4510.database.PhotoData;

public class MapResultsAdapter extends RecyclerView.Adapter<MapResultsAdapter.MyViewHolder> {
    private List<PhotoData> mDataset;
    private final LayoutInflater mInflater;
    static private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public MyViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.image_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MapResultsAdapter(Context theContext) {
        context = theContext;
        this.mDataset = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }
    public void setDataSet(List<PhotoData> dataSet) {
        mDataset = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MapResultsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_image_map, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * takes the view holder and image position to set
     * the image to be displayed in the image view.
     * @param holder
     * @param position
     */
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder!=null && mDataset.get(position)!=null) {

            final int THUMBSIZE = 128;
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(mDataset.get(position).getPath()),
                    THUMBSIZE,
                    THUMBSIZE);
            holder.mImageView.setImageBitmap(thumbImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SinglePictureActivity.class);
                    // This is set to -1 to differentiate this intent from the one launched from MainActivity´s MyAdapter
                    intent.putExtra("position", -1);
                    intent.putExtra("path", mDataset.get(position).getPath());
                    Log.e("imagePath searchAdapter", mDataset.get(position).getPath());
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * sets the results obtained from the search
     * @param results
     */
    public void setResults(List<PhotoData> results) {
        mDataset = results;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
