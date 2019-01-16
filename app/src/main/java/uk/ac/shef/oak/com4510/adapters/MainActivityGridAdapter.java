/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.pojo.ImageElement;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;
import uk.ac.shef.oak.com4510.ui.MainActivity;
import uk.ac.shef.oak.com4510.ui.SinglePictureActivity;


public class MainActivityGridAdapter extends RecyclerView.Adapter<MainActivityGridAdapter.View_Holder> {
    static private Context context;
    private static List<ImageElement> items;

    public MainActivityGridAdapter(List<ImageElement> items) {
        this.items = items;
    }

    public MainActivityGridAdapter(Context cont, List<ImageElement> items) {
        super();
        this.items = items;
        context = cont;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context= parent.getContext();
        return holder;
    }

    /**
     * takes the view holder and image position to set
     * the image to be displayed in the image view.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        // Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {
            if (items.get(position).getImage()!=-1) {
                holder.imageView.setImageResource(items.get(position).getImage());
            } else if (items.get(position).getFile()!=null){
                final Bitmap bitmap = getBitmapFromMemCache(String.valueOf(items.get(position)));
                if (bitmap != null) {
                    holder.imageView.setImageBitmap(bitmap);
                }
                else {
                    new UploadSingleImageTask().execute(new HolderAndPosition(position, holder));
                }
            }
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, SinglePictureActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("path", items.get(position).getFile().getAbsolutePath());
                context.startActivity(intent);
            });
        }
        //animate(holder);
    }


    // convenience method for getting data at click position
    ImageElement getItem(int id) {
        return items.get(id);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageTitle;

        View_Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
//            imageTitle = (TextView) itemView.findViewById(R.id.image_title);
        }
    }

    /**
     * takes the bitmap options and the required width and height to return
     * the correct samplesize used to display each image
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * takes the image absolute path and the required dimensions to return
     * a decoded bitmap image
     * @param imagePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(String imagePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    private class UploadSingleImageTask extends AsyncTask<HolderAndPosition, Void,
            Bitmap> {
        HolderAndPosition holdAndPos;
        @Override
        protected Bitmap doInBackground(HolderAndPosition... holderAndPosition) {
            holdAndPos= holderAndPosition[0];
            Bitmap myBitmap =
                    decodeSampledBitmapFromResource(items.get(holdAndPos.position).getFile().getAbsolutePath(), 100 , 100);
            addBitmapToMemoryCache(String.valueOf(items.get(holdAndPos.position)), myBitmap);
            return myBitmap;
        }
        @Override
        protected void onPostExecute (Bitmap bitmap){
            holdAndPos.holder.imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * adds the decoded bitmap into the applications cache
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            PhotoRepository.mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * gets the specified bitmap from the cache using the key
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return PhotoRepository.mMemoryCache.get(key);
    }
    private class HolderAndPosition {
        int position;
        View_Holder holder;

        public HolderAndPosition(int position, View_Holder holder) {
            this.position = position;
            this.holder = holder;
        }
    }

    public static List<ImageElement> getItems() {
        return items;
    }

    public static void setItems(List<ImageElement> items) {
        MainActivityGridAdapter.items = items;
    }
}