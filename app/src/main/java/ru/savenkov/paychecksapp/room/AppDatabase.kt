package ru.savenkov.paychecksapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.savenkov.paychecksapp.room.entities.*

@Database(
    version = 1,
    entities = [CheckEntity::class, CheckDetailsEntity::class,
        GoodEntity::class, ShopEntity::class, CategoryEntity::class], exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPaychecksDao(): PaychecksDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paychecks"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}