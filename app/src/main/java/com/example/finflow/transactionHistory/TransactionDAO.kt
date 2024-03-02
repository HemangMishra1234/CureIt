package com.example.finflow.transactionHistory

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insertEntity(entity: TransEntity): Long

    @Delete
    suspend fun deleteEntity(entity: TransEntity)

    @Update
    suspend fun updateEntity(entity: TransEntity)

    @Query("DELETE FROM TransactionHistory")
    suspend fun deleteAllTransactions()

    @Query("SELECT * FROM TransactionHistory ORDER BY dateTime DESC")
    fun getAllTransactions() :LiveData<List<TransEntity>>

    @Query("SELECT * FROM TransactionHistory WHERE dateTime BETWEEN :start AND :end ORDER BY dateTime DESC")
    fun searchTransaction(start: String, end: String): LiveData<List<TransEntity>>

}