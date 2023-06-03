package ru.savenkov.paychecksapp.model.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.model.room.entities.*


@Dao
interface PaychecksDao {

    @Insert
    suspend fun insertCheck(checkEntity: CheckEntity): Long

    @Insert
    suspend fun insertCheckDetails(checkDetailsEntity: CheckDetailsEntity)

    @Insert
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Insert
    suspend fun insertGoodsList(goodEntity: List<GoodEntity>)

    @Transaction
    suspend fun insertAllCheckInfo(check: CheckAllInfoTuple) {
        val id = insertCheck(check.check)
        check.details.checkId = id
        check.goods.map { good ->
            good.checkId = id
        }
        insertCheckDetails(check.details)
        insertGoodsList(check.goods)
    }

    @Query("SELECT * FROM 'check' c " +
            "WHERE c.id = :checkId")
    fun getCheckById(checkId: Long): CheckEntity

    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM 'check'")
    suspend fun getCheckList(): List<CheckEntity>

    @Query("SELECT * FROM 'check' c WHERE c.category = :category")
    suspend fun getCheckListWithCategory(category: String): List<CheckEntity>

    @Transaction
    @Query("SELECT * " +
    "FROM 'check' c " +
    "WHERE c.id = :id")
    suspend fun getAllCheckInfoById(id: Long): CheckAllInfoTuple
   /* @Query("SELECT bin_number FROM recent ORDER BY last_visit DESC")
    fun getRecentList(): Flow<MutableList<String>>*/
}