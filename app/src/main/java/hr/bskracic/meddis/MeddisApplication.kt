package hr.bskracic.meddis

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import hr.bskracic.meddis.data.MeddisDatabase
import hr.bskracic.meddis.repositories.AlarmRepository
import hr.bskracic.meddis.repositories.FeedItemRepository
import hr.bskracic.meddis.repositories.MedicationRepository
import hr.bskracic.meddis.repositories.TherapyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MeddisApplication : Application() {

    private val database by lazy { MeddisDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }

    val medicationRepository by lazy { MedicationRepository(database.medicationDao()) }
    val therapyRepository by lazy { TherapyRepository(database.therapyDao()) }
    val alarmRepository by lazy { AlarmRepository(database.alarmDao()) }
    val feedItemRepository by lazy { FeedItemRepository(database.feedItemDao()) }

    override fun onCreate() {
        super.onCreate()

        createChannel(
            getString(R.string.therapy_notification_channel_id),
            getString(R.string.therapy_notification_channel_name)
        )
    }

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationChannel.description = "Obavijesti o terapiji"

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

