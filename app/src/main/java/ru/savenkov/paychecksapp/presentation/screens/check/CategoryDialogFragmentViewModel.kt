package ru.savenkov.paychecksapp.presentation.screens.check

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class CategoryDialogFragmentViewModel(private val repository: CheckRepository): ViewModel() {
    val categoryList = repository.categoryList.asLiveData()

    /*fun saveCategory(category: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveCategory(category)
    }*/

}