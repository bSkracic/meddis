package hr.bskracic.meddis.ui.edits.therapy

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuhart.stepview.StepView.ANIMATION_ALL
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.AlarmAdapter
import hr.bskracic.meddis.adapters.MedicationSpinnerAdapter
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.data.model.RepeatType
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.databinding.FragmentNewTherapyBinding
import hr.bskracic.meddis.utils.AlarmUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NewTherapyFragment : Fragment(R.layout.fragment_new_therapy) {

    private val viewModel: NewTherapyViewModel by viewModels {
        NewTherapyViewModelFactory(
            (activity?.application as MeddisApplication).therapyRepository,
            (activity?.application as MeddisApplication).medicationRepository,
            (activity?.application as MeddisApplication).alarmRepository
        )
    }

    private var _binding: FragmentNewTherapyBinding? = null
    private val binding get() = _binding!!

    private val descriptions = listOf(
        "Odaberite lijek",
        "Postavite količinu lijeka kojeg uzimate",
        "Dodajte vremenske podsjetnike"
    )

    /**
     * Views, replace with view binding
     */
    private lateinit var medicationSpinner: Spinner
    private lateinit var doseUnitView: TextView
    private lateinit var dosageView: TextView
    private lateinit var alarmView: RecyclerView

    private var alarms: MutableList<Alarm> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTherapyBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.medications.observe(viewLifecycleOwner, {
            val adapter = MedicationSpinnerAdapter(requireContext(), it)
            medicationSpinner.adapter = adapter
        })

        medicationSpinner = view.findViewById(R.id.new_therapy_medication)
        doseUnitView = view.findViewById(R.id.new_therapy_dose_unit)
        dosageView = view.findViewById(R.id.new_therapy_dosage)
        alarmView = view.findViewById(R.id.new_therapy_alarms)

        binding.newTherapyDescription.text = descriptions[0]
        binding.newTherapyAlarmLayout.visibility = View.GONE
        binding.newTherapyDosageLayout.visibility = View.GONE
        binding.stepView.state.apply {
            animationType(ANIMATION_ALL)
            commit()
        }

        binding.newTherapyAlarmNext.setOnClickListener {
            nextStep()
        }

        val adapter = AlarmAdapter(object : AlarmAdapter.AlarmListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDeleteClicked(alarm: Alarm) {
                alarms.removeIf {
                    it.id == alarm.id
                }
                (binding.newTherapyAlarms.adapter as AlarmAdapter).let {
                    it.submitList(alarms)
                    it.notifyDataSetChanged()
                }

            }

            override fun onEditTimeClicked(alarm: Alarm) {
                createTimePicker(alarm).show()
            }

        })
        binding.newTherapyAlarms.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.newTherapyAlarmAdd.setOnClickListener {
            alarms.add(Alarm(alarms.size, 0, 8, 0, RepeatType.DAILY))
            adapter.submitList(alarms)
            adapter.notifyDataSetChanged()
        }
    }

    private fun nextStep() {
        var new = 0
        binding.stepView.let {
            it.currentStep.let { index ->
                if (index < it.stepCount - 1) {
                    new = index + 1
                    it.go(new, true)
                }
            }
        }
        setStepVisibility(new)
    }

    private fun setStepVisibility(index: Int) {
        binding.newTherapyDescription.text = descriptions[index]
        when (index) {
            0 -> {
                binding.newTherapyMedicationLayout.visibility = View.VISIBLE
                binding.newTherapyAlarmLayout.visibility = View.GONE
                binding.newTherapyDosageLayout.visibility = View.GONE
                // Remove card with details
            }
            1 -> {
                binding.newTherapyDosageLayout.visibility = View.VISIBLE
                binding.newTherapyMedicationLayout.visibility = View.GONE
                binding.newTherapyDoseUnit.text =
                    (medicationSpinner.selectedItem as Medication).doseUnit
            }
            2 -> {
                binding.newTherapyAlarmLayout.visibility = View.VISIBLE
                binding.newTherapyDosageLayout.visibility = View.GONE
                binding.newTherapyAlarmNext.text = "Spremi"
                binding.newTherapyAlarmNext.setOnClickListener {
                    viewModel.insertWithIdReturn(
                        Therapy(
                            0,
                            (medicationSpinner.selectedItem as Medication).id,
                            binding.newTherapyDosage.text?.toString()?.toInt()!!
                        )
                    ) {
                        for (alarm in alarms) {
                            alarm.apply {
                                id = 0
                                therapyId = it.toInt()
                            }
                            viewModel.insertWithIdReturn(alarm) {
                                alarm.id = it.toInt()
                                val alarmManager =
                                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                AlarmUtils.setAlarm(requireContext(), alarmManager, alarm)
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun createTimePicker(alarm: Alarm): TimePickerDialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity,
            { _, h, min ->
                alarm.hours = h
                alarm.minutes = min
                binding.newTherapyAlarms.adapter?.notifyDataSetChanged()
            },
            hour, minute, true)
    }

}