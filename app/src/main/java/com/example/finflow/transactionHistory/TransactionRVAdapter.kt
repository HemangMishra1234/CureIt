package com.example.finflow.transactionHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemTransactionBinding
import com.example.finflow.debitAppLogic.Logic

class TransactionRVAdapter(private val list: List<TransEntity>,
    private val clickListener: (TransEntity) -> Unit): RecyclerView.Adapter<MyTransactionViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTransactionViewHolder {

        var inflator = LayoutInflater.from(parent.context)
        var cardView: CardItemTransactionBinding = DataBindingUtil.inflate(
            inflator, R.layout.card_item_transaction, parent,false
        )
        return MyTransactionViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyTransactionViewHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }
}

class MyTransactionViewHolder(val binding: CardItemTransactionBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(transEntity: TransEntity, clickListener:(TransEntity) -> Unit){
        binding.cardtransName.text = transEntity.name
        binding.cardTransAmount.text = Logic().formatAmountInCrores(transEntity.amount)
        binding.cardTransType.text = if(transEntity.credit) "Credit" else "Debit"
        binding.cardTransDatetime.text = transEntity.dateTime
        binding.listTransactionsCardConstraint.setOnClickListener(){
            clickListener(transEntity)
        }
    }
}