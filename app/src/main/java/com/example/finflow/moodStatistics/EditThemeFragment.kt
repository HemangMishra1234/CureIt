package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.R
import com.example.finflow.databinding.FragmentEditThemeBinding
import com.example.finflow.goals.GoalEntity
import com.example.finflow.goals.GoalsAdapter

class EditThemeFragment : Fragment() {
    lateinit var binding: FragmentEditThemeBinding
    private lateinit var viewModel: EditThemeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_theme, container, false)

        val dao = ThemeDatabase.getInstance(requireContext()).themeDaoObject
        val repository = ThemeRepository(dao)
        val factory = EditThemeViewModelFactory(repository)
        binding.addLabelButton.setOnClickListener(){
            val bundle = bundleOf(Pair("Name",""), Pair("Question",""), Pair("ID", 0))
            val navController = this.findNavController()
            Log.e("Current Destination",navController.currentDestination.toString())
            navController.navigate(R.id.action_editThemeFragment_to_addNewThemeFragment, bundle)
        }
        viewModel = ViewModelProvider(this, factory)[EditThemeViewModel::class.java]
        binding.lifecycleOwner = activity
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView(){
        binding.themeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        displayAppsList()

    }

    private fun displayAppsList(){
        viewModel.themes.observe(requireActivity(), Observer {
            binding.themeRecyclerView.adapter = MoodAdapter(
                it
            ) { selectedItem: ThemeEntity -> listItemClicked(findNavController(),selectedItem) }
        })
    }

    private fun listItemClicked(navController:NavController,selectedItem: ThemeEntity){
        val bundle = bundleOf(Pair("Name",selectedItem.title), Pair("Question", selectedItem.question), Pair("ID", selectedItem.id))

        navController.navigate(R.id.action_editThemeFragment_to_addNewThemeFragment, bundle)
    }
}