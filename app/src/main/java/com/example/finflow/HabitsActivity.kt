package com.example.finflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.databinding.ActivityHabitsBinding
import com.example.finflow.habits.HabitsDatabase
import com.example.finflow.habits.HabitsEntity
import com.example.finflow.habits.HabitsRVAdapter
import com.example.finflow.habits.HabitsRepository
import com.example.finflow.habits.HabitsViewModel
import com.example.finflow.habits.HabitsViewModelFactory


class HabitsActivity : AppCompatActivity() {
    lateinit var binding: ActivityHabitsBinding
    lateinit var viewModel: HabitsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habits)

        //Room
        val dao = HabitsDatabase.getInstance(application).habitsDAOObject
        val repository = HabitsRepository(dao)
        val factory = HabitsViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(HabitsViewModel::class.java)
        viewModel.contextOfMain = this
        viewModel.contextOfApplication = applicationContext


        binding.inputHabits = viewModel

        binding.lifecycleOwner = this

        initRecyclerView()

    }

    private fun initRecyclerView(){
        binding.recyclerViewHabits.layoutManager = LinearLayoutManager(this)
        DisplayAppsList()

    }

    private fun DisplayAppsList(){
        viewModel.habits.observe(this, Observer {
            binding.recyclerViewHabits.adapter = HabitsRVAdapter(
                it,{selectedItem: HabitsEntity -> listItemClicked(selectedItem)}
            )
        })
    }

    private fun listItemClicked(selectedItem: HabitsEntity){
        //Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
    }
}
