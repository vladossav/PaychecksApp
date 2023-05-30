package ru.savenkov.paychecksapp.ui.check

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.savenkov.paychecksapp.data
import ru.savenkov.paychecksapp.network.model.CheckItem
import ru.savenkov.paychecksapp.network.ProverkachekaApi
import ru.savenkov.paychecksapp.room.PaychecksDao
import java.io.IOException

class CheckViewModel(private val dao: PaychecksDao): ViewModel() {
    private lateinit var checkItem: CheckItem
    private val api = ProverkachekaApi.create()

    fun getCheckFromMock() {
        checkItem = data
    }

    fun insertCheck(checkItem: CheckItem) {
        val entity = checkItem.toCheckAllInfo()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = dao.insertAllCheckInfo(entity)
                //val id = dao.insertCheck(checkEntity2)
                Log.d("Room", id.toString())
            } catch (err: Exception) {
                Log.e("Room",err.message.toString())
            }

        }
    }


    fun getCheckFromApi(qrRaw: String) = viewModelScope.launch(Dispatchers.IO) {
        //loading.postValue(true)
        Log.d("CheckApi","input qrraw: $qrRaw")

        val response = try {
            api.getCheck(qrRaw)
        } catch (e: IOException) {
            Log.e("CheckApi", "IOException: ${e.message}")
            //loading.postValue(false)
            return@launch
        } catch (e: HttpException) {
            Log.e("CheckApi", "HttpException: ${e.message}")
            //loading.postValue(false)
            return@launch
        } catch (e: Exception) {
            //loading.postValue(false)
            Log.e("CheckApi",e.message.toString())
            Log.e("CheckApi","Error: Some troubles on server! Try later!")
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            val res = response.body()
            checkItem = res!!
            Log.d("CheckApi", checkItem.toString())
            Log.d("CheckApi", response.message())
        } else {
            Log.e("CheckApi", "Response not successful")
        }

    }
}