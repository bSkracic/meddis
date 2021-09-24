package hr.bskracic.meddis.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.utils.sendTherapyNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("ALARM_CHECK", "BOOT_COMPLETED")
        }

        if (intent.action.equals("ReceiverAction")) {
            val therapyId = intent.getIntExtra("ID", 0)
            val alarmId = intent.getIntExtra("REQUEST_CODE", 0)
            Log.d("ALARM_CHECK", "Alarm id: $alarmId -> Therapy id: $therapyId")
            CoroutineScope(Dispatchers.IO).launch {
                context?.let {
                    val therapyAndMedication =
                        (it.applicationContext as MeddisApplication).therapyRepository.getByIdWithMedicationSync(
                            therapyId
                        )
                    val notificationManager = ContextCompat.getSystemService(
                        it,
                        NotificationManager::class.java
                    ) as NotificationManager
                    notificationManager.sendTherapyNotification(it, alarmId, therapyAndMedication)

                    (it.applicationContext as MeddisApplication).feedItemRepository.insert(
                        FeedItem(
                            0,
                            therapyId,
                            alarmId,
                            false,
                            Date()
                        )
                    )
                }
            }
        }
    }
}