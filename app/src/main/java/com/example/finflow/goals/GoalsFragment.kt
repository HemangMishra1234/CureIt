package com.example.finflow.goals


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.R
import com.example.finflow.databinding.ActivityGoalsBinding

class GoalsFragment : Fragment() {
    lateinit var binding: ActivityGoalsBinding
    lateinit var viewModel: GoalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_goals,container,false)

        //Room
        val dao = GoalDatabase.getInstance(requireContext()).goalDAO
        val repository = GoalsRepository(dao)
        val factory = GoalsViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[GoalViewModel::class.java]
        viewModel.contextOfMain = requireContext()
        viewModel.contextOfApplicaton = requireContext()


        binding.inputGoals = viewModel

        binding.lifecycleOwner = activity

        initRecyclerView()
        return binding.root

    }

    private fun initRecyclerView(){
        binding.recyclerView3.layoutManager = LinearLayoutManager(requireContext())
        displayAppsList()

    }

    private fun displayAppsList(){
        viewModel.goals.observe(requireActivity(), Observer {
            binding.recyclerView3.adapter = GoalsAdapter(
                it
            ) { selectedItem: GoalEntity -> listItemClicked(selectedItem) }
        })
    }

    private fun listItemClicked(selectedItem: GoalEntity){
        //Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
    }
}