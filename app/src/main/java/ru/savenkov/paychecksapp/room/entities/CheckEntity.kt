package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        childColumns = ["category_code"],
        parentColumns = ["code"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ShopEntity::class,
            childColumns = ["shop_id"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CheckEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "category_code")
    val codeCategory: Int,
    @ColumnInfo(name = "shop_id")
    val shopId: Int,
    @ColumnInfo(name = "total_sum")
    val totalSum: Int
)
