package ru.savenkov.paychecksapp.presentation.repository

import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.presentation.model.StatisticsItem
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.model.CheckAll
import ru.savenkov.paychecksapp.presentation.model.CheckGood

interface CheckRepository {
    suspend fun getCheckById(id: Long): CheckAll?
    suspend fun getCheckFromApi(qrRaw: String): CheckItem?
    suspend fun saveCheck(checkItem: CheckItem, name: String, category: String?)
    suspend fun removeCheckById(id: Long)

    //saved
    suspend fun getCheckWithCategory(category: String): List<Check>
    suspend fun getCheckList(): List<Check>
    suspend fun getCheckListByPeriod(startDate: String, endDate: String): List<Check>

    val categoryList: Flow<List<String>>
    suspend fun saveCategory(category: String)

    //statistics
    suspend fun getStatisticsItem(): StatisticsItem
    suspend fun getAllGoodsListByDesc(): List<CheckGood>
    suspend fun getAllGoodsListByAsc(): List<CheckGood>
}