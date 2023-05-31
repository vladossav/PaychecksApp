package ru.savenkov.paychecksapp.presentation.repository

import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.presentation.model.CheckAll

interface CheckRepository {
    suspend fun getAllCheckInfoById(id: Long): CheckAll?
    suspend fun getCheckFromApi(qrRaw: String): CheckItem?
    suspend fun insertCheck(checkItem: CheckItem)
}