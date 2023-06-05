package ru.savenkov.paychecksapp.presentation.model

data class CheckGood(
    val checkId: Long,
    val id: Long,
    val name: String,
    val price: String,
    val quantity: String,
    val sum: String
) : CheckAdapterItem, StatisticAdapterItem
