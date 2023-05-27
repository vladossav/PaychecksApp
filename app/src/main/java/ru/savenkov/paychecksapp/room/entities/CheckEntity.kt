package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        childColumns = ["categoryCode"],
        parentColumns = ["code"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ShopEntity::class,
            childColumns = ["shopId"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: String,
    val categoryCode: Int,
    val shopId: Int,
    val totalSum: Int
)
