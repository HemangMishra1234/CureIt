package com.example.finflow.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemBinding

class RecyclerViewAdapter(private  val appList: List<DebitApp>,
                          private val clickListener:(DebitApp)-> Unit):RecyclerView.Adapter<MyDebitAppViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDebitAppViewHolder
        {
        var inflator = LayoutInflater.from(parent.context)
        val cardView: CardItemBinding = DataBindingUtil.inflate(inflator,
            R.layout.card_item,parent, false)
        return MyDebitAppViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: MyDebitAppViewHolder, position: Int) {
        holder.bind(appList[position],clickListener)
    }
}

class MyDebitAppViewHolder(val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(debitApp: DebitApp, clickListener:(DebitApp)-> Unit){
        binding.outputName.text = debitApp.name
        binding.ouputDesc.text = debitApp.desc
        binding.outputRate.text = debitApp.rate.toString()
        binding.printFreq.text = debitApp.freq.toString()
        binding.listDebitApp.setOnClickListener(){
            clickListener(debitApp)
        }
    }
}