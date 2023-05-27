package ru.savenkov.paychecksapp.room.entities

import androidx.room.Embedded

data class CheckAllInfoTuple(
    @Embedded val categoryEntity: CategoryEntity?,
    @Embedded val checkEntity: CheckEntity,
    @Embedded val checkDetailsEntity: CheckDetailsEntity,

)