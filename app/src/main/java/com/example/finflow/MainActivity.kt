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
import com.example.finflow.room.DebitApp
import com.example.finflow.room.DebitAppsViewModel
import com.example.finflow.room.DebitAppDB
import com.example.finflow.room.DebitAppsRepository
import com.example.finflow.room.RecyclerViewAdapter
import com.example.finflow.room.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: DebitAppsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Room
        val dao = DebitAppDB.getInstance(application).userDAO
        val repository = DebitAppsRepository(dao)
        val factory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(DebitAppsViewModel::class.java)
        viewModel.contextOfMain = this
        viewModel.contextOfApplicaton = applicationContext


        binding.inputDebitApps = viewModel

        binding.lifecycleOwner = this

        initRecyclerView()

    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        DisplayAppsList()

    }

    private fun DisplayAppsList(){
        viewModel.debitApps.observe(this, Observer {
                                                   binding.recyclerView.adapter = RecyclerViewAdapter(
            it,{selectedItem: DebitApp -> listItemClicked(selectedItem)}
                                                   )
        })
    }

    private fun listItemClicked(selectedItem: DebitApp){
       // Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
    }
}
