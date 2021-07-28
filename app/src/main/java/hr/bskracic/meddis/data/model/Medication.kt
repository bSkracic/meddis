package hr.bskracic.meddis.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name="label") val label: String,
    @ColumnInfo(name="description") val description: String?,
    @ColumnInfo(name="current_amount") val currentAmount: Int,
    @ColumnInfo(name="max_amount") val maxAmount: Int,
    @ColumnInfo(name="dose_unit") val doseUnit: String
    )