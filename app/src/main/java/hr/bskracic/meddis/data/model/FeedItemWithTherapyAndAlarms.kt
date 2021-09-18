package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class FeedItemWithTherapyAndAlarms(
    @Embedded val feedItem: FeedItem,
    @Relation(
        parentColumn = "alarm_id",
        entityColumn = "id",
    )
    val alarm: Alarm,
    @Relation(
        entity = Therapy::class,
        parentColumn = "therapy_id",
        entityColumn = "id"
    )
    val therapyAndMedication: TherapyAndMedication
)