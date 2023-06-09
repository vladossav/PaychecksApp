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

    override suspend fun getCheckListBySearch(search: String): List<Check> {
        val list = dao.searchByChecksName(search)
        return Converter.toView(list)
    }

    override suspend fun getGoodsListBySearch(search: String): List<CheckGood> {
        val list = dao.searchByGoodsName(search)
        return Converter.goodsToView(list)
    }

    override val categoryList: Flow<List<String>> = dao.getCategoryList().map {
        Converter.categoryToView(it)
    }

    override suspend fun removeCheckById(id: Long) {
        try {
            dao.removeCheckById(id)
        } catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
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


    override suspend fun getAllGoodsListByPeriodByDesc(startDate: String, endDate: String): List<CheckGood> {
        val listEntity = dao.getAllGoodsListByPeriodByDesc(startDate, endDate)
        return  Converter.goodsToView(listEntity)
    }

    override suspend fun getAllGoodsListByPeriodByAsc(startDate: String, endDate: String): List<CheckGood> {
        val listEntity = dao.getAllGoodsListByPeriodByAsc(startDate, endDate)
        return Converter.goodsToView(listEntity)
    }

    override suspend fun saveCheck(checkItem: CheckItem, name: String, category: String?) {
        try {
            if(category != null) saveCategory(category)
            val entity = Converter.toDatabase(checkItem, name, category)
            dao.insertAllCheckInfo(entity)
        } catch (err: Exception) {
            Log.e("Room",err.message.toString())
        }
    }

    override suspend fun getCheckListByParams(
        category: String?,
        startDate: String,
        endDate: String,
        startSum: String,
        endSum: String
    ): List<Check> {
        val list = if (category == null) dao.getCheckListByPeriodByTotalSum(startDate, endDate, startSum, endSum)
        else dao.getCheckListByPeriodByTotalSumWithCategory(category, startDate, endDate, startSum, endSum)
        return Converter.toView(list)
    }

    override suspend fun updateCheckName(checkId: Long, newName: String) {
        dao.updateCheckName(checkId, newName)
    }

    override suspend fun updateCheckCategory(checkId: Long, category: String?) {
        dao.updateCheckCategory(checkId, category)
    }

    override suspend fun getStatisticsItemByPeriod(startDate: String, endDate: String): StatisticsItem {
        val categoryCountList = dao.getCategoryCountListByPeriod(startDate, endDate)
        val checkAmount = dao.getCheckCountByPeriod(startDate, endDate)
        val goodsAmount = dao.getGoodsCountByPeriod(startDate, endDate)
        var checkTotalSum = dao.getCheckTotalSumByPeriod(startDate, endDate)
        if (checkTotalSum == null) checkTotalSum = 0
        return Converter.toCategoryCountList(checkAmount, goodsAmount, categoryCountList, checkTotalSum)
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
            return null
        } catch (e: HttpException) {
            Log.e("CheckApi", "HttpException: ${e.message}")
            return null
        } catch (e: Exception) {
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