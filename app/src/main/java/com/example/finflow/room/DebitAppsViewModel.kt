package com.example.finflow.room

import CountdownManager
import NotificationHelper
import android.app.NotificationChannel
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.launch

class DebitAppsViewModel(private val repository: DebitAppsRepository ): ViewModel(),Observable{

    val debitApps = repository.allDebitApps
    lateinit var contextOfMain : Context
    lateinit var contextOfApplicaton:Context
    private var isUpdateOrDelete = false
    private lateinit var userToUpdateOrDelete: DebitApp

    @Bindable
    val inputName = MutableLiveData<String?>()
    @Bindable
    val inputDesc = MutableLiveData<String?>()
    @Bindable
    val rate = MutableLiveData<String?>()
    @Bindable
    val inputfreq = MutableLiveData<String?>()
    @Bindable
    val inputTime = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String?>()
    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String?>()

    init{
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
        rate.value = "175"
        inputTime.value = "1"
        inputfreq.value = "0"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            //Make Update
            userToUpdateOrDelete.name = inputName.value!!
            userToUpdateOrDelete.desc = inputDesc.value!!
            userToUpdateOrDelete.rate = rate.value!!.toFloat()
            userToUpdateOrDelete.freq = inputfreq.value!!.toInt()
            update(userToUpdateOrDelete)
        }else{
            val name = inputName.value!!
            val desc = inputDesc.value!!
            val rate2 = rate.value!!
            val freq = inputfreq.value!!
            insert(DebitApp(0,name,desc,rate2.toFloat(), freq = freq.toInt()))//TODO(Why we have used 0 here for id)

            inputName.value = null
            inputDesc.value = null
            rate.value = null
            inputfreq.value = null
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(userToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun insert(debitApp: DebitApp) = viewModelScope.launch {
        repository.insert(debitApp)
    }

    fun clearAll()= viewModelScope.launch {
        repository.deleteAll()
    }

    fun use(){
        if(isUpdateOrDelete && inputTime.value != null) {
            //TODO("Implement the onClick of the use button")
            //Note the time
            var time = inputTime.value!!.toLong()
            val cal = Calculations(contextOfMain)
            val amt = cal.debitCalculations(time, rate = rate.value!!.toFloat())
            //Updating the frequency
            var f = inputfreq.value!!.toInt()
            inputfreq.value = (++f).toString()
            //Adding the transaction history
            val dao = TransactionDatabase.getInstance(contextOfApplicaton).transDAOObject
            val repository = TransactionRepository(dao)
            viewModelScope.launch {
                repository.insert(
                    TransEntity(
                        0, inputName.value!!, rate.value!!.toFloat(), inputTime.value!!.toLong(),
                        Logic().currentDateAndTime(), false, amt
                    )
                )
            }
            //send the notification
            val helper=NotificationHelper(contextOfMain)
            helper.showNotification("Open ${inputName.value} opening for $time minutes")
            //update the UI
            saveOrUpdate()
        }else if(inputTime.value != null){
            Toast.makeText(contextOfMain,"SELECT THE APP FIRST",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(contextOfMain,"Enter Time first",Toast.LENGTH_SHORT).show()
        }
    }

    fun update(debitApp: DebitApp)= viewModelScope.launch {
        repository.update(debitApp)
        //After updating we are resetting all the variables and making them null

        inputDesc.value = null
        inputName.value = null
        inputfreq.value = null
        rate.value = null

        isUpdateOrDelete = false

        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
    }

    fun delete(debitApp: DebitApp)= viewModelScope.launch {
        repository.delete(debitApp)

        inputDesc.value = null
        inputName.value = null
        inputfreq.value = null
        rate.value = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"

    }


    fun initUpdateOrDelete(debitApp: DebitApp){
        inputName.value = debitApp.name
        inputDesc.value = debitApp.desc
        rate.value = debitApp.rate.toString()
        inputfreq.value = debitApp.freq.toString()
        isUpdateOrDelete = true
        userToUpdateOrDelete = debitApp
        saveOrUpdateButton.value = "Update"
        clearAllOrDeleteButton.value = "Delete"
    }


    //No need to update these because of observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}