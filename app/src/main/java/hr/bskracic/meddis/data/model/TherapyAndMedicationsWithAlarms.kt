package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TherapyAndMedicationsWithAlarms(
    @Embedded val therapy: Therapy,
    @Relation(
        parentColumn = "medication_id",
        entityColumn = "id"
    )
    val medication: Medication,
    @Relation(
        parentColumn = "id",
        entityColumn = "therapy_id"
    )
    val alarms: List<Alarm>
)