package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MoodViewModelFactory(private val repository: MoodRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoodViewModel::class.java))
            return MoodViewModel(repository) as T

        throw IllegalArgumentException("No such View Model Class")
    }
}