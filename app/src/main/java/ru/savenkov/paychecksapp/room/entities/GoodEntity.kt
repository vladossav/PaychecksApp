package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "good",
    foreignKeys = [ForeignKey(
        entity = CheckEntity::class,
        childColumns = ["check_id"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class GoodEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo("check_id")
    val checkId: Long,
    val name: String,
    val price: Int,
    val quantity: Float,
    val sum: Int
)