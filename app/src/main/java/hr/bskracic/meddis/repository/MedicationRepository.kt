package hr.bskracic.meddis.repository

import androidx.annotation.WorkerThread
import hr.bskracic.meddis.data.dao.MedicationDao
import hr.bskracic.meddis.data.model.Medication
import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val medicationDao: MedicationDao) {
    var medications: Flow<List<Medication>> = medicationDao.getAll()

    @WorkerThread
    suspend fun getByIds(medicationIds: IntArray): Flow<List<Medication>> {
        return medicationDao.getByIds(medicationIds)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(medication: Medication) {
        medicationDao.insert(medication)
    }

    @WorkerThread
    suspend fun update(medication: Medication) {
        medicationDao.update(medication)
    }

    @WorkerThread
    suspend fun delete(medication: Medication) {
        medicationDao.delete(medication)
    }

}