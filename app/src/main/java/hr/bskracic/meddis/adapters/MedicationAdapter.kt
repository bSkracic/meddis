package hr.bskracic.meddis.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Medication
import kotlin.math.abs

class MedicationAdapter(private val medicationListener: MedicationListener) : ListAdapter<Medication, MedicationAdapter.MedicationViewHolder>(MedicationComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.bind(getItem(position), medicationListener)
    }

    class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val labelTextView  = itemView.findViewById<TextView>(R.id.medication_label)
        private val amountTextView = itemView.findViewById<TextView>(R.id.medication_amount)

        fun bind(medication: Medication, medicationListener: MedicationListener) {
            labelTextView.text = medication.label
            "${medication.currentAmount}/${medication.maxAmount} ${medication.doseUnit}".also {
                amountTextView.text = it
            }

            itemView.setOnClickListener {
                medicationListener.onMedicationClicked(medication)
            }

            // Set swipe listener
            var x1 = 0f
            var x2: Float

            itemView.setOnTouchListener { v, event ->
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            x1 = event.x
                        }
                        MotionEvent.ACTION_UP -> {
                            x2 = event.x
                            val delta = x2 - x1
                            if (abs(delta) > 150) {
                                medicationListener.onMedicationSwiped(medication)
                            } else {
                                v.performClick()
                            }
                        }
                    }
                }
                true
            }
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

    interface MedicationListener {
        fun onMedicationClicked(medication: Medication)
        fun onMedicationSwiped(medication: Medication)
    }
}