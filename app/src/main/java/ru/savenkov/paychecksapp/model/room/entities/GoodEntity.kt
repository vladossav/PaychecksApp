package ru.savenkov.paychecksapp.model.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "goods",
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
    var checkId: Long,
    val name: String,
    val price: Int,
    val quantity: Float,
    val sum: Int
)