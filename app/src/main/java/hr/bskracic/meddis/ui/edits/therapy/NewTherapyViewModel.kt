package hr.bskracic.meddis.ui.edits.therapy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.repositories.AlarmRepository
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTherapyViewModel(
    private val therapyRepo: TherapyRepository,
    private val medicationRepo: MedicationRepository,
    private val alarmRepo: AlarmRepository
) : ViewModel() {

    val medications = medicationRepo.medications.asLiveData()

    fun insertWithIdReturn(alarm: Alarm, onInserted: (id: Long) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        onInserted(alarmRepo.insertWithIdReturn(alarm))
    }

    fun insertWithIdReturn(therapy: Therapy, onInserted: (id: Long) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onInserted(therapyRepo.insertWithIdReturn(therapy))
        }

}

class NewTherapyViewModelFactory(
    private val therapyRepo: TherapyRepository,
    private val medicationRepo: MedicationRepository,
    private val alarmRepo: AlarmRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTherapyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewTherapyViewModel(therapyRepo, medicationRepo, alarmRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}