package ru.savenkov.paychecksapp.presentation.repository

import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.model.CheckAll

interface CheckRepository {
    suspend fun getCheckById(id: Long): CheckAll?
    suspend fun getCheckFromApi(qrRaw: String): CheckItem?
    suspend fun saveCheck(checkItem: CheckItem, category: String?)
    val checkList: Flow<List<Check>>

    val categoryList: Flow<List<String>>
    suspend fun saveCategory(category: String)
}