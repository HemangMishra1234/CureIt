package com.example.finflow.moodStatistics

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ThemeDAO {

    @Insert
    suspend fun insertEntity(entity: ThemeEntity): Long

    @Delete
    suspend fun deleteEntity(entity: ThemeEntity)

    @Update
    suspend fun updateEntity(entity: ThemeEntity)

    @Query("DELETE FROM ThemeTable")
    suspend fun deleteAllEntities()

    @Query("SELECT * FROM ThemeTable")
    fun getAllMoods() : LiveData<List<ThemeEntity>>

}