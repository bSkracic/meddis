package hr.bskracic.meddis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bskracic.meddis.data.model.Medication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications")
    fun getAll(): Flow<List<Medication>>

    @Query("SELECT * FROM medications")
    fun getAllList(): List<Medication>

    @Query("SELECT * FROM medications WHERE id IN (:medicationIds)")
    fun getByIds(medicationIds: IntArray): Flow<List<Medication>>

    @Query("SELECT * FROM medications WHERE id=:medicationId")
    fun getById(medicationId: Int): LiveData<Medication>

    @Update
    fun update(vararg medication: Medication)

    @Insert
    fun insert(vararg medications: Medication)

    @Delete
    fun delete(medication: Medication)

    @Query("DELETE FROM medications")
    fun deleteAll()
}