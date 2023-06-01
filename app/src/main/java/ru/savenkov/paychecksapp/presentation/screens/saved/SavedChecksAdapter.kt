package ru.savenkov.paychecksapp.presentation.screens.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.util.RecyclerDiffUtil

class SavedChecksAdapter(): RecyclerView.Adapter<SavedCheckViewHolder>() {
    var checkList: List<Check> = emptyList()
        set(value) {
            val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
            field = value
            difResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCheckViewHolder =
        SavedCheckViewHolder(parent)

    override fun onBindViewHolder(holder: SavedCheckViewHolder, position: Int) {
        holder.bind(position, checkList[position])
    }

    override fun getItemCount(): Int = checkList.size
}

class SavedCheckViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.saved_check_item, parent, false)
) {

    fun bind(number: Int, item: Check) {
        val num = number + 1
        itemView.findViewById<TextView>(R.id.name).text = num.toString()
        itemView.findViewById<TextView>(R.id.date_time).text = item.dateTime
        itemView.findViewById<TextView>(R.id.category).text = item.category
        itemView.findViewById<TextView>(R.id.total_sum).text = item.totalSum
    }
}