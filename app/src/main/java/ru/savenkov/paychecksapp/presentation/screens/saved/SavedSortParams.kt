package ru.savenkov.paychecksapp.presentation.screens.saved

data class SavedSortParams(
    var category: String? = null,
    var periodFrom: String = "0",
    var periodTill: String = "9",
    var sumFrom: String = "0",
    var sumTill: String = "10000000"
)
