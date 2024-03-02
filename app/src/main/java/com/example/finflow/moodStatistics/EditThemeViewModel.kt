package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finflow.debitAppLogic.Logic
import kotlinx.coroutines.launch

class EditThemeViewModel(private val repository: ThemeRepository) : ViewModel() {
    var themes = repository.themes
    val presentPosition = 0;

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

    fun record(value: Int, themeEntity: ThemeEntity){
        val date: String = Logic().currentDate()
        viewModelScope.launch {
//            repository.getMoodByDate(date)
        }
    }
}
