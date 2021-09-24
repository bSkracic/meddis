package hr.bskracic.meddis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Medication

class MedicationSpinnerAdapter(context: Context, val medications: List<Medication>) : ArrayAdapter<Medication>(context, 0, medications) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val medication = getItem(position)

        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item_medication, parent, false)
        view.findViewById<TextView>(R.id.spinner_medication_label).text = "${medication?.label} (${medication?.doseUnit})"

        return view
    }

}

