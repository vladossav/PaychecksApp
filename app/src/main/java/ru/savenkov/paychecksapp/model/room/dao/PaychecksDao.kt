package ru.savenkov.paychecksapp.model.room.dao

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
    suspend fun getCheckWithCategoryList(category: String): List<CheckEntity>

    @Transaction
    @Query("SELECT * " +
    "FROM 'check' c " +
    "WHERE c.id = :id")
    suspend fun getAllCheckInfoById(id: Long): CheckAllInfoTuple

    @Query("SELECT cat.name, COUNT(c.category) AS count\n" +
            "FROM category cat\n" +
            "LEFT JOIN `check` c ON cat.name = c.category\n" +
            "GROUP BY cat.name\n" +
            "ORDER BY count DESC\n" +
            "LIMIT 5;")
    suspend fun getCategoryCountList(): List<CategoryCountTuple>

    @Query("SELECT * FROM goods\n" +
            "ORDER BY price DESC;")
    suspend fun getAllGoodsListByDesc(): List<GoodEntity>

    @Query("SELECT * FROM goods\n" +
            "ORDER BY price ASC;")
    suspend fun getAllGoodsListByAsc(): List<GoodEntity>

    @Query("SELECT COUNT(c.id) " +
            "FROM 'check' c")
    suspend fun getCheckCount(): Int

    @Query("SELECT COUNT(g.id) " +
            "FROM goods g")
    suspend fun getGoodsCount(): Int
}