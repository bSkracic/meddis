package hr.bskracic.meddis.ui.therapy

import android.app.AlarmManager
import android.content.Context
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
import hr.bskracic.meddis.adapters.TherapyAdapter
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.databinding.FragmentTherapyBinding
import hr.bskracic.meddis.utils.AlarmUtils

class TherapyListFragment : Fragment() {

    private val viewModel: TherapyListViewModel by viewModels {
        TherapyListViewModelFactory((activity?.application as MeddisApplication).therapyRepository)
    }

    private var _binding: FragmentTherapyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTherapyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TherapyAdapter(object : TherapyAdapter.TherapyListener {
            override fun onTherapyClicked(therapyId: Int) {
//                val bundle = bundleOf(THERAPY_ID to therapyId)
//                activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigate(R.id.nav_edit_therapy, bundle)
            }

            override fun onTherapyRemoveSwipe(therapy: Therapy, alarms: List<Alarm>) {
                for(alarm in alarms) {
                    AlarmUtils.removeAlarm(requireContext(), (activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager), alarm)
                }
                viewModel.delete(therapy)
            }
        })

        view.findViewById<RecyclerView>(R.id.recyclerView_therapies).apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.getAllWithMedicationAndAlarms().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}