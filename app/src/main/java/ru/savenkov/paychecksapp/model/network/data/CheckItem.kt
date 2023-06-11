package ru.savenkov.paychecksapp.model.network.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CheckItem(
    val data: Data = Data()
): Serializable {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "json") val jsonObj: JsonObj = JsonObj()
    ) {
        @JsonClass(generateAdapter = true)
        data class JsonObj(
            val requestNumber: Int = 0, //Чек №
            val dateTime: String = "",
            val operator: String = "", //кассир
            val shiftNumber: Int = 0, //Смена
            val operationType: Int = 0, //Тип операции
            val items: List<Item> = listOf(), //товары
            val totalSum: Int = 0, //ИТОГО

            val kktRegId: String = "", //Рег № ККТ
            val numberKkt: String = "", //ЗН ККТ
            val fiscalDocumentNumber: Int = 0, //ФД
            val fiscalDriveNumber: String = "", //ФН
            val fiscalSign: String = "", //ФП

            val region: String = "", //код региона
            val retailPlace: String = "", //Название магазина
            val retailPlaceAddress: String = "", //Адрес магазина
            val user: String = "", //Организация
            val userInn: String = "" //ИНН Организации
        ) {
            @JsonClass(generateAdapter = true)
            data class Item(
                val name: String = "",
                val price: Int = 0, //коп.
                val quantity: Float = 0f,
                val sum: Int = 0 //коп.
            )
        }
    }
}