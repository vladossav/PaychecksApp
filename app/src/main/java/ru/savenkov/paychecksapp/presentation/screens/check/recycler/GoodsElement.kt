package ru.savenkov.paychecksapp.presentation.screens.check.recycler

import ru.savenkov.paychecksapp.model.room.entities.GoodEntity

data class GoodsElement(
    val goodEntity: GoodEntity
) : RecyclerElement
