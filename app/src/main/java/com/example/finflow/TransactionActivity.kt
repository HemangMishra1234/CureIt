package com.example.finflow

import CountdownManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.databinding.ActivityMainBinding
import com.example.finflow.databinding.FragmentTransactionBinding
import com.example.finflow.room.DebitApp
import com.example.finflow.room.DebitAppsViewModel
import com.example.finflow.room.DebitAppDB
import com.example.finflow.room.DebitAppsRepository
import com.example.finflow.room.RecyclerViewAdapter
import com.example.finflow.room.UserViewModelFactory
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRVAdapter
import com.example.finflow.transactionHistory.TransactionRepository
import com.example.finflow.transactionHistory.TransactionViewModel
import com.example.finflow.transactionHistory.TransactionViewModelFactory

class TransactionActivity : AppCompatActivity() {
    lateinit var binding: FragmentTransactionBinding
    lateinit var viewModel: TransactionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_transaction)

        //Room
        val dao = TransactionDatabase.getInstance(application).transDAOObject
        val repository = TransactionRepository(dao)
        val factory = TransactionViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)
        viewModel.contextOfMain = this
        viewModel.contextOfApplication = applicationContext


        binding.transaction = viewModel

        binding.lifecycleOwner = this

        initRecyclerView()


    }

    private fun initRecyclerView(){
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        DisplayAppsList()

    }

    private fun DisplayAppsList(){
        viewModel.transactions.observe(this, Observer {
            binding.recyclerView2.adapter = TransactionRVAdapter(
                it,{selectedItem: TransEntity -> listItemClicked(selectedItem)}
            )
        })
    }

    private fun listItemClicked(selectedItem: TransEntity){
        Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
    }
}
