package ru.savenkov.paychecksapp.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckItem(
    val code: Int = 0,
    val data: Data = Data()
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "json")
        val jsonObj: JsonObj = JsonObj()
    ) {
        @JsonClass(generateAdapter = true)
        data class JsonObj(
            val code: Int = 0,
            val dateTime: String = "",
            val fiscalDocumentNumber: Int = 0,
            val fiscalDriveNumber: String = "",
            val fiscalSign: Int = 0,
            val items: List<Item> = listOf(),
            val kktRegId: String = "",
            val numberKkt: String = "",
            val metadata: Metadata = Metadata(),
            val operator: String = "",
            val region: String = "",
            val requestNumber: Int = 0,
            val retailPlace: String = "",
            val retailPlaceAddress: String = "",
            val shiftNumber: Int = 0,
            val totalSum: Int = 0,
            val user: String = "",
            val userInn: String = ""
        ) {
            @JsonClass(generateAdapter = true)
            data class Item(
                val name: String = "",
                val price: Int = 0,
                val quantity: Int = 0,
                val sum: Int = 0
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