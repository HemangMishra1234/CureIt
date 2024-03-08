package com.example.finflow.moodStatistics

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finflow.debitAppLogic.Logic
import kotlinx.coroutines.launch

class EditThemeViewModel(private val repository: ThemeRepository) : ViewModel() {
    var themes = repository.themes
    var presentPosition = 0

    fun insert(entity: ThemeEntity) = viewModelScope.launch {
        repository.insert(entity)
    }

    fun update(entity: ThemeEntity) = viewModelScope.launch {
        repository.update(entity)
    }

    fun delete(entity: ThemeEntity) = viewModelScope.launch {
        repository.delete(entity)
    }

    fun getPositionValue(pos: Int): ThemeEntity{
        if(themes.value != null)
        return themes.value!![pos]
        else
            return ThemeEntity(0,"Enter Labels In the Edit Section","Default")
    }

    fun checkIfCanContinue(pos: Int): Boolean{
       if(themes.value != null)
        return pos < themes.value!!.size
        else
            return false
    }

    fun checkIfEmpty():Boolean{
        if(themes.value == null)
            return true
        Log.e("Size", (themes.value)?.size.toString())
        return ((((themes.value)?.size ?: 0) == 0))
    }
}
