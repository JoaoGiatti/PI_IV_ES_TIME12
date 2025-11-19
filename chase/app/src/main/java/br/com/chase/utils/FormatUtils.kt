package br.com.chase.utils

fun formatElapsed(ms: Long): String {
    val total = ms / 1000
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}

fun formatDistance(meters: Double): String {
    return if (meters < 1000) {
        String.format("%.0f m", meters)
    } else {
        String.format("%.2f km", meters / 1000)
    }
}

fun formatAverageSpeed(kmh: Double): String {
    if (kmh.isNaN() || kmh.isInfinite() || kmh < 0) {
        return "0.00 km/h"
    }
    return String.format("%.2f km/h", kmh)
}

fun formatTotalTime(time: String): String {
    val parts = time.split(":")
    if (parts.size != 3) return time

    val hours = parts[0].toIntOrNull() ?: return time
    val minutes = parts[1].toIntOrNull() ?: return time
    val seconds = parts[2].toIntOrNull() ?: return time

    val mm = minutes.toString().padStart(2, '0')
    val ss = seconds.toString().padStart(2, '0')

    return if (hours == 0) {
        "0:$mm:$ss"
    } else {
        "${hours}:${mm}:${ss}"
    }
}