package com.example.finflow.moodStatistics

class ThemeRepository(val dao: ThemeDAO) {

    val themes = dao.getAllMoods()

    suspend fun insert(entity: ThemeEntity): Long{
        return dao.insertEntity(entity)
    }

    suspend fun delete(entity: ThemeEntity) {
        return dao.deleteEntity(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAllEntities()
    }

    suspend fun update(entity: ThemeEntity){
        return dao.updateEntity(entity)
    }
}