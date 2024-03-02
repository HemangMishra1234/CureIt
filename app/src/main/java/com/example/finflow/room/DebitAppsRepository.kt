package com.example.finflow.room

class DebitAppsRepository(val dao:DebitAppDAO) {

    val allDebitApps = dao.getAllAppsInDB()

    suspend fun insert(entity: DebitApp): Long{
        return dao.insert(entity)
    }

    suspend fun delete(entity: DebitApp){
        return dao.deleteDebit(entity)
    }

    suspend fun update(entity: DebitApp){
        return dao.updateDebit(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAll()
    }
}