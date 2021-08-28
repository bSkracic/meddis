package hr.bskracic.meddis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Medication

class MedicationAdapter : ListAdapter<Medication, MedicationAdapter.MedicationViewHolder>(MedicationComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val labelTextView  = itemView.findViewById<TextView>(R.id.medication_label)
        private val amountTextView = itemView.findViewById<TextView>(R.id.medication_amount)

        fun bind(medication: Medication) {
            labelTextView.text = medication.label
            "${medication.currentAmount}/${medication.maxAmount} ${medication.doseUnit}".also { amountTextView.text = it }
        }

        companion object {
            fun create(parent: ViewGroup): MedicationViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_medication, parent, false)
                return MedicationViewHolder(view)
            }
        }
    }

    class MedicationComparator : DiffUtil.ItemCallback<Medication>() {
        override fun areItemsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medication, newItem: Medication): Boolean {
            return oldItem.label == newItem.label
        }

    }
}