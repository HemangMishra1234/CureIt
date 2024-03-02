package com.example.finflow.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finflow.transactionHistory.TransactionRepository
import com.example.finflow.transactionHistory.TransactionViewModel

class StatisticsViewModelFactory (private val repository: TransactionRepository,private val type: Int): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatisticsViewModel::class.java))
            return StatisticsViewModel(repository, type) as T
        throw IllegalArgumentException("No such View Modal Class exists")
    }
}