package hr.bskracic.meddis.data.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "feeditems",
    foreignKeys = [
        ForeignKey(
            entity = Alarm::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("alarm_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Therapy::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("therapy_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = arrayOf("alarm_id")),
        Index(value = arrayOf("therapy_id"))
    ]
)
data class FeedItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "therapy_id") val therapyId: Int,
    @ColumnInfo(name = "alarm_id") val alarmId: Int,
    @ColumnInfo(name = "is_checked") var isChecked: Boolean,
    @ColumnInfo(name = "timestamp") val timestamp: Date
)