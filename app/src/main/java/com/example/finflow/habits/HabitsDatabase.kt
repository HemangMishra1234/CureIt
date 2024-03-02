package com.example.finflow.habits


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDAO

@Database(entities = [HabitsEntity::class], version = 1)
abstract class HabitsDatabase: RoomDatabase() {

    abstract val habitsDAOObject: HabitsDAO

    //Singleton Design pattern
    companion object{
        @Volatile
        private var INSTANCE : HabitsDatabase ?= null
        fun getInstance(context: Context): HabitsDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    //Create the Database
                    instance = Room.databaseBuilder(context.applicationContext, HabitsDatabase::class.java, "HabitsDB").build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}