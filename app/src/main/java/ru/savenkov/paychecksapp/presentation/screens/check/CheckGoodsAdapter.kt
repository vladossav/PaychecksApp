package ru.savenkov.paychecksapp.presentation.screens.check

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.savenkov.paychecksapp.presentation.util.RecyclerDiffUtil
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.presentation.model.CheckAdapterItem
import ru.savenkov.paychecksapp.presentation.model.CheckGood
import ru.savenkov.paychecksapp.presentation.model.CheckInfo

class CheckGoodsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var checkList: List<CheckAdapterItem> = emptyList()
    set(value) {
        val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(field, value))
        field = value
        difResult.dispatchUpdatesTo(this)
    }

    private companion object {
        const val CHECK_GOOD_VIEW_TYPE = 0
        const val CHECK_INFO_TOP_VIEW_TYPE = 1
        const val CHECK_INFO_BOTTOM_VIEW_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            CHECK_GOOD_VIEW_TYPE -> GoodViewHolder(parent)
            CHECK_INFO_TOP_VIEW_TYPE -> CheckInfoViewHolder(parent, R.layout.check_top_info_item)
            CHECK_INFO_BOTTOM_VIEW_TYPE -> CheckInfoViewHolder(parent, R.layout.check_bottom_info_item)
            else -> throw IllegalArgumentException("Wrong viewType: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GoodViewHolder -> holder.bind(position, checkList[position] as CheckGood)
            is CheckInfoViewHolder -> holder.bind(checkList[position] as CheckInfo)
        }
    }

    override fun getItemCount(): Int = checkList.size

    override fun getItemViewType(position: Int): Int =
        if (checkList[position] is CheckGood) {
            CHECK_GOOD_VIEW_TYPE
        } else if (position == 0) CHECK_INFO_TOP_VIEW_TYPE
        else  CHECK_INFO_BOTTOM_VIEW_TYPE

}

class GoodViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.check_goods_item, parent, false)
) {

    fun bind(number: Int, item: CheckGood) {
        itemView.findViewById<TextView>(R.id.goodNumber).text = number.toString()
        itemView.findViewById<TextView>(R.id.goodName).text = item.name
        itemView.findViewById<TextView>(R.id.goodPrice).text = item.price
        itemView.findViewById<TextView>(R.id.goodQuantity).text = item.quantity
        itemView.findViewById<TextView>(R.id.goodSum).text = item.sum
    }
}

class CheckInfoViewHolder(parent: ViewGroup, private val layoutId: Int): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
) {

    fun bind(item: CheckInfo) {
        if (layoutId == R.layout.check_top_info_item) {
            val inn = "${itemView.resources.getString(R.string.check_shop_userInn)} ${item.userInn}"
            val num = "${itemView.resources.getString(R.string.check_number)} ${item.requestNumber}"
            val shiftNum = "${itemView.resources.getString(R.string.check_shift_number)} ${item.shiftNumber}"

            itemView.findViewById<TextView>(R.id.shopUser).text = item.user
            itemView.findViewById<TextView>(R.id.shopName).text = item.retailPlace
            itemView.findViewById<TextView>(R.id.shopAddress).text = item.retailPlaceAddress
            itemView.findViewById<TextView>(R.id.shopUserInn).text = inn
            itemView.findViewById<TextView>(R.id.dateTime).text = item.dateTime
            itemView.findViewById<TextView>(R.id.checkNumber).text = num
            itemView.findViewById<TextView>(R.id.checkShiftNumber).text = shiftNum
            itemView.findViewById<TextView>(R.id.checkType).text = item.operationType
        }
        if (layoutId == R.layout.check_bottom_info_item) {
            val kktRegId = "${itemView.resources.getString(R.string.check_reg_id_kkt)} ${item.kktRegId}"
            val numberKkt = "${itemView.resources.getString(R.string.check_number_kkt)} ${item.numberKkt}"
            val fn = "${itemView.resources.getString(R.string.check_fn)} ${item.fiscalDriveNumber}"
            val fd = "${itemView.resources.getString(R.string.check_fd)} ${item.fiscalDocumentNumber}"
            val fpd = "${itemView.resources.getString(R.string.check_fpd)} ${item.fiscalSign}"

            itemView.findViewById<TextView>(R.id.totalSum).text = item.totalSum
            itemView.findViewById<TextView>(R.id.regIdKkt).text = kktRegId
            itemView.findViewById<TextView>(R.id.numberKkt).text = numberKkt
            itemView.findViewById<TextView>(R.id.fiscalDriveNumber).text = fn
            itemView.findViewById<TextView>(R.id.fiscalDocumentNumber).text = fd
            itemView.findViewById<TextView>(R.id.fiscalSign).text = fpd
        }
    }
}