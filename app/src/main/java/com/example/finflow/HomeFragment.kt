package com.example.finflow

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.example.finflow.databinding.FragmentHomeBinding
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.goals.LevelLogic


class HomeFragment() : Fragment() {


    lateinit var bind: FragmentHomeBinding

    companion object {
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", param1)
            args.putString("ARG_PARAM2", param2)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use DataBindingUtil to inflate the layout
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

         val contextOfMain = requireContext()
        val levelText = "Level ${LevelLogic(contextOfMain).getLevelInSharedPref()}"
        bind.levelDisplay.text = Editable.Factory.getInstance().newEditable(levelText)

       // LevelLogic(contextOfMain).saveNewLevelInSharedPref(0)
        val arr = resources.getStringArray(R.array.level_titles)
        val arr2 = resources.getIntArray(R.array.level_colors)
        Log.e("Level",LevelLogic(contextOfMain).getLevelInSharedPref().toString())
        bind.levelNameDisplay.text = Editable.Factory.getInstance().newEditable(arr[LevelLogic(contextOfMain).getLevelInSharedPref()].toString())
        bind.levelDisplay.setTextColor((arr2[LevelLogic(contextOfMain).getLevelInSharedPref()]))

        bind.nextTargetDisplay.text = Editable.Factory.getInstance().newEditable("Next Target: ${LevelLogic(contextOfMain).getLevelInSharedPref()*50 + 50} hours")




        return bind.root
    }


}