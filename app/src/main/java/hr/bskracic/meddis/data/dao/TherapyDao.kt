package hr.bskracic.meddis.data.dao

import androidx.room.*
import hr.bskracic.meddis.data.model.Therapy
import kotlinx.coroutines.flow.Flow

@Dao
interface TherapyDao {
    @Query("SELECT * FROM therapies")
    fun getAll(): Flow<List<Therapy>>

    @Query("SELECT * FROM therapies WHERE id IN (:therapyIds)")
    fun getByIds(therapyIds: IntArray): Flow<List<Therapy>>

    @Update
    fun update(vararg therapies: Therapy)

    @Insert
    fun insert(vararg therapies: Therapy)

    @Delete
    fun delete(therapy: Therapy)

}