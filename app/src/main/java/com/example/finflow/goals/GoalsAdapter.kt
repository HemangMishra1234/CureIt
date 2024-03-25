package com.example.finflow.goals


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemBinding
import com.example.finflow.databinding.CardItemGoalsBinding
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.room.DebitApp

class GoalsAdapter(private  val appList: List<GoalEntity>,
                          private val clickListener:(GoalEntity)-> Unit):RecyclerView.Adapter<MyGoalsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGoalsViewHolder
    {
        var inflator = LayoutInflater.from(parent.context)
        val cardView: CardItemGoalsBinding = DataBindingUtil.inflate(inflator,
            R.layout.card_item_goals,parent, false)
        return MyGoalsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: MyGoalsViewHolder, position: Int) {
        holder.bind(appList[position],clickListener)
    }
}

class MyGoalsViewHolder(val binding: CardItemGoalsBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(entity: GoalEntity, clickListener:(GoalEntity)-> Unit){
        binding.outputDomain.text = entity.domain
        binding.outputRateGoals.text = Logic().formatFloatToTwoDecimalPlaces(entity.rate)
        binding.presPerc.text = Logic().formatFloatToTwoDecimalPlaces(entity.presentPercent)
        binding.ouputTimeSpent.text = Logic().convertMinutesToHoursString(entity.presentTimeSpent)
        binding.printExpectperc.text = entity.expectedPercent.toString()
        binding.listGoalCardConstraint.setOnClickListener(){
            clickListener(entity)
        }
    }
}