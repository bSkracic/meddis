package hr.bskracic.meddis.data.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "therapies")
data class Therapy (
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name="medication_id") val medicationId: Int,
        @ColumnInfo(name="dosage") val dosage: Int,
        @ColumnInfo(name="alarms") val alarms: String
        )