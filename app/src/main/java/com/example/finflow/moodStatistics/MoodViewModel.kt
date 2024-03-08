package com.example.finflow.moodStatistics

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.finflow.debitAppLogic.Logic

class MoodViewModel( private  val repository: MoodRepository): ViewModel(){
    var moods = repository.moods

    fun record(value: Int, themeEntity: ThemeEntity){
        val date: String = Logic().currentDate()
        Log.e("Printing Theme Entity Title", "$themeEntity")
        val todayList = moods.value
        Log.e("List", "${todayList?.get(0)}")
//        viewModelScope.launch {
//               Log.e("Mood Entity", "$entity")
//            if(entity == null){
//                mrepository.insert(MoodEntity(0, date, themeEntity.title, 1, value))
//            }
//            else{
//                val temp = entity
//                temp.count += 1
//                temp.average = (temp.average * (temp.average - 1) + value)/(temp.average)
//                mrepository.update(temp)
//            }
//        }
    }
}