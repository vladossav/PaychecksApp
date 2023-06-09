package ru.savenkov.paychecksapp.presentation.screens.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.model.CheckGood
import ru.savenkov.paychecksapp.presentation.model.SavedAdapterItem
import ru.savenkov.paychecksapp.presentation.util.RecyclerDiffUtil

class SavedChecksAdapter(private val onClick: (Long) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var checkList: List<SavedAdapterItem> = emptyList()
        set(value) {
            val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
            field = value
            difResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            CHECK_GOOD_VIEW_TYPE -> SavedGoodsViewHolder(parent, onClick)
            CHECK_VIEW_TYPE -> SavedCheckViewHolder(parent, onClick)
            else -> throw IllegalArgumentException("Wrong viewType: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedGoodsViewHolder -> holder.bind(checkList[position] as CheckGood)
            is SavedCheckViewHolder -> holder.bind(checkList[position] as Check)
        }
    }

    override fun getItemCount(): Int = checkList.size

    override fun getItemViewType(position: Int): Int =
        if (checkList[position] is Check) CHECK_VIEW_TYPE
        else CHECK_GOOD_VIEW_TYPE

    private companion object {
        const val CHECK_GOOD_VIEW_TYPE = 0
        const val CHECK_VIEW_TYPE = 1
    }
}

class SavedCheckViewHolder(parent: ViewGroup, private val onClick: (Long) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.saved_check_item, parent, false)
) {

    fun bind(check: Check) {
        itemView.findViewById<TextView>(R.id.name).text = check.name
        itemView.findViewById<TextView>(R.id.date_time).text = check.dateTime.replace("T".toRegex()," ")
        itemView.findViewById<TextView>(R.id.category).text = check.category
        itemView.findViewById<TextView>(R.id.total_sum).text = check.totalSum
        itemView.setOnClickListener {
            onClick.invoke(check.id)
        }
    }
}

class SavedGoodsViewHolder(parent: ViewGroup, private val onClick: (Long) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.statistic_good_item, parent, false)
) {

    fun bind(good: CheckGood) {
        itemView.findViewById<TextView>(R.id.goodName).text = good.name
        itemView.findViewById<TextView>(R.id.goodPrice).text = good.price
        itemView.setOnClickListener {
            onClick.invoke(good.checkId)
        }
    }
}