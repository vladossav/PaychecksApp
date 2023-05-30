package ru.savenkov.paychecksapp.model.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.savenkov.paychecksapp.TOKEN
import ru.savenkov.paychecksapp.model.network.data.CheckItem

interface ProverkachekaApi {
    @FormUrlEncoded
    @POST("get")
    suspend fun getCheck(
        @Field("qrraw") qrRaw: String,
        @Field("token") token: String = TOKEN,
    ):  Response<CheckItem>

    companion object {
        private const val BASE_URL = "https://proverkacheka.com/api/v1/check/"


        fun create() : ProverkachekaApi {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ProverkachekaApi::class.java)
        }
    }
}