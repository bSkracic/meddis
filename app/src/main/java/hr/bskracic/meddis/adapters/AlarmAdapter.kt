package hr.bskracic.meddis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.data.model.RepeatType

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
            val min = alarm.minutes
            val h = alarm.hours
            timeTextView.text = "${if(h < 10) "0$h" else "$h"}:${if(min < 10) "0$min" else "$min"}"

            timeTextView.setOnClickListener {
                alarmListener.onEditTimeClicked(alarm)
            }

            deleteImageView.setOnClickListener {
                alarmListener.onDeleteClicked(alarm)
            }

            repeatTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var engName = ""
                    when(repeatTypeSpinner.selectedItem as String) {
                        "Dnevno" -> engName = "DAILY"
                        "Tjedno" -> engName = "WEEKLY"
                    }
                    alarm.repeatType = RepeatType.valueOf(engName)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

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
            return (oldItem.hours == newItem.hours) && (oldItem.minutes == newItem.minutes)
        }
    }

    interface AlarmListener {
        fun onDeleteClicked(alarm: Alarm)
        fun onEditTimeClicked(alarm: Alarm)
    }
}