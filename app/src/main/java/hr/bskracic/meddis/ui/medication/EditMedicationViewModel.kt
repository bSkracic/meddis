package hr.bskracic.meddis.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.repository.MedicationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditMedicationViewModel(private val repository: MedicationRepository) : ViewModel() {

    val allMedications: LiveData<List<Medication>> = repository.medications.asLiveData()

    fun insert(medication: Medication) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(medication)
    }

    fun update(medication: Medication) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(medication)
    }

    fun getById(medicationId: Int): Medication? {

        var medication: Medication? = null

        CoroutineScope(Dispatchers.IO).launch {
            medication = repository.getByIds(intArrayOf(medicationId)).asLiveData().value?.get(0)
        }

        return medication
    }

}

class EditMedicationViewModelFactory(private val repository: MedicationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditMedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditMedicationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}