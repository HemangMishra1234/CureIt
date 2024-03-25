package com.example.finflow.goals


import CountdownManager
import NotificationHelper
import android.app.NotificationChannel
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.transition.Visibility
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.launch

class GoalViewModel(private val repository: GoalsRepository) : ViewModel(), Observable {

    var goals = repository.allGoals
    lateinit var contextOfMain: Context
    lateinit var contextOfApplicaton: Context
    var isUpdateOrDelete = false
    lateinit var userToUpdateOrDelete: GoalEntity
    private var totalTimeSpentValue: Long = 0
    var totalPercentageTillNow: Float = 0.0F

    companion object {
        @BindingAdapter("app:visibility")
        @JvmStatic
        fun setVisibility(view: View, visibility: Int) {
            view.visibility = visibility
        }
    }

    //Visibility Bindables
    @Bindable
    val visiUseBtn = MutableLiveData<Int?>()


    @Bindable
    val visiTimePicker = MutableLiveData<Int?>()

    @Bindable
    val recoveryTime = MutableLiveData<Int?>()

    init {
        //Setting visibilities
        makeInvisible()
        updateTotalPercentTillNow()
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
    }


    fun insert(entity: GoalEntity) = viewModelScope.launch {
        Log.d("UpdateSingleItem", "Entity in insert function entity: $entity")
        repository.insert(entity)
    }


    fun update(entity: GoalEntity) {
        viewModelScope.launch {
            Log.d("Update()", "Entity in update function: $entity")
            isUpdateOrDelete = false
            repository.update(entity)
        }
        makeInvisible()
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
    }

    fun delete(entity: GoalEntity) = viewModelScope.launch {
        repository.delete(entity)

        isUpdateOrDelete = false

        updateAllRatesAndPercent()
    }

    fun resetForNextLevel(){
        val list = goals.value
        if (list != null) {
            for (element in list) {
                if (element != null)
                {
                    element.presentTimeSpent = 0
                    element.presentPercent = 0.0F
                    element.rate = 350.0F
                    update(element)
                }
            }
        }
        val cal = Calculations(contextOfMain)
        val time =LevelLogic(contextOfMain).getLevelInSharedPref()*3000L
        val amt = cal.creditCalculations(time,
            56.0F)
        //Adding the transaction history

        val dao = TransactionDatabase.getInstance(contextOfApplicaton).transDAOObject
        val repository = TransactionRepository(dao)
        viewModelScope.launch {
            repository.insert(
                TransEntity(
                    0,
                    "Bonus for next Level",
                    56.0F,
                    time,
                    Logic().currentDateAndTime(),
                    true,
                    amt
                )
            )
        }

        userToUpdateOrDelete.presentPercent = 0.0F
        userToUpdateOrDelete.presentTimeSpent = 0
        printAllEntites()
    }

    fun updateTotalTimeSpentInMin() : Long {
        val list = goals.value
        var sum: Long = 0
        if (list != null) {
            for (element in list) {
                if (element != null)
                    sum += element.presentTimeSpent
            }
        }
        totalTimeSpentValue = sum
        return sum
    }

    fun updateAllRatesAndPercent() {
            val list = goals.value
            if (list != null) {
                for (element in list) {
                    updateSingleItem(element)//I got that error is at this point What to do?
                }
            }

    }

    fun updateTotalPercentTillNow() {
        val list = goals.value
        var sum: Float = 0.0F
        if (list != null) {
            for (element in list) {
                if (element != null)
                    sum += element.expectedPercent
            }
        }
        totalPercentageTillNow = sum
    }


    fun initUpdateOrDelete(entity: GoalEntity) {
        Log.d("init", "Entity in initUpdate: $entity")
        makeVisibile()

        isUpdateOrDelete = true
        userToUpdateOrDelete = entity
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
    }

    fun makeVisibile() {

        visiUseBtn.value = View.VISIBLE
        visiTimePicker.value = View.VISIBLE
        recoveryTime.value = View.VISIBLE
    }

    fun makeInvisible() {

        visiUseBtn.value = View.INVISIBLE
        visiTimePicker.value = View.INVISIBLE
        recoveryTime.value = View.INVISIBLE
    }

    fun getRecoveryTime(entity: GoalEntity)=
        Logic().recoveryTimeCalculator(
            entity.expectedPercent,
            entity.presentTimeSpent.toFloat(),
            totalTimeSpentValue.toFloat()
        )



    fun updateSingleItem(entity: GoalEntity) = viewModelScope.launch {
//        Log.d("UpdateSingleItem", "entity at the start: $entity")
        var presentPercent2: Float = 0.0F
        if (totalTimeSpentValue != 0L)
            presentPercent2 = (entity.presentTimeSpent.toFloat() / totalTimeSpentValue) * 100
        try {
            val rate = Logic().rateCalculator(presentPercent2, entity.expectedPercent)
            entity.rate = rate
            entity.presentPercent = presentPercent2
            repository.update(entity)
            Log.d("UpdateSingleItem", "Updated entity: $entity")
        } catch (e: Exception) {
//            Log.e(
//                "UpdateSingleItem", "Error updating entity $presentPercent2" +
//                        "entity.presentTimeSpent.toFloat()=$entity.presentTimeSpent.toFloat()" +
//                        "totalTimeSpentValue=$totalTimeSpentValue", e
//            )
        }
    }


    //No need to update these because of observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    fun printAllEntites() {
        val list = goals.value
        if (list != null) {
            for (element in list) {
                if (element != null)
                    Log.e("Debug","$element")
            }
        }
    }

}