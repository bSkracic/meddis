package hr.bskracic.meddis.ui.medication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Medication

private const val MEDICATION_ID = "MEDICATION_ID"

/*
* TODO: Should only open this from medication list, add option to remove the drug
* */

class EditMedicationFragment : DialogFragment() {
    private var medicationID: Int? = null
    private var medication: Medication? = null

    private val medicationViewModel: EditMedicationViewModel by viewModels {
        EditMedicationViewModelFactory((activity?.application as MeddisApplication).repository)
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

        var isUpdating: Boolean = false

        if (medicationID != null && medicationID != 0) {
            isUpdating = true
            medication = medicationViewModel.getById(medicationID!!)
        } else {
//            dismiss()
        }

        val maxAmount = view?.findViewById<EditText>(R.id.edit_medication_max_amount)
        val currentAmount = view?.findViewById<EditText>(R.id.edit_medication_current_amount)


        // TODO: Does not work
        maxAmount?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed) {
                        currentAmount?.setText(v?.text)
                        return true
                    }
                }
                return false
            }

        })

        view?.findViewById<Button>(R.id.edit_medication_save)?.setOnClickListener {
            view?.apply {
                val medication = Medication(
                    medicationID!!,
                    findViewById<EditText>(R.id.edit_medication_label).text.toString(),
                    findViewById<EditText>(R.id.edit_medication_description).text.toString(),
                    findViewById<EditText>(R.id.edit_medication_current_amount).text.toString().toInt(),
                    findViewById<EditText>(R.id.edit_medication_max_amount).text.toString().toInt(),
                    findViewById<Spinner>(R.id.edit_medication_dose_unit).selectedItem.toString()
                    )
                val message = validateInput(medication)
                if(message == null) {
                    saveChanges(medication, isUpdating)
                } else {
                    findViewById<TextView>(R.id.edit_medication_warning).text = message
                }
            }
        }

        view?.findViewById<Spinner>(R.id.edit_medication_dose_unit)?.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, mutableListOf("tableta", "mg", "mL"), )
    }

    private fun setWindowParams(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun validateInput(medication: Medication): String? {
        if(medication.label.isNullOrBlank()) {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param medicationID Id of the medication user wants to edit; 0 is new medication fragment.
         * @return A new instance of fragment NewMedicationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(medicationID: Int) =
            EditMedicationFragment().apply {
                arguments = Bundle().apply {
                    putInt(MEDICATION_ID, medicationID)
                }
            }
    }
}