package ru.savenkov.paychecksapp.presentation.screens.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.model.StatisticAdapterItem
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class StatisticsViewModel(private val repository: CheckRepository) : ViewModel() {
    var sortPeriodDate: Pair<String, String> = Pair("0","9")
    var goodsSortType: SortType = SortType.BY_DESC
    val statisticList = MutableLiveData<List<StatisticAdapterItem>>()

    fun getStatisticsList() = viewModelScope.launch(Dispatchers.IO) {
        val statisticsItem = repository.getStatisticsItemByPeriod(sortPeriodDate.first,
        sortPeriodDate.second)
        val goodsList = when (goodsSortType) {
            SortType.BY_ASC -> {
                repository.getAllGoodsListByPeriodByAsc(sortPeriodDate.first,
                    sortPeriodDate.second)
            }
            SortType.BY_DESC -> {
                repository.getAllGoodsListByPeriodByDesc(sortPeriodDate.first,
                    sortPeriodDate.second)
            }
        }
        val list = ArrayList<StatisticAdapterItem>()
        list.add(statisticsItem)
        list.addAll(goodsList)
        statisticList.postValue(list)
    }

}