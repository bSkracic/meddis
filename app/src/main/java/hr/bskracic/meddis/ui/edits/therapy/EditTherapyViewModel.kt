package hr.bskracic.meddis.ui.edits.therapy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedication
import hr.bskracic.meddis.repositories.AlarmRepository
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import hr.bskracic.meddis.viewmodels.ViewRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditTherapyViewModel(private val therapyRepo: TherapyRepository, private val medicationRepo: MedicationRepository, private val alarmRepo: AlarmRepository) : ViewModel(),
    ViewRepo<Therapy> {

    val allTherapies: LiveData<List<Therapy>> = therapyRepo.therapies

    fun getAllMedication() : LiveData<List<Medication>> {
        return medicationRepo.medications.asLiveData()
    }

    fun getByIdWithMedication(therapyId: Int): LiveData<TherapyAndMedication> {
        return therapyRepo.getByIdWithMedication(therapyId)
    }

    fun getMedicationById(medicationId: Int): LiveData<Medication> {
        return medicationRepo.getById(medicationId)
    }

    override fun getById(therapyId: Int): LiveData<Therapy> {
        return therapyRepo.getById(therapyId)
    }

    fun getAlarmsByTherapyId(therapyId: Int): LiveData<List<Alarm>> {
        return alarmRepo.getByTherapyId(therapyId)
    }

    fun insert(alarm: Alarm) = CoroutineScope(Dispatchers.IO).launch {
        alarmRepo.insert(alarm)
    }

    fun update(alarm: Alarm) = CoroutineScope(Dispatchers.IO).launch {
        alarmRepo.update(alarm)
    }

    fun delete(alarm: Alarm) = CoroutineScope(Dispatchers.IO).launch {
        alarmRepo.delete(alarm)
    }

    fun insert(therapy: Therapy, onInserted: (id: Long) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        onInserted(therapyRepo.insertWithIdReturn(therapy))
    }

    override fun insert(therapy: Therapy, ) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepo.insert(therapy)
    }

    override fun update(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepo.update(therapy)
    }

    override fun delete(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepo.delete(therapy)
    }
}

class EditTherapyViewModelFactory(private val therapyRepo: TherapyRepository, private val medicationRepo: MedicationRepository, private val alarmRepo: AlarmRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTherapyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditTherapyViewModel(therapyRepo, medicationRepo, alarmRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}