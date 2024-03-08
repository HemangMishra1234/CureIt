package com.example.finflow.dashboard

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.SharedPreferenceLiveData

class DashboardViewModel(context: Context) : ViewModel() {
    var sharedPreferences: SharedPreferences
    var myLiveData: SharedPreferenceLiveData
    init {
        sharedPreferences =
            context.getSharedPreferences(Calculations(context).SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
       myLiveData = SharedPreferenceLiveData(sharedPreferences, Calculations(context).SHARED_VALUE)
    }

    fun getMyLiveData(): LiveData<Float> {
        return myLiveData
    }
}