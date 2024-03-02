package com.example.finflow.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("GOALSSPLIT")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var domain: String,
    var expectedPercent: Float,
    var presentPercent: Float,
    var presentTimeSpent: Long,
    var rate: Float
)
