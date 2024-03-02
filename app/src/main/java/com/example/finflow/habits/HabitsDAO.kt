package com.example.finflow.habits

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finflow.transactionHistory.TransEntity

@Dao
interface HabitsDAO {

    @Insert
    suspend fun insertEntity(entity: HabitsEntity): Long

    @Delete
    suspend fun deleteEntity(entity: HabitsEntity)

    @Update
    suspend fun updateEntity(entity: HabitsEntity)

    @Query("DELETE FROM HABITS")
    suspend fun deleteAllHabits()

    @Query("SELECT * FROM HABITS ORDER BY (fIncrement+fDecrement) DESC")
    fun getAllHabits() :LiveData<List<HabitsEntity>>
}