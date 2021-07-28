package hr.bskracic.meddis.repository

import androidx.annotation.WorkerThread
import hr.bskracic.meddis.data.dao.MedicationDao
import hr.bskracic.meddis.data.model.Medication
import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val medicationDao: MedicationDao) {
    var medications: Flow<List<Medication>> = medicationDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(medication: Medication) {
        medicationDao.insert(medication)
    }

}