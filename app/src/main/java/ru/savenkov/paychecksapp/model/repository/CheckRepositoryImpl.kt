package ru.savenkov.paychecksapp.model.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.model.network.ProverkachekaApi
import ru.savenkov.paychecksapp.model.network.data.CheckItem
import ru.savenkov.paychecksapp.model.room.AppDatabase
import ru.savenkov.paychecksapp.model.room.entities.CategoryEntity
import ru.savenkov.paychecksapp.presentation.model.StatisticsItem
import ru.savenkov.paychecksapp.presentation.model.Check
import ru.savenkov.paychecksapp.presentation.model.CheckAll
import ru.savenkov.paychecksapp.presentation.model.CheckGood
import ru.savenkov.paychecksapp.presentation.repository.CheckRepository
import java.io.IOException

class CheckRepositoryImpl(db: AppDatabase): CheckRepository {
    private val dao = db.getPaychecksDao()
    private val api = ProverkachekaApi.create()

    override val categoryList: Flow<List<String>> = dao.getCategoryList().map {
        Converter.categoryToView(it)
    }

    override suspend fun getCheckWithCategory(category: String): List<Check>  {
        Log.d("Room", "getCheckWithCategory")
        val listEntity = dao.getCheckWithCategoryList(category)
        val list = Converter.toView(listEntity)
        return list
    }

    override suspend fun getCheckList(): List<Check> {
        val listEntity = dao.getCheckList()
        return Converter.toView(listEntity)
    }

    override suspend fun getCheckListByPeriod(startDate: String, endDate: String): List<Check> {
        val listEntity = dao.getCheckListByPeriod(startDate, endDate)
        return Converter.toView(listEntity)
    }

    override suspend fun saveCategory(category: String) {
        try {
            val entity = CategoryEntity(category)
            dao.insertCategory(entity)
        }
        catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
    }

    override suspend fun getAllGoodsListByDesc(): List<CheckGood> {
        val listEntity = dao.getAllGoodsListByDesc()
        val list = Converter.goodsToView(listEntity)
        return list
    }


    override suspend fun getAllGoodsListByAsc(): List<CheckGood> {
        val listEntity = dao.getAllGoodsListByAsc()
        return Converter.goodsToView(listEntity)
    }

    override suspend fun saveCheck(checkItem: CheckItem, category: String?) {
        try {
            if(category != null) saveCategory(category)
            val entity = Converter.toDatabase(checkItem, category)
            dao.insertAllCheckInfo(entity)
        } catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
    }

    override suspend fun getStatisticsItem(): StatisticsItem {
        val listEntity = dao.getCategoryCountList()
        val checkAmount = dao.getCheckCount()
        val goodsAmount = dao.getGoodsCount()
        return Converter.toCategoryCountList(checkAmount, goodsAmount, listEntity)
    }

    override suspend fun getCheckById(id: Long): CheckAll? {
        var check: CheckAll? = null
        try {
            val checkEntity = dao.getAllCheckInfoById(id)
            check = CheckAll(
                Converter.checkInfoToView(checkEntity),
                Converter.goodsToView(checkEntity)
            )
        }
        catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
        return check
    }

    override suspend fun getCheckFromApi(qrRaw: String): CheckItem? {
        Log.d("CheckApi","input qrraw: $qrRaw")
        var checkItem: CheckItem? = null

        val response = try {
            api.getCheck(qrRaw)
        } catch (e: IOException) {
            Log.e("CheckApi", "IOException: ${e.message}")
            //loading.postValue(false)
            return null
        } catch (e: HttpException) {
            Log.e("CheckApi", "HttpException: ${e.message}")
            //loading.postValue(false)
            return null
        } catch (e: Exception) {
            //loading.postValue(false)
            Log.e("CheckApi",e.message.toString())
            Log.e("CheckApi","Error: Some troubles on server! Try later!")
            return null
        }

        if (response.isSuccessful && response.body() != null) {
            Log.d("CheckApi", response.body().toString())
            Log.d("CheckApi", response.message())
            checkItem = response.body()
        } else {
            Log.e("CheckApi", "Response not successful")
        }
        return checkItem
    }
}