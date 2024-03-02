package com.example.finflow.transactionHistory

import androidx.lifecycle.LiveData

class TransactionRepository(val dao: TransactionDAO) {

    val allTransactions = dao.getAllTransactions()

    suspend fun insert(entity: TransEntity):Long{
        return dao.insertEntity(entity)
    }

    suspend fun delete(entity: TransEntity) {
        return dao.deleteEntity(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAllTransactions()
    }

    suspend fun update(entity: TransEntity){
        return dao.updateEntity(entity)
    }

    fun searchTransaction(start: String, end: String) : LiveData<List<TransEntity>> {
        return dao.searchTransaction(start, end)
    }

}