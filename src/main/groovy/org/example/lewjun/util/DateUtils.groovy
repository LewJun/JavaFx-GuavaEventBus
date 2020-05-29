package org.example.lewjun.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DateUtils {
    static Date asDate(LocalDate localDate) {
        Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    }

    static Date asDate(LocalDateTime localDateTime) {
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    static LocalDate asLocalDate(Date date) {
        Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    static LocalDateTime asLocalDateTime(Date date) {
        Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    static long asLong(LocalDate localDate) {
        asDate(localDate).getTime()
    }

    static long asLong(LocalDateTime localDateTime) {
        asDate(localDateTime).getTime()
    }

    static LocalDate asLocalDate(long time) {
        Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    static LocalDateTime asLocalDateTime(long time) {
        Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

}
