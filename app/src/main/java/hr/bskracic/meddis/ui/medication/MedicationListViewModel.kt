package hr.bskracic.meddis.ui.medication

import androidx.lifecycle.*
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.repositories.MedicationRepository

class MedicationViewModel(private val repository: MedicationRepository) : ViewModel() {
    val allMedications: LiveData<List<Medication>> = repository.medications.asLiveData()
}

class MedicationViewModelFactory(private val repository: MedicationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}