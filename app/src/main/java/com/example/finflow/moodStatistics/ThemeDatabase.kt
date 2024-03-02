package com.example.finflow.moodStatistics

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ThemeEntity::class], version = 1)
abstract class ThemeDatabase: RoomDatabase() {
    abstract val themeDaoObject: ThemeDAO

    //Singleton Design Pattern
    companion object{
        @Volatile
        private var INSTANCE: ThemeDatabase ?= null
        fun getInstance(context: Context): ThemeDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null)
                //Create the Database
                    instance = Room.databaseBuilder(context.applicationContext,
                        ThemeDatabase::class.java,"ThemeDB").build()

                INSTANCE = instance
                return instance
            }

        }
    }
}