package hr.bskracic.meddis.data.model

import androidx.room.TypeConverter

enum class RepeatType {
    DAILY,
    WEEKLY
}

class RepeatTypeConverter {
    @TypeConverter
    fun toRepeatType(value: String) = enumValueOf<RepeatType>(value)

    @TypeConverter
    fun fromRepeatType(value: RepeatType) = value.name
}