package ru.savenkov.paychecksapp.ui.check

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.savenkov.paychecksapp.model.CheckItem
import ru.savenkov.paychecksapp.network.ProverkachekaApi
import java.io.IOException

class CheckViewModel: ViewModel() {

    private val api = ProverkachekaApi.create()

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
            val check: CheckItem = res!!
            Log.d("CheckApi", check.toString())
            Log.d("CheckApi", response.message())
        } else {
            Log.e("CheckApi", "Response not successful")
        }

    }
}