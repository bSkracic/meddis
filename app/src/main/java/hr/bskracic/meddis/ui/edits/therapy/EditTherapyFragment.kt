package hr.bskracic.meddis.ui.edits.therapy

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.AlarmAdapter
import hr.bskracic.meddis.adapters.MedicationSpinnerAdapter
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.ui.edits.EditItemFragment
import kotlinx.coroutines.Job
import java.util.*
import kotlin.properties.Delegates

const val THERAPY_ID = "THERAPY_ID"

class EditTherapyFragment : EditItemFragment<Therapy>(R.layout.fragment_edit_therapy) {

    override val ITEM_ID: String = THERAPY_ID

    override val viewModel: EditTherapyViewModel by viewModels {
        EditTherapyViewModelFactory(
            (activity?.application as MeddisApplication).therapyRepository,
            (activity?.application as MeddisApplication).medicationRepository
        )
    }

    private var medicationSpinner: Spinner? = null
    private var dosageTextView: TextView? = null
    private var doseUnitTextView: TextView? = null

    private var medications: List<Medication> = arrayListOf()
    private var alarms: MutableList<Alarm> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemId = it.getInt(ITEM_ID)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        medicationSpinner = view.findViewById(R.id.edit_therapy_medication)
        dosageTextView = view.findViewById(R.id.edit_therapy_dosage)
        doseUnitTextView = view.findViewById(R.id.edit_therapy_dose_unit)

        if(!isUpdating) {
            doseUnitTextView?.visibility = View.GONE
        }

        //  Medication spinner set up
        viewModel.getAllMedication().observe(viewLifecycleOwner, {
            val adapter = MedicationSpinnerAdapter(requireContext(), it)
            medications = it
            medicationSpinner?.adapter = adapter
            medicationSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    doseUnitTextView?.visibility = View.VISIBLE
                    doseUnitTextView?.text = medications[position].doseUnit
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        })

        deleteButton = view.findViewById(R.id.edit_therapy_delete)
        saveButton = view.findViewById(R.id.edit_therapy_save)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun populateView() {
        // BAD
        viewModel.getAllMedication().observe(viewLifecycleOwner, {
            val adapter = MedicationSpinnerAdapter(requireContext(), it)
            medications = it
            medicationSpinner?.adapter = adapter
            medicationSpinner?.setSelection((medicationSpinner?.adapter as MedicationSpinnerAdapter).getPosition(medications.find { m -> m.id == item?.medicationId }))

        })

        // Alarm recyclerView setup
        val adapter = AlarmAdapter(object: AlarmAdapter.AlarmListener {
            override fun onDeleteClicked(alarm: Alarm) {
                alarms.removeAll { a-> a.id == alarm.id }
            }

            override fun onEditTimeClicked(alarm: Alarm) {
                createTimePicker(alarm.id).show()
            }
        })

        view?.findViewById<RecyclerView>(R.id.edit_therapy_alarms)?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        alarms = mutableListOf()

        item?.alarms?.split(';')?.forEachIndexed { index, s ->
            if(s.contains('|')) {
                val alarmData = s.split('|')
                alarms.add(Alarm(index, alarmData[0], alarmData[1].toInt()))
            }
        }

        adapter.submitList(alarms)

        view?.findViewById<Button>(R.id.edit_therapy_alarm_add)?.setOnClickListener {
            alarms.add(Alarm(alarms.size, "8:00", Alarm.REPEAT_DAILY))
            adapter.submitList(alarms) // Does not work as intended
        }


        viewModel.getMedicationById(item?.medicationId!!).observe(viewLifecycleOwner, {
            doseUnitTextView?.text = it.doseUnit
        })

        dosageTextView?.text = item?.dosage.toString()
    }

    override fun validateItem(): String? {
        if(item != null) {
            if(item?.dosage!! <= 0)
                return "Pogrešna vrijednost doze lijeka"
            if(item?.medicationId == 0)
                return "Potrebno je odabrati lijek"
            return null
        }
        return "Pogreška"
    }

    override fun updateItemFromView(): Therapy {

        var medicationId = 0

        if(medicationSpinner?.selectedItem != null) {
            medicationId = (medicationSpinner?.selectedItem as Medication).id
        }

        var alarmString = ""

        if(alarms.isNotEmpty()) {
            for(i in 0 until alarms.size) {
                if(alarms.size > 0) {
                    alarmString += ";"
                }
                alarmString += "${alarms[i].time}|${alarms[i].repeatType}"
            }
        }

        return Therapy(
            itemId,
            medicationId,
            dosageTextView?.text.toString().toInt(),
            alarmString
        )
    }

    private fun createTimePicker(alarmId: Int): TimePickerDialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity,
            { _, hourOfDay, minute ->
                for (alarm in alarms) {
                    if(alarm.id == alarmId){
                        alarm.time = "$hourOfDay:$minute"
                    }
                }
            }, hour, minute, true)
    }

    private fun setMedicationsSpinner() {

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param therapyId Id of the medication user wants to edit; 0 is new medication fragment.
//         * @return A new instance of fragment NewMedicationFragment.
//         */
//        @JvmStatic
//        fun newInstance(therapyId: Int) =
//            EditTherapyFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ITEM_ID, therapyId)
//                }
//            }
//    }

}