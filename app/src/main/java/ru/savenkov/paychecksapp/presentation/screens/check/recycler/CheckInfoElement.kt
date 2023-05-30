package ru.savenkov.paychecksapp.presentation.screens.check.recycler

import ru.savenkov.paychecksapp.model.room.entities.CheckDetailsEntity
import ru.savenkov.paychecksapp.model.room.entities.CheckEntity

data class CheckInfoElement(
    val checkEntity: CheckEntity,
    val checkDetailsEntity: CheckDetailsEntity
) : RecyclerElement

