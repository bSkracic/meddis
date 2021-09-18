package hr.bskracic.meddis.ui.medication

import androidx.lifecycle.*
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.repository.MedicationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationViewModel(private val repository: MedicationRepository) : ViewModel() {

    val allMedications: LiveData<List<Medication>> = repository.medications.asLiveData()

    fun insert(medication: Medication) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(medication)
    }
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