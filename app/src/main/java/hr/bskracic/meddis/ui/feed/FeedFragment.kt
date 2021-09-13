package hr.bskracic.meddis.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.MeddisDatabase
import hr.bskracic.meddis.repositories.MedicationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FeedFragment : Fragment(R.layout.fragment_feed) {

    companion object {
        fun newInstance() = FeedFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val database: MeddisDatabase by lazy { MeddisDatabase.getDatabase(requireContext(), CoroutineScope(SupervisorJob())) }

        val repository: MedicationRepository by lazy { MedicationRepository(database.medicationDao()) }

        CoroutineScope(SupervisorJob()).launch {
            val medications = database.medicationDao().getAllList()
            for(m in medications) {
                Log.println(Log.DEBUG, "MED_CHECK: ", "${m.id} | ${m.label}")
            }
        }
    }
}