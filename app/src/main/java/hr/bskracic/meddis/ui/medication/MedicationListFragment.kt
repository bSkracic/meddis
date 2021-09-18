package hr.bskracic.meddis.ui.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.MedicationAdapter
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.databinding.FragmentMedicationBinding
import hr.bskracic.meddis.ui.edits.medication.EditMedicationFragment

class MedicationListFragment : Fragment() {

    private val medicationViewModel: MedicationViewModel by viewModels {
        MedicationViewModelFactory((activity?.application as MeddisApplication).medicationRepository)
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
        _binding = FragmentMedicationBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationsContainer = view.findViewById<RecyclerView>(R.id.recyclerView_medications)
        val adapter = MedicationAdapter(object : MedicationAdapter.MedicationListener {
            override fun onMedicationClicked(medication: Medication) {
                EditMedicationFragment.newInstance(medication.id).show(activity?.supportFragmentManager!!, null)
            }

            // Useless
            override fun onMedicationSwiped(medication: Medication) {
                // Useless
            }

        })
        medicationsContainer.adapter = adapter
        medicationsContainer.layoutManager = LinearLayoutManager(requireContext())

        medicationViewModel.allMedications.observe(viewLifecycleOwner, { medications ->
            adapter.submitList(medications)
            for(medication in medications) {
                print(medication.id)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}