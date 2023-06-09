package ru.savenkov.paychecksapp.presentation.model

data class Check(
    val id: Long,
    val name: String,
    val dateTime: String,
    val category: String?,
    val totalSum: String
): SavedAdapterItem