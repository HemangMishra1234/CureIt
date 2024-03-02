package com.example.finflow.transactionHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransEntity::class], version = 1)
abstract class TransactionDatabase: RoomDatabase() {

    abstract val transDAOObject: TransactionDAO

    //Singleton Design pattern
    companion object{
        @Volatile
        private var INSTANCE : TransactionDatabase ?= null
        fun getInstance(context: Context): TransactionDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    //Create the Database
                    instance = Room.databaseBuilder(context.applicationContext,
                        TransactionDatabase::class.java, "TransactionDB").build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}

/**synchronized(this): This block is synchronized to ensure thread safety during the creation of the
database instance. Multiple threads might attempt to access the getInstance method simultaneously,
and synchronization helps prevent race conditions.

var instance = INSTANCE: This line retrieves the current value of the INSTANCE variable. The
INSTANCE variable holds the reference to the singleton instance of the DebitAppDB database.

if (instance == null) { ... }: This condition checks whether an instance of the database already
exists. If it doesn't, the block within the condition is executed to create a new instance.

instance = Room.databaseBuilder(...).build(): This line creates a new instance of the DebitAppDB
using Room's databaseBuilder method. It specifies the application context, the database
class (DebitAppDB::class.java), and the name of the database ("debitApp_db"). The build
method is then called to finalize the creation of the database.

INSTANCE = instance: After creating or retrieving the instance, this line sets the
INSTANCE variable to the newly created instance. This ensures that subsequent calls
to getInstance will return the existing instance.

return instance: The method returns the created or existing instance of the DebitAppDB
database.

The purpose of using the Singleton pattern and synchronization in this context is
to ensure that only one instance of the database is created, promoting reusability and preventing
unnecessary overhead associated with creating multiple database instances. The synchronized block
ensures that the creation process is thread-safe.*/