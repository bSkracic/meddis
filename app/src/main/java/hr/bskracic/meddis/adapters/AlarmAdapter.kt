package hr.bskracic.meddis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Alarm

class AlarmAdapter(private val alarmListener: AlarmListener) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(AlarmComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position), alarmListener)
    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val timeTextView = itemView.findViewById<TextView>(R.id.alarm_time)
        private val repeatTypeSpinner = itemView.findViewById<Spinner>(R.id.alarm_type)
        private val deleteImageView = itemView.findViewById<ImageView>(R.id.alarm_delete)

        fun bind(alarm: Alarm, alarmListener: AlarmListener) {
            timeTextView.text = alarm.time

            timeTextView.setOnClickListener {
                alarmListener.onEditTimeClicked(alarm)
            }

            deleteImageView.setOnClickListener {
                alarmListener.onDeleteClicked(alarm)
            }
        }

        companion object {
            fun create(parent: ViewGroup): AlarmViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_alarm, parent, false)
                return AlarmViewHolder(view)
            }
        }
    }

    class AlarmComparator : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.time == newItem.time
        }
    }

    interface AlarmListener {
        fun onDeleteClicked(alarm: Alarm)
        fun onEditTimeClicked(alarm: Alarm)
    }
}