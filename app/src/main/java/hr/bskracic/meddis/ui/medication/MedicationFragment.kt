package hr.bskracic.meddis.ui.medication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.databinding.FragmentMedicationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MedicationFragment : Fragment() {

    private val medicationViewModel: MedicationViewModel by viewModels {
        MedicationViewModelFactory((activity?.application as MeddisApplication).repository)
    }
    private var _binding: FragmentMedicationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        medicationViewModel.allMedications.observe(viewLifecycleOwner, { medications ->
            for(m in medications){
                Log.println(Log.DEBUG, "MED_CHECK: ", "${m.id} | ${m.label}")
            }
        })


        medicationViewModel.insert(Medication(0, "ubaceni lijek", "hehe xD", 69, 69, "tableta"))


        _binding = FragmentMedicationBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}