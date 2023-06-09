package ru.savenkov.paychecksapp.presentation.repository

import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.presentation.model.StatisticsItem
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.model.CheckAll
import ru.savenkov.paychecksapp.presentation.model.CheckGood

interface CheckRepository {
    //check
    suspend fun getCheckById(id: Long): CheckAll?
    suspend fun getCheckFromApi(qrRaw: String): CheckItem?
    suspend fun saveCheck(qrRaw: String, checkItem: CheckItem, name: String, category: String?, loadedAt: String)
    suspend fun removeCheckById(id: Long)
    suspend fun updateCheckName(checkId: Long, newName: String)
    suspend fun updateCheckCategory(checkId: Long, category: String?)

    //saved
    suspend fun getCheckListByParams(category: String?, startDate: String, endDate: String, startSum: String, endSum: String): List<Check>
    suspend fun getCheckListBySearch(search: String): List<Check>
    suspend fun getGoodsListBySearch(search: String): List<CheckGood>

    val categoryList: Flow<List<String>>
    suspend fun saveCategory(category: String)

    //statistics
    suspend fun getStatisticsItemByPeriod(startDate: String, endDate: String): StatisticsItem
    suspend fun getAllGoodsListByPeriodByDesc(startDate: String, endDate: String): List<CheckGood>
    suspend fun getAllGoodsListByPeriodByAsc(startDate: String, endDate: String): List<CheckGood>
}