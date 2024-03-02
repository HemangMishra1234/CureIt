package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.finflow.R
import com.example.finflow.databinding.FragmentMoodTrackBinding

class MoodTrack : Fragment() {

    companion object {
        fun newInstance() = MoodTrack()
    }

    private lateinit var viewModel: EditThemeViewModel
    private lateinit var binding: FragmentMoodTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mood_track,container,false)
        val factory = EditThemeViewModelFactory(getThemeRepoObject())
        viewModel = ViewModelProvider(this, factory)[EditThemeViewModel::class.java]
        binding.lifecycleOwner = activity

        viewModel.themes.observe(viewLifecycleOwner, Observer {themes ->
        //checking if labels are empty
        if(viewModel.checkIfEmpty()){
            Toast.makeText(activity, "Add labels first", Toast.LENGTH_SHORT).show()
            redirectToEditThemeFragment()
        }else {
            var entity = initUI()
            binding.rate5.setOnClickListener() {
                recordMood(5, entity)
                entity = initUI()
            }
            binding.rate4.setOnClickListener() {
                recordMood(4, entity)
                entity = initUI()
            }
            binding.rate3.setOnClickListener() {
                recordMood(3, entity)
                entity = initUI()
            }
            binding.rate2.setOnClickListener() {
                recordMood(2, entity)
                entity = initUI()
            }
            binding.rate1.setOnClickListener() {
                recordMood(1, entity)
                entity = initUI()
            }
        }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

fun recordMood(value: Int, themeEntity: ThemeEntity){
        viewModel.record(value, themeEntity, getMoodTrackRepoObject())
    }

    fun initUI(): ThemeEntity{

        if(viewModel.checkIfCanContinue(viewModel.presentPosition)){
        val entity = viewModel.getPositionValue(viewModel.presentPosition)
        binding.Question.text = entity.question
        viewModel.presentPosition += 1
        return entity}
        else
            requireActivity().supportFragmentManager.popBackStack()
        return ThemeEntity(0,"Default", "Default"
        )
    }

    fun getMoodTrackRepoObject(): MoodRepository {
        val dao = MoodDatabase.getInstance(requireContext()).moodDaoObject
        return MoodRepository(dao)
    }

    fun getThemeRepoObject(): ThemeRepository {
        val dao = ThemeDatabase.getInstance(requireContext()).themeDaoObject
        return ThemeRepository(dao)
    }

    fun redirectToEditThemeFragment(){
        val navController = this.findNavController()
        navController.navigate(R.id.action_moodTrack_to_editThemeFragment)
    }
}