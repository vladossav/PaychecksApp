package ru.savenkov.paychecksapp.model.network.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.savenkov.paychecksapp.model.room.entities.CheckAllInfoTuple
import ru.savenkov.paychecksapp.model.room.entities.CheckDetailsEntity
import ru.savenkov.paychecksapp.model.room.entities.CheckEntity
import ru.savenkov.paychecksapp.model.room.entities.GoodEntity

@JsonClass(generateAdapter = true)
data class CheckItem(
    val code: Int = 0,
    //КОД ОТВЕТА:
    //0 - чек некорректен,
    //1 - данные чека получены (успешный запрос),
    //2 - данные чека пока не получены,
    //3 - превышено кол-во запросов,
    //4 - ожидание перед повторным запросом,
    //5 - прочее (данные не получены)
    val data: Data = Data()
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "json")
        val jsonObj: JsonObj = JsonObj()
    ) {
        @JsonClass(generateAdapter = true)
        data class JsonObj(
            val requestNumber: Int = 0, //Чек №
            val dateTime: String = "",
            val operator: String = "", //кассир
            val shiftNumber: Int = 0, //Смена
            val operationType: Int = 0, //Тип операции
            //1 - Приход
            //2 - Возврат прихода
            //3 - Расход
            //4 - Возврат расхода
            val items: List<Item> = listOf(), //товары
            val totalSum: Int = 0, //ИТОГО

            val metadata: Metadata = Metadata(),
            val kktRegId: String = "", //Рег № ККТ
            val numberKkt: String = "", //ЗН ККТ
            val fiscalDocumentNumber: Int = 0, //ФД
            val fiscalDriveNumber: String = "", //ФН
            val fiscalSign: Int = 0, //ФП

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

            @JsonClass(generateAdapter = true)
            data class Metadata(
                val address: String = "",
                val id: Long = 0,
                val ofdId: String = "",
                val receiveDate: String = "",
                val subtype: String = ""
            )
        }
    }
}