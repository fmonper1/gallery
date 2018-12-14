/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ImageElement {
    int image=-1;
    File file=null;
    String title;
    String date;
    String bucket_id;
    Bitmap bitmap;

    public ImageElement(int image) {
        this.image = image;
    }

    public ImageElement(File fileX) {
        file= fileX;
    }
    public ImageElement(File fileX, String title, String date, String bucket_id) {
        file= fileX;
        this.title = title;
        this.date = date;
        this.bucket_id = bucket_id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(String bucket_id) {
        this.bucket_id = bucket_id;
    }

}
