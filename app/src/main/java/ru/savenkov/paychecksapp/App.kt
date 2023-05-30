package ru.savenkov.paychecksapp

import android.app.Application
import ru.savenkov.paychecksapp.room.AppDatabase

class App: Application() {
    val database by lazy {
        AppDatabase.getInstance(applicationContext)
    }
}