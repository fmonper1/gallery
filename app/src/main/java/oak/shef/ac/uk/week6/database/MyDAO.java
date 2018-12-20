/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.week6.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface MyDAO {
    @Insert
    void insertAll(PhotoData... photoData);

    @Insert
    void insert(PhotoData photoData);

    @Delete
    void delete(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM photodata WHERE path = :path LIMIT 1")
    LiveData<PhotoData> retrievePictureDataLiveData(String path);

    // it selects a random element
    @Query("SELECT * FROM photodata WHERE path = :path LIMIT 1")
    PhotoData retrievePictureData(String path);

    @Delete
    void deleteAll(PhotoData... photoData);

    @Query("SELECT COUNT(*) FROM photodata")
    int howManyElements();
}
