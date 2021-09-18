package hr.bskracic.meddis.ui.edits.therapy

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.AlarmAdapter
import hr.bskracic.meddis.adapters.MedicationSpinnerAdapter
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.data.model.RepeatType
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.ui.edits.EditItemFragment
import hr.bskracic.meddis.utils.AlarmUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val THERAPY_ID = "THERAPY_ID"

// TODO: implement canceling when creating  new fragment because alarms are being inserted into database

class EditTherapyFragment : EditItemFragment<Therapy>(R.layout.fragment_edit_therapy) {

    override val ITEM_ID: String = THERAPY_ID

    override val viewModel: EditTherapyViewModel by viewModels {
        EditTherapyViewModelFactory(
            (activity?.application as MeddisApplication).therapyRepository,
            (activity?.application as MeddisApplication).medicationRepository,
            (activity?.application as MeddisApplication).alarmRepository
        )
    }

    private var medicationSpinner: Spinner? = null
    private var dosageTextView: TextView? = null
    private var doseUnitTextView: TextView? = null
    private var alarmsContainer: RecyclerView? = null
    private var doseLayout: LinearLayout? = null
    private var alarmLayout: LinearLayout? = null

    private var medications: List<Medication> = arrayListOf()
    private var alarms: MutableList<Alarm> = mutableListOf()

    private var initSpinner: Boolean = false

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
        alarmsContainer = view.findViewById(R.id.edit_therapy_alarms)
        doseLayout = view.findViewById(R.id.edit_therapy_dose_layout)
        alarmLayout = view.findViewById(R.id.edit_therapy_alarm_layout)
        saveButton = view.findViewById(R.id.edit_therapy_save)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun populateView() {
        if(!isUpdating) {
            doseUnitTextView?.visibility = View.GONE
            doseLayout?.visibility = View.GONE
            alarmLayout?.visibility = View.GONE
            saveButton?.visibility = View.GONE
        } else {
            setAlarmContainer()
            viewModel.getMedicationById(item?.medicationId!!).observe(viewLifecycleOwner, {
                doseUnitTextView?.text = it.doseUnit
            })
        }

        alarmsContainer?.apply {
            this.adapter = AlarmAdapter(object : AlarmAdapter.AlarmListener {
                override fun onDeleteClicked(alarm: Alarm) {
                    AlarmUtils.removeAlarm(requireContext(), activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager, alarm)
                    viewModel.delete(alarm)
                }

                override fun onEditTimeClicked(alarm: Alarm) {
                    createTimePicker(alarm).show()
                }
            })
            this.layoutManager = LinearLayoutManager(context)
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
                    if(!initSpinner){
                        initSpinner = true
                        return
                    }

                    doseUnitTextView?.visibility = View.VISIBLE
                    doseUnitTextView?.text = medications[position].doseUnit

                    if(!isUpdating) {
                        viewModel.insert(Therapy(0, medications[position].id, 1)) { therapyId ->
                            itemId = therapyId.toInt()
                            CoroutineScope(Dispatchers.Main).launch {
                                isUpdating = true
                                doseLayout?.visibility = View.VISIBLE
                                alarmLayout?.visibility = View.VISIBLE
                                saveButton?.visibility = View.VISIBLE
                                setAlarmContainer() // transition to next stage of input
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            if(isUpdating){
                medicationSpinner?.setSelection(adapter.getPosition(medications.find { m -> m.id == item?.medicationId }))
            }
        })

        dosageTextView?.text = item?.dosage.toString()
    }

    private fun setAlarmContainer() {
        // TODO: Adapter should be set regardless of item's existence
        viewModel.getAlarmsByTherapyId(itemId).observe(viewLifecycleOwner, {
            (alarmsContainer?.adapter as AlarmAdapter).submitList(it)
        })

        view?.findViewById<MaterialButton>(R.id.edit_therapy_alarm_add)?.setOnClickListener {
            val calendar = Calendar.getInstance()

            val now = System.currentTimeMillis()

            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 1)

            val alarm = Alarm(0, itemId, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), RepeatType.DAILY)

            viewModel.insert(alarm) { id ->
                alarm.id = id.toInt()
                AlarmUtils.setAlarm(
                    requireContext(),
                    activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                    alarm
                )
            }
        }
    }

    override fun validateItem(): String? {
        if(item != null) {
            if(alarmsContainer != null){
                if(alarmsContainer?.adapter!!.itemCount <= 0)
                    return "Potrebno je dodati barem jedan podsjetnik"
            }
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

        return Therapy(
            itemId,
            medicationId,
            dosageTextView?.text.toString().toInt()
        )
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
                viewModel.update(alarm)

                AlarmUtils.setAlarm(requireContext(), activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager , alarm)
            },
            hour, minute, true)
    }

}