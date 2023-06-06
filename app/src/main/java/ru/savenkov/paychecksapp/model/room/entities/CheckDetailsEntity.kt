package ru.savenkov.paychecksapp.model.room.entities

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
    var checkId: Long,
    val requestNumber: Int, //номер чека
    val operationType: Int, //Тип операции
    val operator: String, //кассир
    val shiftNumber: Int, //Смена
    val fiscalDocumentNumber: Int, //ФД
    val fiscalDriveNumber: String, //ФН
    val fiscalSign: String, //ФП
    val kktRegId: String, //Рег № ККТ
    val numberKkt: String, //ЗН ККТ

    val region: String, //код региона
    val retailPlace: String, //Название магазина
    val retailPlaceAddress: String, //Адрес магазина
    val user: String, //Организация
    val userInn: String //ИНН Организации
)
