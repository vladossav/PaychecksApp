package ru.savenkov.paychecksapp.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository

class SavedViewModel(private val repository: CheckRepository) : ViewModel() {
    val checksList = repository.checkList.asLiveData()

}