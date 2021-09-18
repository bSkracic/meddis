package hr.bskracic.meddis.ui.therapy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedication
import hr.bskracic.meddis.repositories.TherapyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TherapyListViewModel(private val therapyRepository: TherapyRepository) : ViewModel() {

    fun getAll(): LiveData<List<Therapy>> {
        return therapyRepository.getAll()
    }

    fun getAllWithMedication(): LiveData<List<TherapyAndMedication>> {
        return therapyRepository.getAllWithMedication()
    }

    fun delete(therapy: Therapy) = CoroutineScope(Dispatchers.IO).launch {
        therapyRepository.delete(therapy)
    }
}

class TherapyListViewModelFactory(private val therapyRepository: TherapyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TherapyListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TherapyListViewModel(therapyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}