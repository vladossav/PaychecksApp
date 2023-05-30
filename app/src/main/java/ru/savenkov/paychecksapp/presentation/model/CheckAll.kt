package ru.savenkov.paychecksapp.presentation.model

data class CheckAll(
    val checkInfo: CheckInfo,
    val checkGoods: List<CheckGood>
)