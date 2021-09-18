package hr.bskracic.meddis.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedication
import kotlin.math.abs

class TherapyAdapter(private val therapyListener: TherapyListener) : ListAdapter<TherapyAndMedication, TherapyAdapter.TherapyViewHolder>(TherapyComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        return TherapyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
       holder.bind(getItem(position), therapyListener)
    }

    class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val medicationLabelView  = itemView.findViewById<TextView>(R.id.therapy_medication_label)
        private val dosageView = itemView.findViewById<TextView>(R.id.therapy_dosage)
        private val iconView = itemView.findViewById<ImageView>(R.id.therapy_icon)
        private val removeButton = itemView.findViewById<MaterialButton>(R.id.therapy_remove)

        fun bind(item: TherapyAndMedication, therapyListener: TherapyListener) {

            removeButton.visibility = View.GONE
            medicationLabelView.text = "${item.medication.label}"
            dosageView.text = "${item.therapy.dosage} ${item.medication.doseUnit}"

            itemView.setOnClickListener {
                therapyListener.onTherapyClicked(item.therapy.id)
            }

            removeButton.setOnClickListener {
                therapyListener.onTherapyRemoveSwipe(item.therapy)
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
                            if (abs(delta) > 100) {
                                iconView.visibility = View.GONE
                                removeButton.visibility = View.VISIBLE
                            } else {
                                iconView.visibility = View.VISIBLE
                                removeButton.visibility = View.GONE
                                v.performClick()
                            }
                        }
                    }
                }
                true
            }
        }

        companion object {
            fun create(parent: ViewGroup): TherapyViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_therapy, parent, false)
                return TherapyViewHolder(view)
            }
        }
    }

    class TherapyComparator : DiffUtil.ItemCallback<TherapyAndMedication>() {
        override fun areItemsTheSame(
            oldItem: TherapyAndMedication,
            newItem: TherapyAndMedication
        ): Boolean {
            return oldItem.therapy.id == newItem.therapy.id
        }

        override fun areContentsTheSame(
            oldItem: TherapyAndMedication,
            newItem: TherapyAndMedication
        ): Boolean {
            return oldItem.medication.label == newItem.medication.label
        }


    }

    interface TherapyListener {
        fun onTherapyClicked(therapyId: Int)
        fun onTherapyRemoveSwipe(therapy: Therapy)
    }


}