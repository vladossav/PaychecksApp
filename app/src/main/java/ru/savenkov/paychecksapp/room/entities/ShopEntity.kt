package ru.savenkov.paychecksapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop")
data class ShopEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val user: String,
    val userInn: String,
    val region: String,
    val address: String
)
