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
import com.example.finflow.databinding.ActivityGoalsBinding
import com.example.finflow.databinding.ActivityMainBinding
import com.example.finflow.goals.GoalDatabase
import com.example.finflow.goals.GoalEntity
import com.example.finflow.goals.GoalViewModel
import com.example.finflow.goals.GoalsAdapter
import com.example.finflow.goals.GoalsRepository
import com.example.finflow.goals.GoalsViewModelFactory
import com.example.finflow.room.DebitApp
import com.example.finflow.room.DebitAppsViewModel
import com.example.finflow.room.DebitAppDB
import com.example.finflow.room.DebitAppsRepository
import com.example.finflow.room.RecyclerViewAdapter
import com.example.finflow.room.UserViewModelFactory

class GoalsActivity : AppCompatActivity() {
    lateinit var binding: ActivityGoalsBinding
    lateinit var viewModel: GoalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goals)

        //Room
        val dao = GoalDatabase.getInstance(application).goalDAO
        val repository = GoalsRepository(dao)
        val factory = GoalsViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(GoalViewModel::class.java)
        viewModel.contextOfMain = this
        viewModel.contextOfApplicaton = applicationContext


        binding.inputGoals = viewModel

        binding.lifecycleOwner = this

        initRecyclerView()

    }

    private fun initRecyclerView(){
        binding.recyclerView3.layoutManager = LinearLayoutManager(this)
        DisplayAppsList()

    }

    private fun DisplayAppsList(){
        viewModel.goals.observe(this, Observer {
            binding.recyclerView3.adapter = GoalsAdapter(
                it,{selectedItem: GoalEntity -> listItemClicked(selectedItem)}
            )
        })
    }

    private fun listItemClicked(selectedItem: GoalEntity){
        //Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
    }
}
