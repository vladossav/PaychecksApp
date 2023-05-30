package ru.savenkov.paychecksapp

import android.app.Application
import ru.savenkov.paychecksapp.model.repository.CheckRepository
import ru.savenkov.paychecksapp.model.room.AppDatabase

class App: Application() {
    val database by lazy {
        AppDatabase.getInstance(applicationContext)
    }
    val repository by lazy {
        CheckRepository(database)
    }
}