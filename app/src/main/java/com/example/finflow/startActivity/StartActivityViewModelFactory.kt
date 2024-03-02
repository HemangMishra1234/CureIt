package com.example.finflow.startActivity

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StartActivityViewModelFactory(private val sharedPreferences: SharedPreferences,private val context1: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartActivityViewModel(sharedPreferences,  context = context1) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
