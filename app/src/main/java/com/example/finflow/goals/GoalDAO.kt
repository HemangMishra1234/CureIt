package com.example.finflow.goals

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDAO(goalEntity: GoalEntity): Long

    @Update
    suspend fun updateDebit(goalEntity: GoalEntity)


    @Delete
    suspend fun deleteDebit(goalEntity: GoalEntity)

    @Query("DELETE FROM GOALSSPLIT")
    suspend fun deleteAll()

    @Query("SELECT * FROM GoalsSplit ORDER BY rate DESC")
    fun getAllAppsInDB(): LiveData<List<GoalEntity>>

    @Query("UPDATE GoalsSplit SET presentTimeSpent = 0, presentPercent = 0.0, rate = 350.0")
    suspend fun resetGoals()
}