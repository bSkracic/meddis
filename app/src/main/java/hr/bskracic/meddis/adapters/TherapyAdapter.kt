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
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.Therapy
import hr.bskracic.meddis.data.model.TherapyAndMedicationsWithAlarms
import kotlin.math.abs

class TherapyAdapter(private val therapyListener: TherapyListener) :
    ListAdapter<TherapyAndMedicationsWithAlarms, TherapyAdapter.TherapyViewHolder>(TherapyComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        return TherapyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
        holder.bind(getItem(position), therapyListener)
    }

    class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val medicationLabelView =
            itemView.findViewById<TextView>(R.id.therapy_medication_label)
        private val dosageView = itemView.findViewById<TextView>(R.id.therapy_dosage)
        private val alarmView = itemView.findViewById<TextView>(R.id.therapy_alarms)
        private val removeButton = itemView.findViewById<MaterialButton>(R.id.therapy_remove)

        fun bind(item: TherapyAndMedicationsWithAlarms, therapyListener: TherapyListener) {

            removeButton.visibility = View.GONE
            medicationLabelView.text = "${item.medication.label}"
            dosageView.text = "${item.therapy.dosage} ${item.medication.doseUnit}"
            var alarmText = ""
            for(alarm in item.alarms) {
                val h = alarm.hours
                val min = alarm.minutes
                alarmText = "${if(h < 10) "0$h" else "$h"}:${if(min < 10) "0$min" else "$min"} "
            }
            alarmView.text = alarmText

            itemView.setOnClickListener {
                therapyListener.onTherapyClicked(item.therapy.id)
            }

            removeButton.setOnClickListener {
                therapyListener.onTherapyRemoveSwipe(item.therapy, item.alarms)
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
                            if (abs(delta) > 250) {
                                removeButton.visibility = View.VISIBLE
                            } else {
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
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_therapy, parent, false)
                return TherapyViewHolder(view)
            }
        }
    }

    class TherapyComparator : DiffUtil.ItemCallback<TherapyAndMedicationsWithAlarms>() {
        override fun areItemsTheSame(
            oldItem: TherapyAndMedicationsWithAlarms,
            newItem: TherapyAndMedicationsWithAlarms
        ): Boolean {
            return oldItem.therapy.id == newItem.therapy.id
        }

        override fun areContentsTheSame(
            oldItem: TherapyAndMedicationsWithAlarms,
            newItem: TherapyAndMedicationsWithAlarms
        ): Boolean {
            return oldItem.therapy.dosage == newItem.therapy.dosage
        }


    }

    interface TherapyListener {
        fun onTherapyClicked(therapyId: Int)
        fun onTherapyRemoveSwipe(therapy: Therapy, alarms: List<Alarm>)
    }


}