package com.example.finflow.moodStatistics

import androidx.lifecycle.LiveData
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity

class MoodRepository(val dao: MoodDAO) {

    val moods = dao.getAllMoods()
    val todayMoods = dao.getMoodByDate(
        Logic().currentDate())

    suspend fun insert(entity: MoodEntity): Long{
        return dao.insertEntity(entity)
    }

    suspend fun delete(entity: MoodEntity) {
        return dao.deleteEntity(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAllEntities()
    }

    suspend fun update(entity: MoodEntity){
        return dao.updateEntity(entity)
    }

}