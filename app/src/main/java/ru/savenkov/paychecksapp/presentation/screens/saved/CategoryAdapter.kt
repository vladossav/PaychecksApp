package ru.savenkov.paychecksapp.presentation.screens.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.presentation.util.RecyclerDiffUtil

class CategoryAdapter(private val onClick: (String) -> Unit): RecyclerView.Adapter<CategoryViewHolder>() {
    var categoryList: ArrayList<String> = arrayListOf()
        set(value) {
            val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
            field = value
            difResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(parent, onClick)

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size
}

class CategoryViewHolder(parent: ViewGroup, private val onClick: (String) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.saved_category_item, parent, false)
)  {

    fun onBind(item: String) {
        val chipItem = itemView.findViewById<Chip>(R.id.category_text)
        chipItem.text = item
        chipItem.setOnClickListener {
            onClick.invoke(item)
        }
    }
}