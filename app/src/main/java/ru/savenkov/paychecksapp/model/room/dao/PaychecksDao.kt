package ru.savenkov.paychecksapp.model.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.model.room.entities.*


@Dao
interface PaychecksDao {

    @Insert
    suspend fun insertCheck(checkEntity: CheckEntity): Long

    @Query("DELETE FROM 'check' WHERE id=:id")
    suspend fun removeCheckById(id: Long)

    @Insert
    suspend fun insertCheckDetails(checkDetailsEntity: CheckDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    @Query("SELECT * FROM `check` c " +
            "WHERE c.dateTime BETWEEN :startDate AND :endDate " +
            "AND c.totalSum BETWEEN :startSum AND :endSum " +
            "AND c.category = :category")
    suspend fun getCheckListByPeriodByTotalSumWithCategory(category: String, startDate: String, endDate: String,
                                               startSum: String, endSum: String): List<CheckEntity>

    @Query("SELECT * FROM `check` c " +
            "WHERE c.dateTime BETWEEN :startDate AND :endDate " +
            "AND c.totalSum BETWEEN :startSum AND :endSum")
    suspend fun getCheckListByPeriodByTotalSum(startDate: String, endDate: String,
                                               startSum: String, endSum: String): List<CheckEntity>

    @Transaction
    @Query("SELECT * FROM 'check' c WHERE c.id = :id")
    suspend fun getAllCheckInfoById(id: Long): CheckAllInfoTuple

    @Query("SELECT cat.name, COUNT(c.category) AS count\n" +
            "FROM category cat\n" +
            "LEFT JOIN `check` c ON cat.name = c.category\n" +
            "WHERE c.dateTime BETWEEN :start AND :end " +
            "GROUP BY cat.name\n" +
            "ORDER BY count DESC\n" +
            "LIMIT 5;")
    suspend fun getCategoryCountListByPeriod(start: String, end: String): List<CategoryCountTuple>

    @Query("SELECT g.id, g.checkId, g.name, g.price, g.quantity, g.sum \n" +
            "FROM goods g\n" +
            "JOIN 'check' c ON g.checkId = c.id \n" +
            "WHERE g.id in (SELECT max(g.id) FROM goods g group by g.name ORDER BY g.price DESC) " +
            "AND c.dateTime BETWEEN :start AND :end \n" +
            "ORDER BY g.price DESC")
    suspend fun getAllGoodsListByPeriodByDesc(start: String, end: String): List<GoodEntity>

    @Query("SELECT g.id, g.checkId, g.name, g.price, g.quantity, g.sum \n" +
            "FROM goods g\n" +
            "JOIN 'check' c ON g.checkId = c.id \n" +
            "WHERE g.id in (SELECT max(g.id) FROM goods g group by g.name ORDER BY g.price DESC) " +
            "AND c.dateTime BETWEEN :start AND :end\n" +
            "ORDER BY g.price ASC")
    suspend fun getAllGoodsListByPeriodByAsc(start: String, end: String): List<GoodEntity>

    @Query("UPDATE 'check' SET name = :newName WHERE id = :checkId")
    suspend fun updateCheckName(checkId: Long, newName: String)

    @Query("UPDATE 'check' SET category = :newCategory WHERE id = :checkId")
    suspend fun updateCheckCategory(checkId: Long, newCategory: String?)

    @Query("SELECT COUNT(c.id) FROM 'check' c WHERE c.dateTime BETWEEN :start AND :end")
    suspend fun getCheckCountByPeriod(start: String, end: String): Int

    @Query("SELECT COUNT(g.id) FROM goods g " +
            "JOIN 'check' c ON g.checkId = c.id \n" +
            "WHERE c.dateTime BETWEEN :start AND :end")
    suspend fun getGoodsCountByPeriod(start: String, end: String): Int

    @Query("SELECT SUM(c.totalSum) FROM 'check' c WHERE c.dateTime BETWEEN :start AND :end")
    suspend fun getCheckTotalSumByPeriod(start: String, end: String): Long?

    @Query("SELECT * FROM goods WHERE name LIKE '%' || :good || '%' ORDER BY price DESC")
    suspend fun searchByGoodsName(good: String): List<GoodEntity>

    @Query("SELECT * FROM 'check' WHERE name LIKE '%' || :checkName || '%'")
    suspend fun searchByChecksName(checkName: String): List<CheckEntity>
}