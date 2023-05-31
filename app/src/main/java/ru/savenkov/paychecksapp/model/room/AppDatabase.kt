package ru.savenkov.paychecksapp.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.savenkov.paychecksapp.model.room.entities.CategoryEntity
import ru.savenkov.paychecksapp.model.room.entities.CheckDetailsEntity
import ru.savenkov.paychecksapp.model.room.entities.CheckEntity
import ru.savenkov.paychecksapp.model.room.entities.GoodEntity

@Database(
    version = 1,
    entities = [CheckEntity::class, CheckDetailsEntity::class,
         CategoryEntity::class, GoodEntity::class], exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPaychecksDao(): PaychecksDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase?= null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paychecks"
                )//.createFromAsset("paychecks.db")
                    .fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}