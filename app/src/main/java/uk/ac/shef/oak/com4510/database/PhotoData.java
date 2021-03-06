/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity()
public class PhotoData {


    public void setId(String id) {
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    private String id;
    private String path;
    private String title;
    private String description;
    private String dateTaken;
    private String latitude;
    private String longitude;
    private Double latitudeDouble;
    private Double longitudeDouble;

    public PhotoData(String path, String title, String date, String latitude, String longitude) {
        this.id = path;
        this.path = path;
        this.title = title;
        Date fileData = new Date(Long.parseLong(date)*1000);
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(fileData);
        this.dateTaken = formattedDate;
        this.latitude = latitude;
        if(latitude != null){
            this.latitudeDouble = Double.parseDouble(latitude);
            this.longitudeDouble = Double.parseDouble(longitude);
        }
        this.longitude = longitude;
        this.description = "Please add a description";
    }

    public PhotoData() {
        this.description = "Please add a description";
    }

    public String getTitle() {
        return title;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NonNull
    public String getId() {
        return id;
    }


    public Double getLatitudeDouble() {
        return latitudeDouble;
    }

    public void setLatitudeDouble(Double latitudeDouble) {
        this.latitudeDouble = latitudeDouble;
    }

    public Double getLongitudeDouble() {
        return longitudeDouble;
    }

    public void setLongitudeDouble(Double longitudeDouble) {
        this.longitudeDouble = longitudeDouble;
    }
}
