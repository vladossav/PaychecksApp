package ru.savenkov.paychecksapp.presentation.model

data class StatisticsItem(
    val checkAmount: Int,
    val goodsAmount: Int,
    val categoryCountMap: Map<String, Int>
): StatisticAdapterItem
