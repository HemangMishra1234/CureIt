package com.example.finflow.transactionHistory

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
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel(),Observable {

    val transactions = repository.allTransactions
    lateinit var contextOfMain: Context
    lateinit var contextOfApplication: Context
    private var isUpdateOrDelete = false
    private lateinit var transToUpdateOrDelete: TransEntity
    private var lastAmt: Float=0.0F
    //TODO(do not forget to update this value when a user clicks on a card, then
   // update its value each time user clicks update or delete button)
    //TODO(think on the bug if we press calculate button then press update button, in this
    //case wrong value will be stored in this lastAmt
    //Dont update this variable with the value of EditText


    companion object {
        @BindingAdapter("app:visibility")
        @JvmStatic
        fun setVisibility(view: View, visibility: Int) {
            view.visibility = visibility
        }
    }



    @Bindable
    val trans_name = MutableLiveData<String?>()
    @Bindable
    val trans_rate = MutableLiveData<String?>()
    @Bindable
    val trans_dateTime = MutableLiveData<String?>()
    @Bindable
    val trans_time = MutableLiveData<String?>()
    @Bindable
    val trans_type = MutableLiveData<Boolean?>()
    @Bindable
    val trans_amount = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateBtn = MutableLiveData<String?>()
    @Bindable
    val deleteAllOrDeleteBtn  = MutableLiveData<String?>()
    @Bindable
    val visi = MutableLiveData<Int?>()


    init {
        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"
        trans_type.value=true
        trans_dateTime.value=Logic().currentDateAndTime()
        visi.value = View.VISIBLE
//TODO(Do not forget to initialize date time when update button is clicked)
    }

    fun saveOrUpdate(){//this function is called when saveOr Update button is clicked
        if(isUpdateOrDelete){
            val name: String = trans_name.value!!
            val rate = trans_rate.value!!.toFloat()
            val time: Long = trans_time.value!!.toLong()
            val dateTime: String = trans_dateTime.value!!
            val credit = trans_type.value!!
            val amount = Calculations(contextOfMain).updateTransactionInSP(rate,time,credit,lastAmt)
            transToUpdateOrDelete.name = name
            transToUpdateOrDelete.rate = rate
            transToUpdateOrDelete.time = time
            transToUpdateOrDelete.dateTime = dateTime
            transToUpdateOrDelete.credit = credit
            transToUpdateOrDelete.amount = amount
            lastAmt = 0.0F
            update(transToUpdateOrDelete)
        }else{
            val name: String = trans_name.value!!
            val rate = trans_rate.value!!.toFloat()
            val time: Long = trans_time.value!!.toLong()
            val dateTime: String = Logic().currentDateAndTime()
            val credit = trans_type.value!!
            var amount:Float
            if(credit)
            amount = Calculations(contextOfMain).creditCalculations(time,rate)
            else
                amount = Calculations(contextOfMain).debitCalculations(time,rate)

            insert(TransEntity(0,name,rate,time,dateTime,credit,amount))

            trans_rate.value = null
            trans_rate.value = null
            trans_time.value = null
            trans_dateTime.value = null
            trans_type.value = true
            trans_amount.value = null
            lastAmt = 0.0F
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete)
        {
            Calculations(contextOfMain).updateTransactionInSP(0.0F,0,true,lastAmt)
            lastAmt = 0.0F
            delete(transToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun calWithoutSave(){
        if(trans_time.value != null && trans_rate.value != null){
            val amt = Calculations(contextOfMain).calculationsWithoutSaving(trans_time.value!!.toLong(),trans_rate.value!!.toFloat(),trans_type.value!!)
            trans_amount.value = Logic().formatAmountInCrores(amt)
        }else{
            Toast.makeText(contextOfMain,"First enter rate,time",Toast.LENGTH_SHORT).show()
        }
    }

    fun insert(transEntity: TransEntity)= viewModelScope.launch {
        repository.insert(transEntity)
    }

    fun clearAll()= viewModelScope.launch {
        repository.deleteAll()
    }

    fun update(transEntity: TransEntity) = viewModelScope.launch{
        repository.update(transEntity)

        trans_name.value=null
        trans_time.value=null
        trans_rate.value = null
        trans_dateTime.value = null
        trans_type.value =true
        trans_amount.value=null

        isUpdateOrDelete=false

        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"
    }

    fun delete(transEntity: TransEntity)= viewModelScope.launch {
        repository.delete(transEntity)

        trans_name.value=null
        trans_time.value=null
        trans_rate.value = null
        trans_dateTime.value = null
        trans_type.value =true
        trans_amount.value=null

        isUpdateOrDelete=false

        saveOrUpdateBtn.value="Save"
        deleteAllOrDeleteBtn.value="Clear All"
    }



    fun initUpdateOrDelete(transEntity: TransEntity){
        visi.value = View.VISIBLE
        lastAmt = transEntity.amount

        trans_name.value=transEntity.name
        trans_rate.value=transEntity.rate.toString()
        trans_time.value=transEntity.time.toString()
        trans_dateTime.value=transEntity.dateTime
        trans_type.value=transEntity.credit
        trans_amount.value=Logic().formatAmountInCrores(transEntity.amount)

        isUpdateOrDelete = true

        transToUpdateOrDelete = transEntity

        saveOrUpdateBtn.value = "Update"
        deleteAllOrDeleteBtn.value = "Delete"
    }

    //No need to update these because of observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {


    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}