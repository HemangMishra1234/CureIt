package com.example.finflow.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DebitAppDAO {
    @Insert
    suspend fun insert(debit: DebitApp): Long

    @Update
    suspend fun updateDebit(debit: DebitApp)

    @Delete
    suspend fun deleteDebit(debit: DebitApp)

    @Query("DELETE FROM DebitApps")
    suspend fun deleteAll()

    @Query("SELECT * FROM DebitApps ORDER BY freq DESC")
    fun getAllAppsInDB(): LiveData<List<DebitApp>>
}