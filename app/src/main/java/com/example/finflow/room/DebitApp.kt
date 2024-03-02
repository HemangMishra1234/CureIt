package com.example.finflow.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DebitApps")
data class DebitApp(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="App id")
    var id:Int,
    var name: String?,
    @ColumnInfo(name = "Description")
    var desc: String?,
    var rate: Float?,
    var freq: Int?
)
