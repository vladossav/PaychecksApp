package ru.savenkov.paychecksapp.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "good",
    foreignKeys = [ForeignKey(
        entity = CheckEntity::class,
        childColumns = ["checkId"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class GoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val checkId: Long,
    val name: String,
    val price: Int,
    val quantity: Float,
    val sum: Int
)