package hr.bskracic.meddis.ui.edits.therapy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedication
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import hr.bskracic.meddis.viewmodels.ViewRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTherapyViewModel(private val therapyRepository: TherapyRepository, private val medicationRepository: MedicationRepository) : ViewModel(),
    ViewRepo<Therapy> {

    val allTherapies: LiveData<List<Therapy>> = therapyRepository.therapies

    fun getByIdWithMedication(therapyId: Int): LiveData<TherapyAndMedication> {
        return therapyRepository.getByIdWithMedication(therapyId)
    }

    fun getMedicationById(medicationId: Int): LiveData<Medication> {
        return medicationRepository.getById(medicationId)
    }

    override fun getById(therapyId: Int): LiveData<Therapy> {
        return therapyRepository.getById(therapyId)
    }

    fun getAllMedication() : LiveData<List<Medication>> {
        return medicationRepository.medications.asLiveData()
    }

    override fun insert(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepository.insert(therapy)
    }

    override fun update(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepository.update(therapy)
    }

    override fun delete(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepository.delete(therapy)
    }
}

class EditTherapyViewModelFactory(private val therapyRepository: TherapyRepository, private val medicationRepository: MedicationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTherapyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditTherapyViewModel(therapyRepository, medicationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}