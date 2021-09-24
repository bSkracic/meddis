package hr.bskracic.meddis.data.model

import androidx.room.*

@Entity(
    tableName = "therapies",
    foreignKeys = [
        ForeignKey(
            entity = Medication::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("medication_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = arrayOf("medication_id"))
    ]
)
data class Therapy(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "medication_id") var medicationId: Int,
    @ColumnInfo(name = "dosage") val dosage: Int
)