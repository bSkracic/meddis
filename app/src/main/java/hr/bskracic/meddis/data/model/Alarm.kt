package hr.bskracic.meddis.data.model

import androidx.room.*

@Entity(
    tableName = "alarms",
    foreignKeys = [
        ForeignKey(
            entity = Therapy::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("therapy_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = arrayOf("therapy_id"))
    ]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "therapy_id") var therapyId: Int,
    @ColumnInfo(name = "hours") var hours: Int,
    @ColumnInfo(name = "minutes") var minutes: Int,
    @ColumnInfo(name = "repeat_type") var repeatType: RepeatType,
)