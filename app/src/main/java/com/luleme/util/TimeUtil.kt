package com.luleme.util

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

fun formatTimestampToTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val time = LocalTime.ofInstant(instant, ZoneId.systemDefault())
    return String.format("%02d:%02d", time.hour, time.minute)
}
