package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TherapyAndMedication(
    @Embedded val medication: Medication,
    @Relation(
        parentColumn = "id",
        entityColumn = "medication_id"
    )
    val therapy: Therapy
)