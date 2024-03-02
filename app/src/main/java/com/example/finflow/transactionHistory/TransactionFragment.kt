package com.example.finflow.transactionHistory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finflow.R
import com.example.finflow.databinding.FragmentTransactionBinding
import javax.xml.transform.TransformerFactory

class TransactionFragment() : Fragment() {

    lateinit var binding: FragmentTransactionBinding
    lateinit var viewModel: TransactionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Use DataBindingUtil to inflate the layout
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_transaction,container,false
        )

        //Room
        val dao = TransactionDatabase.getInstance(requireContext().applicationContext).transDAOObject
        val repository = TransactionRepository(dao)
        val factory = TransactionViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(TransactionViewModel::class.java)

       // viewModel = ViewModelProvider(this,factory).get(TransactionViewModel::class.java)
        viewModel.contextOfMain = requireActivity()
        viewModel.contextOfApplication = requireContext().applicationContext

        binding.transaction = viewModel

        binding.lifecycleOwner = this

        initRecyclerView()

        return binding.root
    }


    private fun initRecyclerView(){
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        DisplayTransactionList()
    }

    private fun DisplayTransactionList(){
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            binding.recyclerView2.adapter = TransactionRVAdapter(
                it, {selectedItem: TransEntity -> listItemClicked(selectedItem)}
            )
        })
    }

    private fun listItemClicked(selectedItem: TransEntity){
        viewModel.initUpdateOrDelete(transEntity = selectedItem)
    }

}