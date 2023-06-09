package ru.savenkov.paychecksapp.presentation.screens.check


import androidx.lifecycle.LiveData
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
import java.text.SimpleDateFormat
import java.util.*

class CheckViewModel(private val repository: CheckRepository): ViewModel() {
    private val _checkAll = MutableLiveData<CheckAll>()
    val checkAll: LiveData<CheckAll> = _checkAll

    val checkSavedState = MutableLiveData<CheckSavedState>(CheckSavedState.NOT_SAVED)
    val checkName = MutableLiveData<String>()
    val checkCategory = MutableLiveData<String?>(null)
    private val checkAllFromApi = MutableLiveData<CheckItem>()

    fun saveCheck() = viewModelScope.launch(Dispatchers.IO) {
        val name = if (checkName.value == null) getDefaultCheckName()
        else checkName.value!!
        repository.saveCheck(checkAllFromApi.value!!, name, checkCategory.value)
        checkSavedState.postValue(CheckSavedState.SAVED)
    }

    fun getCheckFromMock() {
        checkAllFromApi.value = data
        _checkAll.value = Converter.fullCheckToView(data)
        checkName.value = getDefaultCheckName()
    }

    fun updateCheckName() = viewModelScope.launch(Dispatchers.IO) {

    }

    fun updateCheckCategory() = viewModelScope.launch(Dispatchers.IO) {

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
            checkName.postValue(getDefaultCheckName())
            _checkAll.postValue(check)
            isSuccess = true
        }
        return isSuccess
    }

    fun makeSaveDeleteAction() {
        if (checkSavedState.value == CheckSavedState.NOT_SAVED) {
            saveCheck()
        }
        else {
            removeCheck()
        }
    }

    fun getCheckById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val check = repository.getCheckById(id)
        _checkAll.postValue(check!!)
        checkCategory.postValue(check.checkInfo.category)
        checkName.postValue(check.checkInfo.name)
    }

    fun removeCheck() = viewModelScope.launch(Dispatchers.IO) {
        repository.removeCheckById(_checkAll.value!!.checkInfo.id)
        checkSavedState.postValue(CheckSavedState.NOT_SAVED)
    }

    private fun getDefaultCheckName(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())
        return "Чек от $currentDateAndTime"
    }
}