package com.example.finflow.goals

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finflow.room.DebitApp
import com.example.finflow.room.DebitAppDAO

@Database(entities = [GoalEntity::class], version = 1)
abstract class GoalDatabase : RoomDatabase(){

    abstract val goalDAO: GoalDAO

    // Singleton Design Pattern
    companion object{
        @Volatile
        private var INSTANCE: GoalDatabase ?= null
        fun getInstance(context: Context): GoalDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    // Creating the database object
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GoalDatabase::class.java,
                        "GoalDatabase"
                    )
                        .build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }

}