package com.example.finflow.habits


import NotificationHelper
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.transition.Visibility
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.launch

class HabitsViewModel(private val repository: HabitsRepository) : ViewModel(),Observable {

    val habits = repository.allHabits
    lateinit var contextOfMain: Context
    lateinit var contextOfApplication: Context
    private var isUpdateOrDelete = false
    private lateinit var toUpdateOrDelete: HabitsEntity

    companion object {
        @BindingAdapter("app:visibility")
        @JvmStatic
        fun setVisibility(view: View, visibility: Int) {
            view.visibility = visibility
        }
    }



    @Bindable
    val habits_name = MutableLiveData<String?>()
    @Bindable
    val appName = MutableLiveData<String?>()
    @Bindable
    val habits_incRate = MutableLiveData<String?>()
    @Bindable
    val habits_decRate = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateBtn = MutableLiveData<String?>()
    @Bindable
    val deleteAllOrDeleteBtn  = MutableLiveData<String?>()
    @Bindable
    val debitBtnVisi = MutableLiveData<Int?>();
    @Bindable
    val creditBtnVisi = MutableLiveData<Int?>();
    @Bindable
    val visi = MutableLiveData<Int?>()


    init {
        appName.value=""
        habits_incRate.value=""
        habits_decRate.value=""
        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"
        debitBtnVisi.value = View.INVISIBLE
        creditBtnVisi.value = View.INVISIBLE
    }

    fun saveOrUpdate(){//this function is called when saveOr Update button is clicked
        if(isUpdateOrDelete){
            val name: String = habits_name.value!!
            val app: String = appName.value!!
            val inc_rate = habits_incRate.value!!.toFloat()
            val dec_rate = habits_decRate.value!!.toFloat()
            toUpdateOrDelete.name = name;
            toUpdateOrDelete.appName = app
            toUpdateOrDelete.rateIncrement =inc_rate
            toUpdateOrDelete.rateDecrement = dec_rate
            update(toUpdateOrDelete)
        }else{
            val name: String = habits_name.value!!
            val app: String = appName.value!!
            val inc_rate = habits_incRate.value!!.toFloat()
            val dec_rate = habits_decRate.value!!.toFloat()
            insert(HabitsEntity(0,name,inc_rate,dec_rate,0L,0L,app))

            habits_name.value = ""
            appName.value = ""
            habits_incRate.value =""
            habits_decRate.value =""
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete)
        {
            delete(toUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun creditBtn(){
        toUpdateOrDelete.fIncrement++
        update(toUpdateOrDelete)
        var amt: Float = Calculations(contextOfApplication).creditCalculations(10,habits_incRate.value!!.toFloat())
        //Adding the transaction history
        val dao = TransactionDatabase.getInstance(contextOfApplication).transDAOObject
        val repository = TransactionRepository(dao)
        viewModelScope.launch {
            repository.insert(
                TransEntity(
                    0,
                    habits_name.value!!,
                    habits_incRate.value!!.toFloat(),
                    10,
                    Logic().currentDateAndTime(),
                    true,
                    amt
                )
            )
        }
        saveOrUpdate()
        //send the notification
        if(appName != null && appName.value!!.isNotEmpty()) {
            val helper = NotificationHelper(contextOfMain)
            helper.showNotification("Open ${appName.value}")
        }
    }

    fun debitBtn(){
        toUpdateOrDelete.fDecrement++
        update(toUpdateOrDelete)
        var amt: Float = Calculations(contextOfApplication).debitCalculations(10,habits_decRate.value!!.toFloat())
        //Adding the transaction history
        val dao = TransactionDatabase.getInstance(contextOfApplication).transDAOObject
        val repository = TransactionRepository(dao)
        viewModelScope.launch {
            repository.insert(
                TransEntity(
                    0,
                    habits_name.value!!,
                    habits_decRate.value!!.toFloat(),
                    10,
                    Logic().currentDateAndTime(),
                    false,
                    amt
                )
            )
        }
        saveOrUpdate()
        //send the notification
        if(appName != null && appName.value!!.isNotEmpty()) {
            val helper = NotificationHelper(contextOfMain)
            helper.showNotification("Open ${appName.value}")
        }
    }
    fun insert(transEntity: HabitsEntity)= viewModelScope.launch {
        repository.insert(transEntity)
    }

    fun clearAll()= viewModelScope.launch {
        repository.deleteAll()
    }

    fun update(transEntity: HabitsEntity) = viewModelScope.launch{
        repository.update(transEntity)

        habits_name.value = ""
        appName.value = ""
        habits_incRate.value =""
        habits_decRate.value =""

        isUpdateOrDelete=false

        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"

        creditBtnVisi.value = View.INVISIBLE
        debitBtnVisi.value = View.INVISIBLE
    }

    fun delete(transEntity: HabitsEntity)= viewModelScope.launch {
        repository.delete(transEntity)

        habits_name.value = ""
        appName.value = ""
        habits_incRate.value =""
        habits_decRate.value =""
        isUpdateOrDelete=false

        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"
    }



    fun initUpdateOrDelete(transEntity: HabitsEntity){
        debitBtnVisi.value = View.VISIBLE
        creditBtnVisi.value = View.VISIBLE

        habits_name.value = transEntity.name
        appName.value = transEntity.appName
        habits_decRate.value = transEntity.rateDecrement.toString()
        habits_incRate.value = transEntity.rateIncrement.toString()

        isUpdateOrDelete = true

        toUpdateOrDelete = transEntity

        saveOrUpdateBtn.value = "Update"
        deleteAllOrDeleteBtn.value = "Delete"
    }

    //No need to update these because of observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {


    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}