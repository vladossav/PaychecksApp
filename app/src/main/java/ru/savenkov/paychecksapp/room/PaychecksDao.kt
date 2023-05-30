package ru.savenkov.paychecksapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.savenkov.paychecksapp.room.entities.*

@Dao
interface PaychecksDao {

    @Insert
    suspend fun insertCheck(checkEntity: CheckEntity): Long

    @Insert
    suspend fun insertCheckDetails(checkDetailsEntity: CheckDetailsEntity)

    @Insert
    suspend fun insertCategory(categoryEntity: CategoryEntity): Long

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

    @Transaction
    @Query("SELECT * " +
    "FROM 'check' c, check_details cd, goods g " +
    "WHERE c.id = cd.checkId AND c.id = g.checkId AND c.id = :id")
    suspend fun getAllInfoById(id: Long): CheckAllInfoTuple
   /* @Query("SELECT bin_number FROM recent ORDER BY last_visit DESC")
    fun getRecentList(): Flow<MutableList<String>>*/
}