/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.com4510.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PhotoDAO {
    @Insert
    void insertAll(PhotoData... photoData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhotoData photoData);

    @Delete
    void delete(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM photodata WHERE path = :path LIMIT 1")
    LiveData<PhotoData> retrievePictureDataLiveData(String path);

    @Query("SELECT * FROM photodata WHERE title LIKE :title ")
    LiveData<List<PhotoData>> findImagesByTitle(String title);

    @Query("SELECT * FROM photodata WHERE description LIKE :description ")
    LiveData<List<PhotoData>> findImagesByDescription(String description);

    @Query("SELECT * FROM photodata WHERE title LIKE :title AND description LIKE :description")
    LiveData<List<PhotoData>> findImagesByTitleAndDescription(String title, String description);

    @Query("SELECT * FROM photodata WHERE latitude")

    @Update
    void updatePhotoData(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM photodata WHERE path = :path LIMIT 1")
    PhotoData retrievePictureData(String path);

    @Delete
    void deleteAll(PhotoData... photoData);

    @Query("SELECT COUNT(*) FROM photodata")
    int howManyElements();
}
