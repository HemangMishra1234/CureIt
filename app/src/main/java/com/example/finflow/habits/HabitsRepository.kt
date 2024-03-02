package com.example.finflow.habits

import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDAO

class HabitsRepository(val dao: HabitsDAO) {

    val allHabits = dao.getAllHabits()

    suspend fun insert(entity: HabitsEntity): Long {
        return dao.insertEntity(entity)
    }

    suspend fun delete(entity: HabitsEntity) {
        return dao.deleteEntity(entity)
    }

    suspend fun deleteAll() {
        return dao.deleteAllHabits()
    }

    suspend fun update(entity: HabitsEntity) {
        return dao.updateEntity(entity)
    }

}