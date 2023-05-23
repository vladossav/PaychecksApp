package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check_details",
    foreignKeys = [ForeignKey(
        entity = CheckEntity::class,
        childColumns = ["check_id"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CheckDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "check_id")
    val checkId: Long,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "category_code")
    val codeCategory: Int,
    @ColumnInfo(name = "shop_id")
    val shopId: Int,
    @ColumnInfo(name = "total_sum")
    val totalSum: Int
)
