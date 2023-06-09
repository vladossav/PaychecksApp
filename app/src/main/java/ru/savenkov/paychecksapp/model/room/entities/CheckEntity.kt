package ru.savenkov.paychecksapp.model.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check",
    foreignKeys = [
        ForeignKey(
        entity = CategoryEntity::class,
        childColumns = ["category"],
        parentColumns = ["name"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class CheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dateTime: String,
    val category: String?,
    val totalSum: Int,
    val qrRaw: String,
    val loadedDateTime: String
)
