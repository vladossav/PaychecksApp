package ru.savenkov.paychecksapp.presentation.model

data class CheckInfo(
    val id: Long,
    val name: String,
    val dateTime: String,
    val category: String?,
    val totalSum: String, //руб

    val requestNumber: String, //номер чека
    val operationType: String, //Тип операции
    val operator: String, //кассир
    val shiftNumber: String, //Смена
    val fiscalDocumentNumber: String, //ФД
    val fiscalDriveNumber: String, //ФН
    val fiscalSign: String, //ФП
    val kktRegId: String, //Рег № ККТ
    val numberKkt: String, //ЗН ККТ

    val region: String, //код региона
    val retailPlace: String, //Название магазина
    val retailPlaceAddress: String, //Адрес магазина
    val user: String, //Организация
    val userInn: String //ИНН Организации
    ) : CheckAdapterItem