package com.example.finflow.goals

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.finflow.debitAppLogic.Logic
import kotlin.math.abs

class LevelLogic(private val context: Context) {

    val SHARED_PREF_NAME = "Level storage pref"
    val SHARED_VALUE = "Level"

    fun saveNewLevelInSharedPref(level : Int) {
        //Opening the sharedpref

        val sharedPref: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        Toast.makeText(context,"Level $level reached!! Congratulations!!!", Toast.LENGTH_SHORT).show()

        //writing data into shared preferences
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(SHARED_VALUE, level)
        editor.apply()
    }

    fun getLevelInSharedPref(): Int{
        //Opening the sharedpref


        val sharedPref: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return  sharedPref.getInt(SHARED_VALUE, 0)
    }

    fun checkIfGoalIsCompleted(lst: List<GoalEntity>): Boolean{
        val target = getLevelInSharedPref()* 50.0F*60 +50.0F*60
        var sum = 0.0F
        for (i in lst){
            sum += i.presentTimeSpent
            if(abs(i.presentPercent - i.expectedPercent) > 2.0F){
                Log.e("LevelLogic","Goal not completed ${i}")
                return false
                }
        }
        Log.e("LevelLogic","sum >= target ${sum}  ${target}")
        return sum >= target
    }
}