package hr.bskracic.meddis.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import hr.bskracic.meddis.MainActivity
import hr.bskracic.meddis.R
import hr.bskracic.meddis.data.model.TherapyAndMedication
import hr.bskracic.meddis.receivers.TherapyActionReceiver

const val NOTIFICATION_ID = "NOTIFICATION_ID"

/**
 * Extension method for sending therapy notification in therapy notification channel.
 *
 * @param applicationContext context of the application
 * @param notificationId unique identifier of notification, identical to alarm's unique identifier
 * @param therapyAndMedication therapy to notify
 */
fun NotificationManager.sendTherapyNotification(applicationContext: Context, notificationId: Int, therapyAndMedication: TherapyAndMedication) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        notificationId,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val checkTherapyIntent = Intent(applicationContext, TherapyActionReceiver::class.java)
    checkTherapyIntent.action = "TherapyTakenAction"
    checkTherapyIntent.putExtra(NOTIFICATION_ID, notificationId)
    val checkTherapyPendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        0,
        checkTherapyIntent,
        PendingIntent.FLAG_ONE_SHOT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.therapy_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_therapy)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        .setContentTitle(therapyAndMedication.medication.label)
        .setContentText("Uzmi ${therapyAndMedication.therapy.dosage} ${therapyAndMedication.medication.doseUnit}!") // TODO: Add alarm time
        .setCategory("Meddis | Terapija")
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setVibrate(longArrayOf(0, 250, 250, 250))
        .addAction(
            R.drawable.ic_therapy,
            "Uzmi",
            checkTherapyPendingIntent
        )

        notify(notificationId, builder.build())

}
