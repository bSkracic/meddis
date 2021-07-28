package hr.bskracic.meddis

import android.app.Application
import hr.bskracic.meddis.data.MeddisDatabase
import hr.bskracic.meddis.repository.MedicationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MeddisApplication : Application() {

    private val database by lazy { MeddisDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }

    val repository by lazy { MedicationRepository(database.medicationDao()) }
}