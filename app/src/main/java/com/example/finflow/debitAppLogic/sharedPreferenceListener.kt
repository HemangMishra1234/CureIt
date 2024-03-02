package com.example.finflow.debitAppLogic

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedPreferenceLiveData(private val sharedPreferences: SharedPreferences, private val key: String) :
    LiveData<Float>() {

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                value = sharedPreferences.getFloat(key, 0.0F)
            }
        }

    override fun onActive() {
        super.onActive()
        value = sharedPreferences.getFloat(key, 0.0F)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        super.onInactive()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}
