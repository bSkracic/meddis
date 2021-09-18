package hr.bskracic.meddis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bskracic.meddis.data.model.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAll(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE alarms.id IN (:alarmIds)")
    fun getByIds(alarmIds: IntArray): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE alarms.therapy_id = :therapyId")
    fun getByTherapyId(therapyId: Int): LiveData<List<Alarm>>

    @Update
    fun update(alarm: Alarm)

    @Insert
    fun insert(vararg alarms: Alarm)

    @Insert
    fun insertWithIdReturn(alarm: Alarm): Long

    @Delete
    fun delete(alarm: Alarm)
}