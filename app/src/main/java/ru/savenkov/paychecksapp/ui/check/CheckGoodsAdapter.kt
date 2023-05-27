package ru.savenkov.paychecksapp.ui.check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.savenkov.homework.util.RecyclerDiffUtil
import ru.savenkov.paychecksapp.R

class CheckGoodsAdapter() : RecyclerView.Adapter<GoodViewHolder>() {
    private var goodsList: List<String> = emptyList()
    set(value) {
        val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
        field = value
        difResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodViewHolder
        = GoodViewHolder(parent)

    override fun onBindViewHolder(holder: GoodViewHolder, position: Int) {
        holder.bind(goodsList[position])
    }

    override fun getItemCount(): Int = goodsList.size

}

class GoodViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.check_goods_item, parent, false)
) {

    fun bind(item: String) {
        itemView.findViewById<TextView>(R.id.goodName)
    }
}