package hr.bskracic.meddis

import android.app.Application
import hr.bskracic.meddis.data.MeddisDatabase
import hr.bskracic.meddis.repositories.AlarmRepository
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MeddisApplication : Application() {

    private val database by lazy { MeddisDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }

    val medicationRepository by lazy { MedicationRepository(database.medicationDao()) }
    val therapyRepository by lazy { TherapyRepository(database.therapyDao()) }
    val alarmRepository by lazy { AlarmRepository(database.alarmDao()) }
}