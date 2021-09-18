package hr.bskracic.meddis.data.model

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toRepeatType(value: String) = enumValueOf<RepeatType>(value)

    @TypeConverter
    fun fromRepeatType(value: RepeatType) = value.name

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}