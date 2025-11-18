package br.com.chase.data.model

import com.google.android.gms.maps.model.LatLng

data class RouteRequest(
    val uid: String,
    val name: String,
    val description: String,
    val startLocation: String,
    val endLocation: String,
    val distance: Double,
    val recordTime: String,
    val points: List<LatLng>?
)

data class RouteResponse(
    val rid: String,
    val uid: String,
    val name: String,
    val description: String,
    val startLocation: String,
    val endLocation: String,

    val points: List<LatLng>,
    val distance: Double,
    val recordTime: String,
    val top3: List<Ranking>,
    val competitors: Int,

    val bestAverageSpeed: Double,
    val estimatedCalories: Double,
    val public: Boolean,
    val createdAt: String,
)

data class Ranking(
    val uid: String,
    val userName: String,
    val photoUrl: String?,
    val totalTime: String,
    val averageSpeed: Double
)

data class RouteAttemptRequest(
    val uid: String,
    val totalTime: String,
)