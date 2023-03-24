package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NasaDatabaseDao {

    // Insert to the database all the asteroid data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    // Recover from the database all the asteroid data
    @Query("SELECT * FROM asteroid ORDER BY close_approach_date DESC")
    fun selectByDate() : List<DatabaseAsteroid>
}