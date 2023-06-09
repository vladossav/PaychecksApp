package ru.savenkov.paychecksapp.presentation.screens.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.chip.Chip
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.presentation.model.StatisticsItem
import ru.savenkov.paychecksapp.presentation.model.CheckGood
import ru.savenkov.paychecksapp.presentation.model.StatisticAdapterItem
import ru.savenkov.paychecksapp.presentation.util.RecyclerDiffUtil

class StatisticAdapter(private val onSortClick: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var statisticList: List<StatisticAdapterItem> = emptyList()
    set(value) {
        val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
        field = value
        difResult.dispatchUpdatesTo(this)
    }

    private companion object {
        const val STATISTIC_VIEW_TYPE = 0
        const val CHECK_GOOD_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            STATISTIC_VIEW_TYPE -> StatisticViewHolder(parent, onSortClick)
            CHECK_GOOD_VIEW_TYPE -> GoodViewHolder(parent)
            else -> throw IllegalArgumentException("Wrong viewType: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GoodViewHolder -> holder.bind(statisticList[position] as CheckGood)
            is StatisticViewHolder -> holder.bind(statisticList[position] as StatisticsItem)
        }
    }

    override fun getItemCount(): Int = statisticList.size

    override fun getItemViewType(position: Int): Int =
        if (statisticList[position] is CheckGood) {
            CHECK_GOOD_VIEW_TYPE
        } else STATISTIC_VIEW_TYPE

}

class GoodViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.statistic_good_item, parent, false)
) {

    fun bind(item: CheckGood) {
        itemView.findViewById<TextView>(R.id.goodName).text = item.name
        itemView.findViewById<TextView>(R.id.goodPrice).text = item.price
    }
}

class StatisticViewHolder(private val parent: ViewGroup, private val onSortClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.statistic_item, parent, false)
    ) {

    fun bind(item: StatisticsItem) {
        itemView.findViewById<TextView>(R.id.good_count).text = item.goodsAmount.toString()
        itemView.findViewById<TextView>(R.id.check_count).text = item.checkAmount.toString()

        setSortingPopupMenu()
        setPieChart(item)
    }

    private fun setSortingPopupMenu() {
        val sortButton = itemView.findViewById<Chip>(R.id.sort_button)
        sortButton.setOnClickListener {
            val popUpMenu = PopupMenu(parent.context, it)
            popUpMenu.menuInflater.inflate(R.menu.statistics_sort_menu_popup, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener { item ->
                sortButton.text = item.title.toString()
                when(item.itemId) {
                    R.id.sort_asc -> onSortClick.invoke(R.id.sort_asc)
                    R.id.sort_desc -> onSortClick.invoke(R.id.sort_desc)
                }
                true
            }
            popUpMenu.show()
        }
    }

    private fun setPieChart(item: StatisticsItem) {
        val sumStr = "ИТОГО:\n${item.checkTotalSum / 100} руб.\n${item.checkTotalSum % 100} коп."
        val pieChart = itemView.findViewById<PieChart>(R.id.pieChart)
        pieChart.apply {
            description.isEnabled = false
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setDrawCenterText(true)
            centerText = sumStr
            setCenterTextSize(20f)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            legend.isEnabled = true
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
            setEntryLabelTextSize(16f)
            highlightValues(null)
            setUsePercentValues(true)
            invalidate()
        }


        val entries: ArrayList<PieEntry> = ArrayList()
        item.categoryCountMap.forEach { (category, count) ->
            entries.add(PieEntry(count.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "").apply {
            valueTextColor = Color.BLACK
            setDrawIcons(true)
            sliceSpace = 5f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f
            colors = ColorTemplate.COLORFUL_COLORS.asList()
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(18f)
            setValueTypeface(Typeface.DEFAULT_BOLD)
            setValueTextColor(Color.WHITE)
        }
        pieChart.data = data
        pieChart.legend.apply {
            form = Legend.LegendForm.CIRCLE
            orientation = Legend.LegendOrientation.VERTICAL
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            setDrawInside(false)
            xEntrySpace = 20f
            yEntrySpace = 0f
            yOffset = 0f
        }

        for (entry in entries) {
            entry.label = entry.label + " (" + entry.value.toInt() + ")"
        }
    }
}