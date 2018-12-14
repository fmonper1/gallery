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
    public String getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(String bucket_id) {
        this.bucket_id = bucket_id;
    }

    @PrimaryKey
    @NonNull
    private String bucket_id; // comes from MediaStore.Images.ImageColumns.BUCKET_ID
    private String title;
    private String description;
    private String dateTaken;
    private Float latitude;
    private Float longitude;

    public PhotoData(String id) {
        this.bucket_id = id;
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

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

        public void setId(String id) {
        this.bucket_id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
