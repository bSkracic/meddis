package hr.bskracic.meddis.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name="therapy_id") val therapyId: Int,
    @ColumnInfo(name="time") var time: String,
    @ColumnInfo(name="repeat_type") var repeatType: RepeatType,
    )