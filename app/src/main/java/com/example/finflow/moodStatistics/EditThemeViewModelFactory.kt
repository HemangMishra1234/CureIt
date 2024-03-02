package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class EditThemeViewModelFactory(private val repository: ThemeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditThemeViewModel::class.java))
            return EditThemeViewModel(repository) as T

        throw IllegalArgumentException("No such View Model Class")
    }
}