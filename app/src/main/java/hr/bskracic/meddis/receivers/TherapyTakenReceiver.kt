package hr.bskracic.meddis.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import hr.bskracic.meddis.utils.NOTIFICATION_ID

class TherapyTakenReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action.equals("TherapyTakenAction")) {

            Log.d("ALARM_CHECK", "Therapy Receiver called")

            val alarmId = intent?.getIntExtra(NOTIFICATION_ID, 0)

            context?.let {
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                alarmId?.let {
                    notificationManager.cancel(it)
                    Log.d("ALARM_CHECK", "Alarm id: $it -> therapy taken")
                }
            }
        }
    }
}