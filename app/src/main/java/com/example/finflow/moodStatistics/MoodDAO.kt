package com.example.finflow.moodStatistics

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoodDAO {

    @Insert
    suspend fun insertEntity(entity: MoodEntity): Long

    @Delete
    suspend fun deleteEntity(entity: MoodEntity)

    @Update
    suspend fun updateEntity(entity: MoodEntity)

    @Query("DELETE FROM MoodStatsTable")
    suspend fun deleteAllEntities()

    @Query("SELECT * FROM MoodStatsTable ORDER BY date DESC")
    fun getAllMoods() :LiveData<List<MoodEntity>>

    @Query("SELECT * FROM MoodStatsTable WHERE date = :dateVal")
    fun getMoodByDate(dateVal: String): LiveData<MoodEntity>
}