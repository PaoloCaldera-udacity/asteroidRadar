package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NasaDatabaseDao {

    // Insert to the database all the asteroid data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: DatabaseAsteroid)

    // Recover from the database all the asteroid data
    @Query("SELECT * FROM asteroid WHERE DATE(close_approach_date) >= DATE() ORDER BY close_approach_date DESC")
    fun selectByDate() : LiveData<List<DatabaseAsteroid>>
}