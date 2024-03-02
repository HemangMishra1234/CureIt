package com.example.finflow.moodStatistics

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.finflow.R
import com.example.finflow.databinding.FragmentMoodTrackBinding

class MoodTrack : Fragment() {

    companion object {
        fun newInstance() = MoodTrack()
    }

    private lateinit var viewModel: MoodTrackViewModel
    private lateinit var binding: FragmentMoodTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mood_track,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

}