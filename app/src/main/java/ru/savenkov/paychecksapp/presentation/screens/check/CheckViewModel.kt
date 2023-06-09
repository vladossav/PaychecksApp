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
    var qrCodeRaw: String = "null"


    fun getCheckFromMock() {
        checkAllFromApi.value = data
        _checkAll.value = Converter.fullCheckToView("  ", data)
        checkName.value = getCurrentDateTime()
    }

    fun updateCheckName() = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCheckName(checkAll.value!!.checkInfo.id, checkName.value!!)
    }

    fun updateCheckCategory() = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCheckCategory(checkAll.value!!.checkInfo.id, checkCategory.value)
    }

    fun getCheck(qrRaw: String?): Boolean {

        var isSuccess = false
        viewModelScope.launch(Dispatchers.IO) {
            if (qrRaw == null || qrRaw == "") {
                isSuccess = false
                return@launch
            }
            qrCodeRaw = qrRaw
            val checkItemFromApi = repository.getCheckFromApi(qrRaw)
            checkAllFromApi.postValue(checkItemFromApi!!)
            val check = Converter.fullCheckToView(qrRaw, checkItemFromApi)
            checkName.postValue(check.checkInfo.retailPlace)
            _checkAll.postValue(check)
            isSuccess = true
        }
        return isSuccess
    }

    fun makeSaveDeleteAction() {
        if (checkSavedState.value == CheckSavedState.NOT_SAVED) saveCheck()
        else removeCheck()
    }

    private fun saveCheck() = viewModelScope.launch(Dispatchers.IO) {
        val name = if (checkName.value == null) checkAll.value?.checkInfo?.retailPlace ?: "Чек"
        else checkName.value!!
        repository.saveCheck(qrCodeRaw!!, checkAllFromApi.value!!, name, checkCategory.value, getCurrentDateTime())
        checkSavedState.postValue(CheckSavedState.SAVED)
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

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}