package ru.savenkov.paychecksapp.presentation.screens.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.model.StatisticAdapterItem
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class StatisticsViewModel(private val repository: CheckRepository) : ViewModel() {

    val statisticList = MutableLiveData<List<StatisticAdapterItem>>()

    fun getStatisticsList(goodsSortType: SortType) = viewModelScope.launch(Dispatchers.IO) {
        val statisticsItem = repository.getStatisticsItem()
        val goodsList = when(goodsSortType) {
            SortType.BY_ASC -> repository.getAllGoodsListByAsc()
            SortType.BY_DESC -> repository.getAllGoodsListByDesc()
        }
        val list = ArrayList<StatisticAdapterItem>()
        list.add(statisticsItem)
        list.addAll(goodsList)
        statisticList.postValue(list)
    }

}