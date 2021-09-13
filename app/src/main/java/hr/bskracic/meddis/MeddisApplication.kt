package hr.bskracic.meddis

import android.app.Application
import hr.bskracic.meddis.data.MeddisDatabase
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MeddisApplication : Application() {

    private val database by lazy { MeddisDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }

    val medicationRepository by lazy { MedicationRepository(database.medicationDao()) }
    val therapyRepository by lazy {TherapyRepository(database.therapyDao())}

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
//            val list = database.collectionDao().getAllWithTherapiesAndMedications()
//            for(item in list){
//                for(therapy in item.therapies) {
//                    Log.d("MED_CHECK", "${item.collection.id} | ${therapy.therapy.id} | ${therapy.medication.label}")
//                }
//            }

//            val list = database.therapyDao().getAllWithMedication()
//            for (item in list) {
//                Log.d("MED_CHECK", "${item.therapy.id} | ${item.medication.label}")
//            }
        }

    }

}