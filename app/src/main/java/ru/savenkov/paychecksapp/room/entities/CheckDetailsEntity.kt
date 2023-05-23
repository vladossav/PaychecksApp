package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check")
data class CheckDetailsEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "category_code")
    val codeCategory: Int,
    val shopId: Int,
    val totalSum: Int
)
