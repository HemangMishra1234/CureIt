package com.example.finflow.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HabitsViewModelFactory(private val repository: HabitsRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HabitsViewModel::class.java))
            return HabitsViewModel(repository) as T
        throw IllegalArgumentException("No such View Modal Class exists")
    }
}