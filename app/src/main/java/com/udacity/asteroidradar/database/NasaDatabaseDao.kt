package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NasaDatabaseDao {

    // Recover from the database today's asteroids
    @Query("SELECT * FROM asteroid WHERE DATE(close_approach_date) BETWEEN DATE('now','+1 day') AND DATE('now','+7 days') ORDER BY DATE(close_approach_date) ASC")
    fun selectNextWeek(): LiveData<List<DatabaseAsteroid>>

    // Recover from the database today's asteroids
    @Query("SELECT * FROM asteroid WHERE DATE(close_approach_date) == DATE()")
    fun selectToday(): LiveData<List<DatabaseAsteroid>>

    // Recover from the database all the asteroid data (today and next seven days)
    @Query("SELECT * FROM asteroid WHERE DATE(close_approach_date) >= DATE() ORDER BY DATE(close_approach_date) ASC")
    fun selectAll(): LiveData<List<DatabaseAsteroid>>

    // Insert to the database all the asteroid data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    // Delete from the database all the asteroids before today
    @Query("DELETE FROM asteroid WHERE DATE(close_approach_date) < DATE()")
    fun deleteOld()
}