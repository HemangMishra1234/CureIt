package com.example.finflow.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GoalsViewModelFactory(private val repository: GoalsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GoalViewModel::class.java))
            return GoalViewModel(repository) as T

        throw IllegalAccessException("NO Such View Model class exists")
    }
}