package hr.bskracic.meddis.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import hr.bskracic.meddis.data.dao.AlarmDao
import hr.bskracic.meddis.data.model.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {
    var alarms = alarmDao.getAll()

    @WorkerThread
    fun getByTherapyId(therapyId: Int): LiveData<List<Alarm>> {
        return alarmDao.getByTherapyId(therapyId)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insert(alarm: Alarm) {
        return alarmDao.insert(alarm)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertWithIdReturn(alarm: Alarm): Long {
        return alarmDao.insertWithIdReturn(alarm)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

}