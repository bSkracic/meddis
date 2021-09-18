package hr.bskracic.meddis.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "therapies")
data class Therapy (
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name="medication_id") val medicationId: Int,
        @ColumnInfo(name="dosage") val dosage: Int,
        @ColumnInfo(name="collection_id") val collectionId: Int
        )