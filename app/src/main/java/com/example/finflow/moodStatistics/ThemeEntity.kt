package com.example.finflow.moodStatistics

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("ThemeTable")
@Parcelize
data class ThemeEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var question: String,
    var title: String
): Parcelable
