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
    private var isUpdateOrDelete = false
    private lateinit var userToUpdateOrDelete: GoalEntity
    private var totalTimeSpentValue: Long = 0
    private var totalPercentageTillNow: Float = 0.0F

    //TODO("do not forget to reset the total time spent possibly monthly and in the process you will
    // have to reset all cards with their time spent as 0")
    //TODO("Add visibility at last")
    //There is a condition that percent is 100 it should be 0
    //There should be a textView by default which shows total percentage left

    companion object {
        @BindingAdapter("app:visibility")
        @JvmStatic
        fun setVisibility(view: View, visibility: Int) {
            view.visibility = visibility
        }
    }


    @Bindable
    val inputDomain = MutableLiveData<String?>()

    @Bindable
    val inputExpectedPercent = MutableLiveData<String?>()

    @Bindable
    val inputPresentPercent = MutableLiveData<String?>()

    @Bindable
    val inputDesc = MutableLiveData<String?>()

    @Bindable
    val inputTimeGoingToSpend = MutableLiveData<String?>()

    @Bindable
    val inputRate = MutableLiveData<String?>()

    @Bindable
    val totalTimeSpent = MutableLiveData<String?>()

    @Bindable
    val timeSpent = MutableLiveData<String?>()

    @Bindable
    val saveOrUpdateButton = MutableLiveData<String?>()

    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String?>()

    @Bindable
    val percentLeft = MutableLiveData<String?>()

    //Visibility Bindables
    @Bindable
    val visiRate = MutableLiveData<Int?>()

    @Bindable
    val visiTimeGoingToStart = MutableLiveData<Int?>()

    @Bindable
    val visiUseBtn = MutableLiveData<Int?>()

    @Bindable
    val visiPresentPercent = MutableLiveData<Int?>()

    @Bindable
    val visiTimeSpentForThis = MutableLiveData<Int?>()

    @Bindable
    val visiOfPercentLeft = MutableLiveData<Int?>()

    init {
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
        inputDesc.value = "Description"

        //Setting visibilities
        makeInvisible()
        inputExpectedPercent.value = "10.0"
        updateTotalPercentTillNow()
        updateTotalTimeSpentInMin()
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            //Make Update
            userToUpdateOrDelete.domain = inputDomain.value!!
            userToUpdateOrDelete.expectedPercent = inputExpectedPercent.value!!.toFloat()
            //Rate updater
            //TODO("Update all other rates after this")
            val rateNew: Float = Logic().rateCalculator(
                userToUpdateOrDelete.presentPercent,
                userToUpdateOrDelete.expectedPercent
            )
            userToUpdateOrDelete.rate = rateNew
            update(userToUpdateOrDelete)
        } else {
            val domain = inputDomain.value!!
            val desc = inputDesc.value!!
            //TODO("Call here to reset percent left")
            val expectPercent = inputExpectedPercent.value!!.toFloat()
            val rateNew = Logic().rateCalculator(0.0F, expectPercent)
            insert(GoalEntity(0, domain, expectPercent, 0.0F, 0, rateNew))

            inputDomain.value = null
            inputDesc.value = null
            inputExpectedPercent.value = null
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(userToUpdateOrDelete)

        } else {
            clearAll()

        }
    }

    fun insert(entity: GoalEntity) = viewModelScope.launch {
        Log.d("UpdateSingleItem", "Entity in insert function entity: $entity")
        repository.insert(entity)
    }

    fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
    }

    fun use() {
        updateTotalPercentTillNow()
        if (totalPercentageTillNow == 100.0F) {
            if (isUpdateOrDelete && inputTimeGoingToSpend.value != null) {
                //Note the time
                var time = inputTimeGoingToSpend.value!!.toLong()
                val cal = Calculations(contextOfMain)
                val amt = cal.creditCalculations(time, rate = inputRate.value!!.toFloat())
                //Adding the transaction history
                val dao = TransactionDatabase.getInstance(contextOfApplicaton).transDAOObject
                val repository = TransactionRepository(dao)
                viewModelScope.launch {
                    repository.insert(
                        TransEntity(
                            0,
                            inputDomain.value!!,
                            inputRate.value!!.toFloat(),
                            inputTimeGoingToSpend.value!!.toLong(),
                            Logic().currentDateAndTime(),
                            true,
                            amt
                        )
                    )
                }
                //update the UI
                userToUpdateOrDelete.presentTimeSpent += time
                Log.e("update", "enitiy after adding time $userToUpdateOrDelete")

                //Updating the functions

                saveOrUpdate()
//                updateTotalTimeSpentInMin()
//                updateAllRatesAndPercent()
//                updateTotalPercentTillNow()
                val logic = LevelLogic(contextOfMain)
                if (logic.checkIfGoalIsCompleted(goals.value!!)) {
                    logic.saveNewLevelInSharedPref(logic.getLevelInSharedPref() + 1)
                    resetForNextLevel()
                }

                //Checking for next level


                //saveOrUpdate()


            } else if (inputTimeGoingToSpend.value != null) {
                Toast.makeText(contextOfMain, "SELECT THE Domain FIRST", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contextOfMain, "Enter Time first", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(contextOfMain, "The sum must be 100", Toast.LENGTH_SHORT).show()
        }
    }

    fun update(entity: GoalEntity) = viewModelScope.launch {
        Log.d("Update()", "Entity in update function: $entity")
        repository.update(entity)
        //After updating we are resetting all the variables and making them null

        inputDesc.value = null
        inputDomain.value = null
        inputExpectedPercent.value = 0.0F.toString()
        inputPresentPercent.value = 0.0F.toString()
        inputRate.value = null
        timeSpent.value = null


        isUpdateOrDelete = false

        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
        makeInvisible()
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
        updateTotalPercentTillNow()
    }

    fun delete(entity: GoalEntity) = viewModelScope.launch {
        repository.delete(entity)

        inputDesc.value = null
        inputDomain.value = null
        inputExpectedPercent.value = 0.0F.toString()
        inputPresentPercent.value = 0.0F.toString()
        inputRate.value = null
        timeSpent.value = null

        isUpdateOrDelete = false

        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
        updateTotalTimeSpentInMin()
        updateAllRatesAndPercent()
        updateTotalPercentTillNow()
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

    fun updateTotalTimeSpentInMin() {
        val list = goals.value
        var sum: Long = 0
        if (list != null) {
            for (element in list) {
                if (element != null)
                    sum += element.presentTimeSpent
            }
        }
        totalTimeSpentValue = sum
        totalTimeSpent.value = Logic().convertMinutesToHoursString(totalTimeSpentValue)
    }

    fun updateAllRatesAndPercent() {
        val list = goals.value
        if (list != null) {
            for (element in list) {
                if (element != null)
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
        percentLeft.value = (100 - sum).toString()
    }


    fun initUpdateOrDelete(entity: GoalEntity) {
        Log.d("init", "Entity in initUpdate: $entity")
        makeVisibile()

        inputDomain.value = entity.domain
        inputPresentPercent.value = Logic().formatFloatToTwoDecimalPlaces(entity.presentPercent)
        inputExpectedPercent.value = entity.expectedPercent.toString()
        inputTimeGoingToSpend.value = "30"
        inputRate.value = Logic().formatFloatToTwoDecimalPlaces(entity.rate)
        timeSpent.value = entity.presentTimeSpent.toString()
        totalTimeSpent.value = Logic().convertMinutesToHoursString(totalTimeSpentValue)
        //percentLeft.value = (100 - totalPercentageTillNow).toString()
        percentLeft.value = Logic().recoveryTimeCalculator(
            entity.expectedPercent,
            entity.presentTimeSpent.toFloat(),
            totalTimeSpentValue.toFloat()
        )
        isUpdateOrDelete = true
        userToUpdateOrDelete = entity
        saveOrUpdateButton.value = "Update"
        clearAllOrDeleteButton.value = "Delete"
        updateTotalTimeSpentInMin()
    }

    fun makeVisibile() {
        visiRate.value = View.VISIBLE
        visiTimeGoingToStart.value = View.VISIBLE
        visiUseBtn.value = View.VISIBLE
        visiPresentPercent.value = View.VISIBLE
        visiTimeSpentForThis.value = View.VISIBLE
    }

    fun makeInvisible() {
        visiRate.value = View.INVISIBLE
        visiTimeGoingToStart.value = View.INVISIBLE
        visiUseBtn.value = View.INVISIBLE
        visiPresentPercent.value = View.INVISIBLE
        visiTimeSpentForThis.value = View.INVISIBLE
    }


    fun updateSingleItem(entity: GoalEntity) = viewModelScope.launch {
        Log.d("UpdateSingleItem", "entity at the start: $entity")
        var presentPercent2: Float = 0.0F
        if (totalTimeSpentValue != 0L)
            presentPercent2 = (entity.presentTimeSpent.toFloat() / totalTimeSpentValue) * 100
        try {
            val rate = Logic().rateCalculator(presentPercent2, entity.expectedPercent)
            entity.rate = rate
            entity.presentPercent = presentPercent2
            repository.update(entity)
          //  Log.d("UpdateSingleItem", "Updated entity: $entity")
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