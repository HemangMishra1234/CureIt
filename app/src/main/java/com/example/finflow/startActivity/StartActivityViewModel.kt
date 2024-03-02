package com.example.finflow.startActivity

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.SharedPreferenceLiveData

class StartActivityViewModel(private val sharedPreferences: SharedPreferences, context: Context) : ViewModel() {

    private val myLiveData = SharedPreferenceLiveData(sharedPreferences, Calculations(context).SHARED_VALUE)


    fun getMyLiveData(): LiveData<Float> {
        return myLiveData
    }
}
