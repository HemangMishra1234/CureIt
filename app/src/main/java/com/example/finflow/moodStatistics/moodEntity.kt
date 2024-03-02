package com.example.finflow.moodStatistics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("MoodStatsTable")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var date: String,
    var type: String,
    var count : Int,
    var average: Int
)
