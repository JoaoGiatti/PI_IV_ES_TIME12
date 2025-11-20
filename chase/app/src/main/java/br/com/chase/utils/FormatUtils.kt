package br.com.chase.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import kotlin.math.min

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