package ru.savenkov.paychecksapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "check_details",
    foreignKeys = [ForeignKey(
        entity = CheckEntity::class,
        childColumns = ["checkId"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CheckDetailsEntity(
    @PrimaryKey
    val checkId: Long,
    val requestNumber: Int, //номер чека
    val operationType: Int, //Тип операции
    val operator: String, //кассир
    val shiftNumber: Int, //Смена
    val fiscalDocumentNumber: Int, //ФД
    val fiscalDriveNumber: String, //ФН
    val fiscalSign: Int, //ФП
    val kktRegId: String, //Рег № ККТ
    val numberKkt: String, //ЗН ККТ
)
