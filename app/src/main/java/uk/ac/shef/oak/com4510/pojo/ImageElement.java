/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.pojo;

import android.graphics.Bitmap;

import java.io.File;
import android.graphics.Bitmap;

import java.io.File;
public class ImageElement {
    public int image=-1;
    File file=null;
    String title;
    String date;
    String bucket_id;
    Bitmap bitmap;
    String latitude;
    String longitude;

    public ImageElement(int image) {
        this.image = image;
    }

    public ImageElement(File fileX) {
        file= fileX;
    }
    public ImageElement(File fileX, String title, String date, String bucket_id, String latitude, String longitude) {
        file= fileX;
        this.title = title;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
