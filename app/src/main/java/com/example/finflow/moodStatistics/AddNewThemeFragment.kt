package com.example.finflow.moodStatistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.finflow.R
import com.example.finflow.databinding.FragmentAddNewThemeBinding



class AddNewThemeFragment : Fragment() {

    lateinit var viewModel: EditThemeViewModel
    lateinit var binding: FragmentAddNewThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_theme, container, false)
        val dao = ThemeDatabase.getInstance(requireContext()).themeDaoObject
        val repository = ThemeRepository(dao)
        val factory = EditThemeViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[EditThemeViewModel::class.java]


        var type: String? = ""
        var ques: String? = ""
        var id: Int? = 0
        requireArguments().putInt("deb",0)
        val size = requireArguments().size()
        if(size >= 3) {
            type = requireArguments().getString("Name")
            ques = requireArguments().getString("Question")
            id = requireArguments().getInt("ID")
            if (id == null)
                id = 0
        }
        binding.apply {
            question.setText(ques)
            theme.setText(type)

            continueButton.setOnClickListener(){
                if(question.text == null || theme.text == null )
                    Toast.makeText(activity,"Enter required Data", LENGTH_SHORT).show()
                else{
                   if(id == 0){
                       viewModel.insert(ThemeEntity(0,question.text.toString(), title = theme.text.toString()))
                   }else
                       viewModel.update(ThemeEntity(id!!,question.text.toString(), title = theme.text.toString()))
                    val navController = it.findNavController()
                    navController.navigate(R.id.action_addNewThemeFragment_to_editThemeFragment)
                }
            }
        }

        binding.backButton.setOnClickListener(){
            val navController = it.findNavController()
            navController.navigate(R.id.action_addNewThemeFragment_to_editThemeFragment)
        }
        binding.deleteButton.setOnClickListener(){
            if(id != 0&& id != null && type != null && ques != null)
            viewModel.delete(ThemeEntity(id,type,ques))
            val navController = it.findNavController()
            navController.navigate(R.id.action_addNewThemeFragment_to_editThemeFragment)
        }
        return binding.root
    }




}