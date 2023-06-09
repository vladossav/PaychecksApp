package ru.savenkov.paychecksapp.presentation.screens.saved

import android.util.Log
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
    var sortSavedSortParams = SavedSortParams()

    fun getChecksWithCategory(category: String) = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckWithCategory(category)
        checksList.postValue(list)
    }

    fun getCheckList() = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckList()
        checksList.postValue(list)
    }

    fun getCheckListBySortParams() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Room", "Filter: $sortSavedSortParams")
        val list = repository.getCheckListByParams(sortSavedSortParams.category,
        sortSavedSortParams.periodFrom, sortSavedSortParams.periodTill,
        sortSavedSortParams.sumFrom, sortSavedSortParams.sumTill)
        checksList.postValue(list)
    }



}