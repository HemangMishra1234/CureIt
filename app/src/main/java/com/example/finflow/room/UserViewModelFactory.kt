package com.example.finflow.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModelFactory(private val repository: DebitAppsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DebitAppsViewModel::class.java))
            return DebitAppsViewModel(repository) as T

        throw IllegalAccessException("NO Such View Model class exists")
    }
}