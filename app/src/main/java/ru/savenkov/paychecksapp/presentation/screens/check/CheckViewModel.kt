package ru.savenkov.paychecksapp.presentation.screens.check


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.model.repository.CheckRepository
import ru.savenkov.paychecksapp.model.room.entities.CheckAllInfoTuple
import ru.savenkov.paychecksapp.presentation.model.CheckAll

class CheckViewModel(private val repository: CheckRepository): ViewModel() {
    private var checkItem: CheckItem? = null
    val checkAllInfoTuple = MutableLiveData<CheckAll>()

    fun insertCheck(checkItem: CheckItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCheck(checkItem)
        }
    }

    fun getCheck(qrRaw: String) {
        viewModelScope.launch(Dispatchers.IO) {
            checkItem = repository.getCheckFromApi(qrRaw)
        }
    }

    fun getAllCheckById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val check = repository.getAllCheckInfoById(id)
        checkAllInfoTuple.postValue(check!!)
    }

}