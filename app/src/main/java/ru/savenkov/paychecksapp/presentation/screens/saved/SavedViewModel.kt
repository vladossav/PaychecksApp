package ru.savenkov.paychecksapp.presentation.screens.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.model.SavedAdapterItem
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class SavedViewModel(private val repository: CheckRepository) : ViewModel() {
    var checksList = MutableLiveData<List<SavedAdapterItem>>()
    val categoryList = repository.categoryList.asLiveData()
    var sortSavedSortParams = SavedSortParams()

    fun getCheckListBySortParams() = viewModelScope.launch(Dispatchers.IO) {
        val list = repository.getCheckListByParams(sortSavedSortParams.category,
        sortSavedSortParams.periodFrom, sortSavedSortParams.periodTill,
        sortSavedSortParams.sumFrom, sortSavedSortParams.sumTill)
        checksList.postValue(list)
    }

    fun getCheckListBySearch(search: String) = viewModelScope.launch(Dispatchers.IO) {
        val listChecks = repository.getCheckListBySearch(search)
        val listGoods = repository.getGoodsListBySearch(search)
        val list = listOf(listChecks, listGoods).flatten()
        checksList.postValue(list)
    }

}