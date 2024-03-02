package com.example.finflow.habits

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("HABITS")
data class HabitsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var rateIncrement: Float,
    var rateDecrement: Float,
    var fIncrement: Long,
    var fDecrement: Long,
    var appName: String
)
