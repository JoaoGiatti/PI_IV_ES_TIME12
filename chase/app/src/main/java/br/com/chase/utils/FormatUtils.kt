package br.com.chase.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToLong

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

fun createBalloonBitmap(
    source: Bitmap,
    borderSize: Float = 8f,
    triangleHeight: Float = 35f,
    triangleWidth: Float = 55f
): Bitmap {
    val size = min(source.width, source.height)
    val totalHeight = size + triangleHeight.toInt()

    val output = Bitmap.createBitmap(size, totalHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val radius = size / 2f
    val centerX = size / 2f
    val centerY = radius

    paint.color = Color.BLACK
    canvas.drawCircle(centerX, centerY, radius, paint)

    val imageRect = RectF(
        borderSize,
        borderSize,
        size - borderSize,
        size - borderSize
    )

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    canvas.drawBitmap(source, null, imageRect, paint)
    paint.xfermode = null

    paint.color = -1170124
    val trianglePath = android.graphics.Path().apply {
        moveTo(centerX, size + triangleHeight)        // ponta do triângulo
        lineTo(centerX - triangleWidth / 2, size.toFloat()) // canto esquerdo
        lineTo(centerX + triangleWidth / 2, size.toFloat()) // canto direito
        close()
    }
    canvas.drawPath(trianglePath, paint)

    return output
}

fun calcularPace(distanciaMetros: Double, tempo: String): String {
    val partes = tempo.split(":")
    require(partes.size == 3) { "Formato de tempo inválido (esperado HH:mm:ss)" }

    val horas = partes[0].toDouble()
    val minutos = partes[1].toDouble()
    val segundos = partes[2].toDouble()

    val totalSegundos = horas * 3600 + minutos * 60 + segundos
    val distanciaKm = distanciaMetros / 1000.0
    require(distanciaKm > 0) { "Distância deve ser maior que zero" }

    val paceSegundos = totalSegundos / distanciaKm

    val paceMin = (paceSegundos / 60).toInt()
    val paceSec = (paceSegundos % 60).toInt()

    return String.format("%02d:%02d min/km", paceMin, paceSec)
}

fun formatCalories(calories: Double?): String {
    if (calories == null || calories.isNaN()) {
        return "-- Cal"
    }
    return "${calories.toInt()} Cal"
}

fun formatDistanceKm(meters: Double): String {
    val km = meters / 1000.0
    return String.format(Locale.getDefault(), "%.1f km", km)
}


fun formatTimeFromMillis(totalMillis: Double): String {
    val totalSeconds = (totalMillis / 1000.0).roundToLong()

    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return String.format(Locale.getDefault(), "%02d:%02d:%02d h", hours, minutes, seconds)
}


fun formatCaloriesKcal(calories: Double): String {
    return String.format(Locale.getDefault(), "%.2f kcal", calories)
}