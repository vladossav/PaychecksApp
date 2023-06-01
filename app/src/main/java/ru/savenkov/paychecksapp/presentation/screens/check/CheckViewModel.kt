package ru.savenkov.paychecksapp.presentation.screens.check


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.data
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.presentation.model.CheckAll
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class CheckViewModel(private val repository: CheckRepository): ViewModel() {
    val checkAll = MutableLiveData<CheckAll>()
    val checkCategory = MutableLiveData<String>(null)
    private val checkAllFromApi = MutableLiveData<CheckItem>()

    fun saveCheck() = viewModelScope.launch(Dispatchers.IO) {
        repository.saveCheck(checkAllFromApi.value!!, checkCategory.value)
    }

    fun getCheckFromMock() {
        checkAllFromApi.value = data
        checkAll.value = Converter.fullCheckToView(data)
    }

    fun getCheck(qrRaw: String?): Boolean {
        var isSuccess = false
        viewModelScope.launch(Dispatchers.IO) {
            if (qrRaw == null || qrRaw == "") {
                isSuccess = false
                return@launch
            }
            val checkItemFromApi = repository.getCheckFromApi(qrRaw)
            checkAllFromApi.postValue(checkItemFromApi!!)
            val check = Converter.fullCheckToView(checkItemFromApi)
            checkAll.postValue(check)
            isSuccess = true
        }
        return isSuccess
    }


    fun getAllCheckById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val check = repository.getCheckById(id)
        checkAll.postValue(check!!)
    }

}