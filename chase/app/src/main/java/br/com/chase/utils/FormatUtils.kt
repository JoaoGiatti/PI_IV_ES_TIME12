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