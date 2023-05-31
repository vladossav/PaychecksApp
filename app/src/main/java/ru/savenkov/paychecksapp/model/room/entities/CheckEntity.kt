package ru.savenkov.paychecksapp.model.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

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
    val dateTime: String,
    val category: String?,
    val totalSum: Int
)
