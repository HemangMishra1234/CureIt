package com.example.finflow.goals


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.R
import com.example.finflow.databinding.FragmentGoalsBinding
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GoalsFragment : Fragment() {
    lateinit var binding: FragmentGoalsBinding
    lateinit var viewModel: GoalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals,container,false)

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
        clickListeners()

//        viewModel.goals.observe(requireActivity(), Observer {
//            CoroutineScope(Dispatchers.Main).launch {
//                viewModel.updateAllRatesAndPercent()
//            }
//        })

        binding.timePickerForGoals.maxValue = 1000
        return binding.root

    }

    fun clickListeners(){
        binding.dashboardAddEditButton.setOnClickListener() {
            if (viewModel.isUpdateOrDelete) {
                dialogueBoxOpener(viewModel.userToUpdateOrDelete)
            } else {
                dialogueBoxOpenerAdd()
            }
        }

        binding.creditButton.setOnClickListener(){
            use()
        }
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

            val t = viewModel.updateTotalTimeSpentInMin()
            binding.totalTimeSpent.text = "Time Spent: ${Logic().convertMinutesToHoursString(t)}"
            viewModel.updateTotalPercentTillNow()
        }
        )
    }



    private fun listItemClicked(selectedItem: GoalEntity){
        //Toast.makeText(this, "Selected Item is ${selectedItem.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(selectedItem)
        binding.sourceEditorAdd.setImageResource(R.drawable.baseline_edit_24)
        binding.recoveryTime.text = viewModel.getRecoveryTime(selectedItem)
    }

    private fun dialogueBoxOpener(selectedItem: GoalEntity){
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.dialogue_box_goals)

        val closeButton = customDialog.findViewById<Button>(R.id.btnClose_dialogue)
        val deleteButton = customDialog.findViewById<Button>(R.id.delete_dialogue)
        val updateButton = customDialog.findViewById<Button>(R.id.update_diaglogue)
        val name = customDialog.findViewById<EditText>(R.id.name_diag)

        name.setText(selectedItem.domain)

        deleteButton.setOnClickListener(){
            viewModel.delete(selectedItem)
        }

        closeButton.setOnClickListener { // Close the dialog when the close button is clicked
            customDialog.dismiss()
        }
        Log.i("dialogue", "$selectedItem")

        val numberPicker = customDialog.findViewById<NumberPicker>(R.id.numberPicker)
        numberPicker.value = selectedItem.expectedPercent.toInt()
        numberPicker.maxValue=100
        customDialog.show()

        updateButton.setOnClickListener(){
            selectedItem.domain = name.text.toString()
            selectedItem.expectedPercent = numberPicker.value.toString().toFloat()
            viewModel.update(entity = selectedItem)
        }
    }

    private fun dialogueBoxOpenerAdd(){
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.dialogue_box_goals_add)

        val closeButton = customDialog.findViewById<Button>(R.id.btnClose_add_dialogue)
       val saveButton = customDialog.findViewById<Button>(R.id.save_add_diaglogue)
        val name = customDialog.findViewById<EditText>(R.id.name_domain_add_dialogue)

        closeButton.setOnClickListener { // Close the dialog when the close button is clicked
            customDialog.dismiss()
        }

        val numberPicker = customDialog.findViewById<NumberPicker>(R.id.numberPicker2)
         numberPicker.maxValue=100
        customDialog.show()

        saveButton.setOnClickListener(){
            if(name.text != null && numberPicker.value.toString().toInt() > 0){
                val domain = name.text.toString()
                val expectedPercentage = numberPicker.value.toString().toInt()
                val rateNew = Logic().rateCalculator(0.0F, expectedPercentage.toFloat())
                viewModel.insert(GoalEntity(0,domain,expectedPercentage.toFloat(),0.0F,0,rateNew))
                customDialog.dismiss()
            }else{
                Toast.makeText(activity,"Domain name must be valid and Expected Percentage should be greater than 0",Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun use() {
        val inputTime = binding.timePickerForGoals.value.toLong()
        val entity = viewModel.userToUpdateOrDelete
        if (viewModel.totalPercentageTillNow == 100.0F) {
            if (viewModel.isUpdateOrDelete && inputTime != 0L) {
                //Note the time
                val cal = Calculations(requireContext())
                val amt = cal.creditCalculations(inputTime, entity.rate)
                //Adding the transaction history
                val dao = TransactionDatabase.getInstance(requireContext()).transDAOObject
                val repository = TransactionRepository(dao)
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insert(
                        TransEntity(
                            0,
                            entity.domain,
                            entity.rate,
                            inputTime,
                            Logic().currentDateAndTime(),
                            true,
                            amt
                        )
                    )
                }
                //update the UI
                viewModel.userToUpdateOrDelete.presentTimeSpent += inputTime
                viewModel.updateSingleItem(viewModel.userToUpdateOrDelete)
                Log.e("update", "enitiy after adding time ${viewModel.userToUpdateOrDelete}")
                viewModel.updateAllRatesAndPercent()
                viewModel.makeInvisible()

                //Updating the functions

                val logic = LevelLogic(requireContext())
                if (logic.checkIfGoalIsCompleted(viewModel.goals.value!!)) {
                    logic.saveNewLevelInSharedPref(logic.getLevelInSharedPref() + 1)
                    viewModel.resetForNextLevel()
                }

                //Checking for next level
            } else if (inputTime > 0) {
                Toast.makeText(requireContext(), "Select an item first", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Enter Time first", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Sum must be 100, at present it is ${viewModel.totalPercentageTillNow}", Toast.LENGTH_SHORT).show()
        }
    }

}