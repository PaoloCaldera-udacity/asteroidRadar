package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NasaDatabaseDao {

    // Recover from the database all the asteroid data
    @Query("SELECT * FROM asteroid")
    fun selectByDate() : LiveData<List<DatabaseAsteroid>>

    // Insert to the database all the asteroid data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}