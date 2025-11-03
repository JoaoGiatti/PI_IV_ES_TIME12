package br.com.chase.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
fun locationUpdatesFlow(
    context: Context,
    intervalMs: Long = 3000L,
    fastestIntervalMs: Long = 1000L,
    priority: Int = Priority.PRIORITY_HIGH_ACCURACY
): Flow<Location> = callbackFlow {
    val client = LocationServices.getFusedLocationProviderClient(context)

    val request = LocationRequest.Builder(intervalMs)
        .setMinUpdateIntervalMillis(fastestIntervalMs)
        .setPriority(priority)
        .build()

    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { location ->
                // Adapta prioridade apartir da velocidade...
                val adaptivePriority = if (location.speed < 2f) {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                } else {
                    Priority.PRIORITY_HIGH_ACCURACY
                }
                client.setMockMode(false)
                trySend(location).isSuccess
            }
        }
    }

    client.requestLocationUpdates(request, callback, Looper.getMainLooper())
    awaitClose { client.removeLocationUpdates(callback) }
}
