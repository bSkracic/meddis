package hr.bskracic.meddis.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import hr.bskracic.meddis.data.model.Alarm
import hr.bskracic.meddis.receivers.AlarmBroadcastReceiver
import java.util.*

class AlarmUtils {
    companion object {
        fun setAlarm(context: Context, alarmManager: AlarmManager, alarm: Alarm) {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            intent.action = "ReceiverAction"
            intent.putExtra("ID", alarm.therapyId)
            intent.putExtra("REQUEST_CODE", alarm.id)
            val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, alarm.hours)
            calendar.set(Calendar.MINUTE, alarm.minutes)

            val interval: Long = 1 * 60 * 1000

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                        pendingIntent
                    )
                }
            } else {
                // TODO: could be setInexactRepeating
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    interval,
                    pendingIntent
                )
            }

            Log.d("ALARM_CHECK",
                "Alarm id: ${alarm.id} -> Set time: ${if(alarm.hours < 10) "0${alarm.hours}" else "${alarm.hours}"}:${if(alarm.minutes < 10) "0${alarm.minutes}" else "${alarm.minutes}"}"
            )
        }

        fun removeAlarm(context: Context, alarmManager: AlarmManager, alarm: Alarm) {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)

            // So fucking stupid that I have repeat these steps
            intent.action = "ReceiverAction"
            intent.putExtra("ID", alarm.therapyId)
            intent.putExtra("REQUEST_CODE", alarm.id)
            val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)


            Log.d("ALARM_CHECK", "Alarm id: ${alarm.id} -> canceled")
        }
    }
}