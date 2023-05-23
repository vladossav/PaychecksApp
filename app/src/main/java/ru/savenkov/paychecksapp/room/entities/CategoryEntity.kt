package ru.savenkov.paychecksapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    val code: Int,
    val name: String
)