/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity()
public class PhotoData {


    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String dateTaken;
    private String latitude;
    private String longitude;
    private String path;

    public PhotoData(String path, String title, String date, String latitude, String longitude) {
        this.path = path;
        this.title = title;
        this.dateTaken = date;
        this.latitude = latitude;
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
    public int getId() {
        return id;
    }
}
