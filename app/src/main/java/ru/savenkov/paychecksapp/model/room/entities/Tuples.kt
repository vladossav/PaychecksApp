package ru.savenkov.paychecksapp.model.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation


data class CheckAllInfoTuple(
    @Embedded
    val check: CheckEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "checkId"
    )
    val details: CheckDetailsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "checkId"
    )
    val goods: List<GoodEntity>
)

data class CategoryCountTuple(
    @ColumnInfo("name") val category: String,
    val count: Int
)

