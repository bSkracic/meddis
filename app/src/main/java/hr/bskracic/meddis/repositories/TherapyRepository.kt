package hr.bskracic.meddis.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import hr.bskracic.meddis.data.dao.TherapyDao
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedication
import kotlinx.coroutines.flow.Flow

class TherapyRepository(private val therapyDao: TherapyDao) {
    val therapies: LiveData<List<Therapy>> = therapyDao.getAll()

    @WorkerThread
    fun getAll(): LiveData<List<Therapy>> {
        return therapyDao.getAll()
    }

    @WorkerThread
    fun getAllWithMedication(): LiveData<List<TherapyAndMedication>> {
        return therapyDao.getAllWithMedication()
    }


    @WorkerThread
    fun getById(therapyId: Int) : LiveData<Therapy> {
        return therapyDao.getById(therapyId)
    }

    @WorkerThread
    fun getByIdWithMedication(therapyId: Int): LiveData<TherapyAndMedication> {
        return therapyDao.getByIdWithMedication(therapyId)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insert(therapy: Therapy) {
        therapyDao.insert(therapy)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun update(therapy: Therapy) {
        therapyDao.update(therapy)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(therapy: Therapy) {
        therapyDao.delete(therapy)
    }
}