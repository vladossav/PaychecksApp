package ru.savenkov.paychecksapp.model.repository

import android.util.Log
import retrofit2.HttpException
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.model.network.ProverkachekaApi
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.model.room.AppDatabase
import ru.savenkov.paychecksapp.model.room.entities.CheckAllInfoTuple
import ru.savenkov.paychecksapp.presentation.model.CheckAll
import java.io.IOException

class CheckRepository(db: AppDatabase) {
    private val dao = db.getPaychecksDao()
    private val api = ProverkachekaApi.create()

    suspend fun insertCheck(checkItem: CheckItem) {

        val entity = Converter.toDatabase(checkItem)

        try {
            val id = dao.insertAllCheckInfo(entity)
            //val id = dao.insertCheck(checkEntity2)
            Log.d("Room", id.toString())
        } catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
    }

    suspend fun getAllCheckInfoById(id: Long): CheckAll? {
        var check: CheckAll? = null
        try {
            val checkEntity = dao.getAllCheckInfoById(id)
            check = CheckAll(
                Converter.checkInfoToUser(checkEntity),
                Converter.goodsToUser(checkEntity)
            )
        }
        catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
        return check
    }

    suspend fun getCheckFromApi(qrRaw: String): CheckItem? {
        Log.d("CheckApi","input qrraw: $qrRaw")
        var checkItem: CheckItem? = null

        val response = try {
            api.getCheck(qrRaw)
        } catch (e: IOException) {
            Log.e("CheckApi", "IOException: ${e.message}")
            //loading.postValue(false)
            return null
        } catch (e: HttpException) {
            Log.e("CheckApi", "HttpException: ${e.message}")
            //loading.postValue(false)
            return null
        } catch (e: Exception) {
            //loading.postValue(false)
            Log.e("CheckApi",e.message.toString())
            Log.e("CheckApi","Error: Some troubles on server! Try later!")
            return null
        }

        if (response.isSuccessful && response.body() != null) {
            Log.d("CheckApi", response.body().toString())
            Log.d("CheckApi", response.message())
            checkItem = response.body()
        } else {
            Log.e("CheckApi", "Response not successful")
        }
        return checkItem
    }
}