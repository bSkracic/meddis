package hr.bskracic.meddis.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.data.model.FeedItemWithTherapyAndAlarms

class FeedItemAdapter(private val feedItemListener: FeedItemListener) : ListAdapter<FeedItemWithTherapyAndAlarms, FeedItemAdapter.FeedItemViewHolder>(FeedItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        return FeedItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(getItem(position), feedItemListener)
    }

    class FeedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val alarmTimeView: TextView = itemView.findViewById(R.id.feed_alarm_time)
        private val medicationLabelView: TextView = itemView.findViewById(R.id.feed_medication_label)
        private val doseView: TextView = itemView.findViewById(R.id.feed_dose)
        private val checkView: MaterialButton = itemView.findViewById(R.id.feed_check)

        fun bind(item: FeedItemWithTherapyAndAlarms, feedItemListener: FeedItemListener) {
            val min = item.alarm.minutes
            val h = item.alarm.hours
            alarmTimeView.text = "${if(h < 10) "0$h" else "$h"}:${if(min < 10) "0$min" else "$min"}"

            var labelText = ""

            if(item.feedItem.isChecked) {
                medicationLabelView.typeface = Typeface.DEFAULT
            } else {
                medicationLabelView.typeface = Typeface.DEFAULT_BOLD
                labelText += "Uzmi lijek:"
            }
            medicationLabelView.text = "$labelText ${item.therapyAndMedication.medication.label}"
            doseView.text = "${item.therapyAndMedication.therapy.dosage} ${item.therapyAndMedication.medication.doseUnit}"

            if(item.feedItem.isChecked) {
                checkView.visibility = View.GONE
            } else {
                checkView.setOnClickListener {
                    feedItemListener.onFeedItemChecked(item.feedItem)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): FeedItemViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_item, parent, false)
                return FeedItemViewHolder(view)
            }
        }
    }

    class FeedItemComparator : DiffUtil.ItemCallback<FeedItemWithTherapyAndAlarms>() {
        override fun areItemsTheSame(
            oldItem: FeedItemWithTherapyAndAlarms,
            newItem: FeedItemWithTherapyAndAlarms
        ): Boolean {
            return oldItem.feedItem.id == newItem.feedItem.id
        }

        override fun areContentsTheSame(
            oldItem: FeedItemWithTherapyAndAlarms,
            newItem: FeedItemWithTherapyAndAlarms
        ): Boolean {
            return oldItem.therapyAndMedication.therapy.id == newItem.therapyAndMedication.therapy.id
        }

    }

    interface FeedItemListener {
        fun onFeedItemChecked(feedItem: FeedItem)
    }
}