package ru.savenkov.paychecksapp.converter

import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.model.room.entities.CheckAllInfoTuple
import ru.savenkov.paychecksapp.model.room.entities.CheckDetailsEntity
import ru.savenkov.paychecksapp.model.room.entities.CheckEntity
import ru.savenkov.paychecksapp.model.room.entities.GoodEntity
import ru.savenkov.paychecksapp.presentation.model.CheckGood
import ru.savenkov.paychecksapp.presentation.model.CheckInfo

object Converter {
    fun toDatabase(checkItem: CheckItem): CheckAllInfoTuple = toCheckAllInfo(checkItem)

    fun toUser(checkItem: CheckItem) {}

    fun goodsToUser(checkAllInfoTuple: CheckAllInfoTuple): List<CheckGood> = checkAllInfoTuple.goods.map {
        CheckGood(
            it.checkId,
            it.id,
            it.name,
            toRubleToString(it.price),
            it.quantity.toString(),
            toRubleToString(it.sum)
        )
    }

    fun checkInfoToUser(checkAllInfo: CheckAllInfoTuple): CheckInfo =
        CheckInfo(
            checkAllInfo.check.id,
            checkAllInfo.check.dateTime.replace("T".toRegex()," "),
            "",
            toRubleToString(checkAllInfo.check.totalSum),
            checkAllInfo.details.requestNumber.toString(),
            getOperationFromCode(checkAllInfo.details.operationType),
            checkAllInfo.details.operator,
            checkAllInfo.details.shiftNumber.toString(),
            checkAllInfo.details.fiscalDocumentNumber.toString(),
            checkAllInfo.details.fiscalDriveNumber,
            checkAllInfo.details.fiscalSign.toString(),
            checkAllInfo.details.kktRegId,
            checkAllInfo.details.numberKkt,
            checkAllInfo.details.region,
            checkAllInfo.details.retailPlace,
            checkAllInfo.details.retailPlaceAddress,
            checkAllInfo.details.user,
            checkAllInfo.details.userInn
        )


    fun toRubleToString(kopeck: Int): String {
        val ruble: Double = kopeck.toDouble() / 100
        return ruble.toString()
    }

    fun getOperationFromCode(code: Int)  =
        when(code) {
            1 -> "Приход"
            2 -> "Возврат прихода"
            3 -> "Расход"
            4 -> "Возврат расхода"
            else -> ""
        }

    private fun toCheckEntity(checkItem: CheckItem): CheckEntity {
        val item = checkItem.data.jsonObj
        return CheckEntity(
            0,
            item.dateTime,
            null,
            item.totalSum
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
            item.userInn,
        )
    }

    private fun toGoodEntityList(checkItem: CheckItem): List<GoodEntity> =
        checkItem.data.jsonObj.items.map {
            GoodEntity(0, 0,it.name, it.price, it.quantity, it.sum)
        }

    private fun toCheckAllInfo(checkItem: CheckItem): CheckAllInfoTuple =
        CheckAllInfoTuple(
            toCheckEntity(checkItem),
            toCheckDetailsEntity(checkItem),
            toGoodEntityList(checkItem)
        )
}