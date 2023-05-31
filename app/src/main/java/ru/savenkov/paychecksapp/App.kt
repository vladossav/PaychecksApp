package ru.savenkov.paychecksapp

import android.app.Application
import ru.savenkov.paychecksapp.model.repository.CheckRepositoryImpl
import ru.savenkov.paychecksapp.model.room.AppDatabase

class App: Application() {
    private val database by lazy {
        AppDatabase.getInstance(applicationContext)
    }
    val repository by lazy {
        CheckRepositoryImpl(database)
    }
}