package ru.savenkov.paychecksapp.converter

import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.model.room.entities.*
import ru.savenkov.paychecksapp.presentation.model.*
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    fun toDatabase(
        qrRaw: String, checkItem: CheckItem, name: String, category: String? = "",
        loadedAt: String
    ): CheckAllInfoTuple =
        CheckAllInfoTuple(
            toCheckEntity(qrRaw, checkItem, name, category, loadedAt),
            toCheckDetailsEntity(checkItem),
            toGoodEntityList(checkItem)
        )

    fun toView(checksEntityList: List<CheckEntity>): List<Check> = checksEntityList.map { check ->
        Check(
            check.id,
            check.name,
            check.dateTime,
            check.category,
            toRubleToString(check.totalSum)
        )
    }

    fun toCategoryCountList(checkAmount: Int, goodsAmount: Int,
    categoryCountList: List<CategoryCountTuple>, checkTotalSum: Long): StatisticsItem =
        StatisticsItem(
            checkAmount,
            goodsAmount,
            categoryCountList.associate { it.category to it.count },
            checkTotalSum
        )

    fun categoryToView(categoryEntityList: List<CategoryEntity>): List<String> =
        categoryEntityList.map { it.name }

    fun fullCheckToView(qrRaw: String, checkItem: CheckItem): CheckAll {
        val goodsList = checkItem.data.jsonObj.items.map {
            CheckGood(
                0,
                0,
                it.name,
                toRubleToString(it.price),
                it.quantity.toString(),
                toRubleToString(it.sum)
            )
        }
        val item = checkItem.data.jsonObj
        val checkDetails = CheckInfo(
            qrRaw,
            0,
            " ",
            item.dateTime.replace("T".toRegex()," "),
            "",
            toRubleToString(item.totalSum),
            item.requestNumber.toString(),
            getOperationFromCode(item.operationType),
            item.operator,
            item.shiftNumber.toString(),
            item.fiscalDocumentNumber.toString(),
            item.fiscalDriveNumber,
            item.fiscalSign,
            item.kktRegId,
            item.numberKkt,
            item.region,
            item.retailPlace,
            item.retailPlaceAddress,
            item.user,
            item.userInn
        )
        return CheckAll(
            checkDetails, goodsList
        )
    }

    fun goodsToView(checkAllInfoTuple: CheckAllInfoTuple): List<CheckGood> = checkAllInfoTuple.goods.map {
        CheckGood(
            it.checkId,
            it.id,
            it.name,
            toRubleToString(it.price),
            it.quantity.toString(),
            toRubleToString(it.sum)
        )
    }

    fun goodsToView(checkGoods: List<GoodEntity>): List<CheckGood> = checkGoods.map {
        CheckGood(
            it.checkId,
            it.id,
            it.name,
            toRubleToString(it.price),
            it.quantity.toString(),
            toRubleToString(it.sum)
        )
    }

    fun checkInfoToView(checkAllInfo: CheckAllInfoTuple): CheckInfo =
        CheckInfo(
            checkAllInfo.check.qrRaw,
            checkAllInfo.check.id,
            checkAllInfo.check.name,
            checkAllInfo.check.dateTime.replace("T".toRegex()," "),
            checkAllInfo.check.category,
            toRubleToString(checkAllInfo.check.totalSum),
            checkAllInfo.details.requestNumber.toString(),
            getOperationFromCode(checkAllInfo.details.operationType),
            checkAllInfo.details.operator,
            checkAllInfo.details.shiftNumber.toString(),
            checkAllInfo.details.fiscalDocumentNumber.toString(),
            checkAllInfo.details.fiscalDriveNumber,
            checkAllInfo.details.fiscalSign,
            checkAllInfo.details.kktRegId,
            checkAllInfo.details.numberKkt,
            checkAllInfo.details.region,
            checkAllInfo.details.retailPlace,
            checkAllInfo.details.retailPlaceAddress,
            checkAllInfo.details.user,
            checkAllInfo.details.userInn
        )


    private fun toRubleToString(kopeck: Int): String {
        val ruble: Double = kopeck.toDouble() / 100
        return ruble.toString()
    }

    private fun getOperationFromCode(code: Int)  =
        when(code) {
            1 -> "Приход"
            2 -> "Возврат прихода"
            3 -> "Расход"
            4 -> "Возврат расхода"
            else -> ""
        }

    private fun toCheckEntity(qrRaw:String, checkItem: CheckItem, name: String,
                              category: String?, loadedAt: String): CheckEntity {
        val item = checkItem.data.jsonObj
        return CheckEntity(
            0,
            name,
            item.dateTime,
            category,
            item.totalSum,
            qrRaw,
            loadedAt
        )
    }

    private fun toCheckDetailsEntity(checkItem: CheckItem) : CheckDetailsEntity {
        val item = checkItem.data.jsonObj
        return CheckDetailsEntity(
            0,
            item.requestNumber,
            item.operationType,
            item.operator,
            item.shiftNumber,
            item.fiscalDocumentNumber,
            item.fiscalDriveNumber,
            item.fiscalSign,
            item.kktRegId,
            item.numberKkt,
            item.region,
            item.retailPlace,
            item.retailPlaceAddress,
            item.user,
            item.userInn
        )
    }

    private fun toGoodEntityList(checkItem: CheckItem): List<GoodEntity> =
        checkItem.data.jsonObj.items.map {
            GoodEntity(0, 0,it.name, it.price, it.quantity, it.sum)
        }

    fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }
}