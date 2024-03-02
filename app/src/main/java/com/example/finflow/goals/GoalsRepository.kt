package com.example.finflow.goals

import android.util.Log


class GoalsRepository(val dao: GoalDAO) {

    var allGoals = dao.getAllAppsInDB()

    suspend fun insert(entity: GoalEntity): Long{
        return dao.insertDAO(entity)
    }

    suspend fun delete(entity: GoalEntity){
        return dao.deleteDebit(entity)
    }

    suspend fun update(entity: GoalEntity){
        return dao.updateDebit(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAll()
    }

    suspend fun resetGoals(){
        Log.e("GoalsRepository", "Resetting goals by reaching")
        try {
        dao.resetGoals()
    } catch (e: Exception) {
        Log.e("GoalsRepository", "Error resetting goals", e)
    }

        allGoals = dao.getAllAppsInDB()
    }
}