package hr.bskracic.meddis.ui.edits.medication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Medication
import hr.bskracic.meddis.ui.medication.EditMedicationViewModel
import hr.bskracic.meddis.ui.medication.EditMedicationViewModelFactory

private const val MEDICATION_ID = "MEDICATION_ID"

class EditMedicationFragment : DialogFragment() {
    private var medicationID: Int? = null

    private val medicationViewModel: EditMedicationViewModel by viewModels {
        EditMedicationViewModelFactory((activity?.application as MeddisApplication).medicationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicationID = it.getInt(MEDICATION_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_medication, container, false)
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()

        view?.findViewById<FrameLayout>(R.id.edit_medication_container)?.setOnClickListener {
            dismiss()
        }
        // Clumsy, but does the job
        view?.findViewById<CardView>(R.id.edit_medication_card)?.setOnClickListener(null)

        view?.findViewById<Spinner>(R.id.edit_medication_dose_unit)?.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, mutableListOf("tableta", "mg", "mL"))

        val maxAmount = view?.findViewById<EditText>(R.id.edit_medication_max_amount)
        val currentAmount = view?.findViewById<EditText>(R.id.edit_medication_current_amount)

        maxAmount?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed) {
                        currentAmount?.setText(v?.text.toString())
                        return true
                    }
                }
                return false
            }
        })

        var medication: Medication? = null
        var isUpdating = false

        if (medicationID != null && medicationID != 0) {
            isUpdating = true
            medicationViewModel.getById(medicationID!!).observe(viewLifecycleOwner, {
                medication = it
                populateFields(it)
            })
        }
        else if( medicationID != null && medicationID == 0) {
            view?.findViewById<Button>(R.id.edit_medication_delete)?.visibility = View.GONE
        }
        else {
            dismiss()
        }

        view?.findViewById<Button>(R.id.edit_medication_save)?.setOnClickListener {
            view?.apply {
                medication = Medication(
                    medicationID!!,
                    findViewById<TextInputEditText>(R.id.edit_medication_label).text.toString(),
                    findViewById<TextInputEditText>(R.id.edit_medication_description).text.toString(),
                    currentAmount?.text.toString().toInt(),
                    maxAmount?.text.toString().toInt(),
                    findViewById<Spinner>(R.id.edit_medication_dose_unit).selectedItem.toString()
                    )
                val message = validateInput(medication!!)
                if(message == null) {
                    saveChanges(medication!!, isUpdating)
                    dismiss()
                } else {
                    findViewById<TextView>(R.id.edit_medication_warning).text = message
                }
            }
        }

        view?.findViewById<Button>(R.id.edit_medication_delete)?.setOnClickListener {
            if(medication != null){
                medicationViewModel.delete(medication!!)
                dismiss()
            }
        }

    }

    private fun setWindowParams(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun validateInput(medication: Medication): String? {
        if(medication.label.isBlank()) {
            return "Ime lijeka ne smije biti prazno"
        } else if(medication.maxAmount <= 0 || medication.currentAmount > medication.maxAmount) {
            return "Neispravna vrijednost količine"
        }
        return null
    }

    private fun saveChanges(medication: Medication, isUpdating: Boolean) {
        if(isUpdating) {
            medicationViewModel.update(medication)
        } else {
            medicationViewModel.insert(medication)
        }
    }

    private fun populateFields(medication: Medication) {
        view?.apply {
            findViewById<TextInputEditText>(R.id.edit_medication_label).setText(medication.label)
            findViewById<TextInputEditText>(R.id.edit_medication_description).setText(medication.description)
            findViewById<EditText>(R.id.edit_medication_current_amount).setText(medication.currentAmount.toString())
            findViewById<EditText>(R.id.edit_medication_max_amount).setText(medication.maxAmount.toString())
            val spinner = findViewById<Spinner>(R.id.edit_medication_dose_unit)
            for(i in 0..spinner.adapter.count) {
                if(spinner.adapter.getItem(i) == medication.doseUnit) {
                    spinner.setSelection(i)
                    break
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param medicationID Id of the medication user wants to edit; 0 is new medication fragment.
         * @return A new instance of fragment NewMedicationFragment.
         */
        @JvmStatic
        fun newInstance(medicationID: Int) =
            EditMedicationFragment().apply {
                arguments = Bundle().apply {
                    putInt(MEDICATION_ID, medicationID)
                }
            }
    }
}