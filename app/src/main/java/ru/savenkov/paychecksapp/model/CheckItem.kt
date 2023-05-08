package ru.savenkov.paychecksapp.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckItem(
    @Json(name = "code")
    val code: Int = 0,
    @Json(name = "data")
    val data: Data = Data(),
    @Json(name = "first")
    val first: Int = 0
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "json")
        val jsonObj: JsonObj = JsonObj()
    ) {
        @JsonClass(generateAdapter = true)
        data class JsonObj(
            val appliedTaxationType: Int = 0,
            val cashTotalSum: Int = 0,
            val checkingLabeledProdResult: Int = 0,
            val code: Int = 0,
            val creditSum: Int = 0,
            val dateTime: String = "",
            val ecashTotalSum: Int = 0,
            val fiscalDocumentFormatVer: Int = 0,
            val fiscalDocumentNumber: Int = 0,
            val fiscalDriveNumber: String = "",
            val fiscalSign: Int = 0,
            val fnsUrl: String = "",
            val items: List<Item> = listOf(),
            val kktRegId: String = "",
            val messageFiscalSign: Double = 0.0,
            val metadata: Metadata = Metadata(),
            val nds10: Int = 0,
            val nds18: Int = 0,
            val numberKkt: String = "",
            val operationType: Int = 0,
            val `operator`: String = "",
            val prepaidSum: Int = 0,
            val provisionSum: Int = 0,
            val redefineMask: Int = 0,
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
                val checkingProdInformationResult: Int? = 0,
                val itemsQuantityMeasure: Int = 0,
                val labelCodeProcesMode: Int? = 0,
                val name: String = "",
                val nds: Int = 0,
                val ndsSum: Int = 0,
                val paymentType: Int = 0,
                val price: Int = 0,
                val productCodeNew: ProductCodeNew? = ProductCodeNew(),
                val productType: Int = 0,
                val quantity: Int = 0,
                val sum: Int = 0
            ) {
                @JsonClass(generateAdapter = true)
                data class ProductCodeNew(
                    val gs1m: Gs1m = Gs1m()
                ) {
                    @JsonClass(generateAdapter = true)
                    data class Gs1m(
                        val gtin: String = "",
                        val productIdType: Int = 0,
                        val rawProductCode: String = "",
                        val sernum: String = ""
                    )
                }
            }

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