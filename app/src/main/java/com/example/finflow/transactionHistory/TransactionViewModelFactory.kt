package com.example.finflow.transactionHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TransactionViewModelFactory(private val repository: TransactionRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionViewModel::class.java))
            return TransactionViewModel(repository) as T
        throw IllegalArgumentException("No such View Modal Class exists")
    }
}