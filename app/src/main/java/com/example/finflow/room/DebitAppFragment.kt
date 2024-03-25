package com.example.finflow.room

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.R
import com.example.finflow.databinding.ActivityMainBinding
import com.example.finflow.databinding.FragmentDebitAppBinding
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.goals.GoalEntity

class DebitAppFragment : Fragment() {

    lateinit var binding: FragmentDebitAppBinding
    lateinit var viewModel: DebitAppsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debit_app,container,false)

        //Room
        val dao = DebitAppDB.getInstance(requireContext()).userDAO
        val repository = DebitAppsRepository(dao)
        val factory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(DebitAppsViewModel::class.java)
        viewModel.contextOfMain = requireContext()
        viewModel.contextOfApplicaton = requireContext()


        binding.lifecycleOwner = this

        initRecyclerView()

        binding.saveButton.setOnClickListener(){
            dialogueBoxOpenerAdd()
        }

        binding.timePickerForDebitApps.maxValue = 90

        clickListeners()

        return binding.root
    }

    fun clickListeners(){
        binding.sourceEditorAdd.setOnClickListener(){
            if(viewModel.isUpdateOrDelete)
                dialogueBoxUpdateOpener(viewModel.userToUpdateOrDelete)
            else
                dialogueBoxOpenerAdd()
        }

        binding.creditButton.setOnClickListener(){
            viewModel.use(binding.timePickerForDebitApps.value.toLong())
        }
    }
    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        DisplayAppsList()

    }

    private fun DisplayAppsList(){
        viewModel.debitApps.observe(requireActivity(), Observer {
            binding.recyclerView.adapter = RecyclerViewAdapter(
                it
            ) { selectedItem: DebitApp -> listItemClicked(selectedItem) }
        })
    }

    private fun listItemClicked(selectedItem: DebitApp){
        // Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
//        dialogueBoxUpdateOpener(selectedItem)
        binding.sourceEditorAdd.setImageResource(R.drawable.baseline_edit_24)

    }

    private fun dialogueBoxUpdateOpener(selectedItem: DebitApp){
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.dialgoue_box_update)

        val closeButton = customDialog.findViewById<Button>(R.id.btnClose_dialogue)
        val deleteButton = customDialog.findViewById<Button>(R.id.delete_app_update_dialogue)
        val updateButton = customDialog.findViewById<Button>(R.id.update_dialogue_app)
        val name = customDialog.findViewById<EditText>(R.id.appName_dial)
        val description = customDialog.findViewById<EditText>(R.id.description_dial)

        name.setText(selectedItem.name)

        deleteButton.setOnClickListener(){
            viewModel.delete(selectedItem)
        }

        closeButton.setOnClickListener { // Close the dialog when the close button is clicked
            customDialog.dismiss()
        }
        Log.i("dialogue", "$selectedItem")

        val numberPicker = customDialog.findViewById<NumberPicker>(R.id.numberPicker_rate)
        numberPicker.value = if(selectedItem.rate?.toInt() == null) 0 else selectedItem.rate!!.toInt()
        val maxValue = 10000
        val minValue = 0
        val step = 500
        val displayedValues = Array(maxValue - minValue + 1) { i -> ((i + minValue) * step).toString() }

        numberPicker.minValue = minValue
        numberPicker.maxValue = maxValue
        numberPicker.displayedValues = displayedValues
        customDialog.show()

        updateButton.setOnClickListener(){
            selectedItem.name = name.text.toString()
            selectedItem.desc = description.text.toString()
            selectedItem.rate = numberPicker.value.toString().toFloat()*step
            viewModel.update(selectedItem)
        }
    }

    private fun dialogueBoxOpenerAdd(){
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.dialogue_box_add_debit)

        val closeButton = customDialog.findViewById<Button>(R.id.btnClose_dialogue_addapp)
        val saveButton = customDialog.findViewById<Button>(R.id.add_dialogue_app)
        val name = customDialog.findViewById<EditText>(R.id.appName_dial_add)
        val description = customDialog.findViewById<TextView>(R.id.description_dial_des)

        closeButton.setOnClickListener { // Close the dialog when the close button is clicked
            customDialog.dismiss()
        }

        val numberPicker = customDialog.findViewById<NumberPicker>(R.id.numberPicker_rate_add)
        val maxValue = 10000
        val minValue = 0
        val step = 500
        val displayedValues = Array(maxValue - minValue + 1) { i -> ((i + minValue) * step).toString() }

        numberPicker.minValue = minValue
        numberPicker.maxValue = maxValue
        numberPicker.displayedValues = displayedValues
        customDialog.show()

        saveButton.setOnClickListener(){
            if(name.text != null && numberPicker.value.toString().toInt() > 0 && description != null){
                val name = name.text.toString()
                val rate = numberPicker.value.toString().toFloat() * step
                val description = description.text.toString()
                viewModel.insert(DebitApp(0,name,description,rate,0))
                customDialog.dismiss()
            }else{
                Toast.makeText(activity,"Name and description must be valid and Rate should be greater than 0",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}