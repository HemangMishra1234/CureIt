package com.example.finflow.statistics

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: TransactionRepository,private val type: Int) : ViewModel(), Observable {
    private val pr = StatsLogic().getPresentAndPastDate(type)

    val allTrans = repository.searchTransaction(pr.second, pr.first)


    @Bindable
     val netChange = MutableLiveData<String?>()
    @Bindable
     val creditAvg = MutableLiveData<String?>()
    @Bindable
     val debitAvg = MutableLiveData<String?>()

    class StatisticsViewModel(private val repository: TransactionRepository,private val type: Int) : ViewModel(), Observable {
        private val pr = StatsLogic().getPresentAndPastDate(type)

        val allTrans = repository.searchTransaction(pr.second, pr.first)

        @Bindable
        val netChange = MutableLiveData<String?>()
        @Bindable
        val creditAvg = MutableLiveData<String?>()
        @Bindable
        val debitAvg = MutableLiveData<String?>()

        init{
            allTrans.observeForever { transactions ->
                if (transactions != null) {
                    viewModelScope.launch() {
                        initData()
                    }
                } else {
                    netChange.value = "0.0"
                    creditAvg.value = "0.0"
                    debitAvg.value = "0.0"
                }
            }
        }

        private suspend fun initData(){
            val l = StatsLogic().generateHoursStudied(allTrans.value!!, type, 1)
            val l2 = StatsLogic().generateHoursStudied(allTrans.value!!, type, 0)
            val net = l.sum() - l2.sum()
            netChange.value =  (net/10000000f).toString()
            creditAvg.value = (l.sum()/l.count()).toString()
            debitAvg.value = (l2.sum()/l2.count()).toString()
        }

        override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

        }

        override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

        }
    }

    fun initData(){
        val l = StatsLogic().generateHoursStudied(allTrans.value!!, type, 1)
        val l2 = StatsLogic().generateHoursStudied(allTrans.value!!, type, 0)
        val net = l.sum() - l2.sum()
        netChange.value =  (net/10000000f).toString()
        creditAvg.value = (l.sum()/l.count()).toString()
        debitAvg.value = (l2.sum()/l2.count()).toString()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}