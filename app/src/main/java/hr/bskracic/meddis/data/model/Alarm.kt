package hr.bskracic.meddis.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name="therapy_id") val therapyId: Int,
    @ColumnInfo(name="hours") var hours: Int,
    @ColumnInfo(name="minutes") var minutes: Int,
    @ColumnInfo(name="repeat_type") var repeatType: RepeatType,
    )