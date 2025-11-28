package br.com.chase.utils

import android.Manifest
import android.R
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showRankingNotification(context: Context) {
    val notification = NotificationCompat.Builder(context, "ranking_channel")
        .setSmallIcon(R.drawable.ic_dialog_info)
        .setContentTitle("Alerta!")
        .setContentText("Passaram você no ranking. Vai deixar barato?")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    NotificationManagerCompat.from(context).notify(1001, notification)
}