package com.example.finflow.moodStatistics

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoodEntity::class], version = 1)
abstract class MoodDatabase: RoomDatabase() {
    abstract val moodDaoObject: MoodDAO

    //Singleton Design Pattern
    companion object{
        @Volatile
        private var INSTANCE: MoodDatabase ?= null
        fun getInstance(context: Context): MoodDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null)
                    //Create the Database
                    instance = Room.databaseBuilder(context.applicationContext,
                        MoodDatabase::class.java,"MoodDB").build()

                INSTANCE = instance
                return instance
            }

        }
    }
}