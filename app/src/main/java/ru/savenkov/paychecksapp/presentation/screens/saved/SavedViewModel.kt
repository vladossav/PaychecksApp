package ru.savenkov.paychecksapp.presentation.screens.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository
import java.text.SimpleDateFormat
import java.util.*

class SavedViewModel(private val repository: CheckRepository) : ViewModel() {
    var checksList = MutableLiveData<List<Check>>()
    val categoryList = repository.categoryList.asLiveData()

    fun getChecksWithCategory(category: String) = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckWithCategory(category)
        checksList.postValue(list)
    }

    fun getCheckList() = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckList()
        checksList.postValue(list)
    }

    fun getCheckListByPeriod(startDate: String, endDate: String) = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckListByPeriod(startDate, endDate)
        checksList.postValue(list)
    }



}