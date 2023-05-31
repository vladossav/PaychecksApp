package ru.savenkov.paychecksapp.presentation.screens.check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.model.CheckCategory
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class CategoryDialogFragmentViewModel(private val repository: CheckRepository): ViewModel() {
    val categoryList = MutableLiveData<List<CheckCategory>>()
    val currentCategory = MutableLiveData<Int?>(null)

    fun getCategoryList() = viewModelScope.launch {
        //category.postValue(null)
    }

}