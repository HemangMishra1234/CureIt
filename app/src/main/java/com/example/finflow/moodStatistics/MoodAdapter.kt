package com.example.finflow.moodStatistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemThemeBinding
import com.example.finflow.generated.callback.OnClickListener

class MoodAdapter(private val themeList: List<ThemeEntity>,
    private val clickListener: (ThemeEntity) -> Unit): RecyclerView.Adapter<MyThemesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyThemesViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val cardView: CardItemThemeBinding = DataBindingUtil.inflate(inflater,
            R.layout.card_item_theme,parent,false
        )
        return MyThemesViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return themeList.size
    }

    override fun onBindViewHolder(holder: MyThemesViewHolder, position: Int) {

        holder.bind(themeList[position], clickListener)
    }
}

class MyThemesViewHolder(val binding: CardItemThemeBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(entity: ThemeEntity, clickListener: (ThemeEntity) -> Unit){
        binding.displayQuestion.text = entity.question
        binding.displayTitle.text = entity.title
        binding.editButtonTheme.setOnClickListener(){
            clickListener(entity)
        }
    }
}