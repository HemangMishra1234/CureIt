package com.example.finflow.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemHabitsBinding
import com.example.finflow.databinding.CardItemTransactionBinding
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity


class HabitsRVAdapter(private val list: List<HabitsEntity>,
                           private val clickListener: (HabitsEntity) -> Unit): RecyclerView.Adapter<HabitsViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {

        var inflator = LayoutInflater.from(parent.context)
        var cardView: CardItemHabitsBinding = DataBindingUtil.inflate(
            inflator, R.layout.card_item_habits, parent,false
        )
        return HabitsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }
}

class HabitsViewHolder(val binding: CardItemHabitsBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(habitsEntity: HabitsEntity, clickListener:(HabitsEntity) -> Unit){
        binding.outputId.text = habitsEntity.id.toString()
        binding.outputDomain.text = habitsEntity.name
        binding.freqDecrement.text = habitsEntity.fDecrement.toString()
        binding.outputRateIncrement.text = habitsEntity.rateIncrement.toString()
        binding.printRateDecrement.text = habitsEntity.rateDecrement.toString()
        binding.freqIncrement.text = habitsEntity.fIncrement.toString()
        binding.listHabitCardConstraint.setOnClickListener{
            clickListener(habitsEntity)
        }
    }
}