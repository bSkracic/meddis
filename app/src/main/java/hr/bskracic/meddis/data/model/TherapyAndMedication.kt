package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TherapyAndMedication(
    @Embedded val therapy: Therapy,
    @Relation(
        parentColumn = "medication_id",
        entityColumn = "id"
    )
    val medication: Medication
)