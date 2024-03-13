package dev.proptit.messenger.setup.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

object TimeUtils {
    fun format(dateLong: Long, format:String): String{
        val date = Date(dateLong)
        val timeFormatter = SimpleDateFormat(format, Locale.getDefault())
        return timeFormatter.format(date).toString()
    }

    fun convertFromStringToLong(dateString: String, format: String): Long{
        val timeFormatter = SimpleDateFormat(format, Locale.getDefault())
        return timeFormatter.parse(dateString)?.time ?: 0
    }

    fun currentTime(): Long = GregorianCalendar().time.time
}