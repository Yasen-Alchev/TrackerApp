package com.example.trackerapp.database.item

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    companion object{
        val FORMATTER = SimpleDateFormat("yyy-MM-dd")
    }
}