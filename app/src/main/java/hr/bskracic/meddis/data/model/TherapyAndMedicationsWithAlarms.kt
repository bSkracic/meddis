package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TherapyAndMedicationsWithAlarms(
    @Embedded val therapyAndMedication: TherapyAndMedication,
    @Relation(
        parentColumn = "id",
        entityColumn = "therapy_id"
    )
    val alarms: List<Alarm>
)