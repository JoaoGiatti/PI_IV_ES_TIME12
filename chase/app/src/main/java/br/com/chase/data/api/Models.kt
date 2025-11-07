package br.com.chase.data.api

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

data class UserRequest(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String? = ""
)

data class UserResponse(
    val uid: Long,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val bio: String?,
    val createdAt: String,
    val medals: List<String>,
    val totalCalories: Double,
    val totalDistance: Double,
    val totalTime: Double
)

fun FirebaseUser.toUserRequest() = UserRequest(
    uid = uid,
    email = email?.cleanUtf8(),
    displayName = displayName?.cleanUtf8(),
    photoUrl = photoUrl?.toString()?.cleanUtf8()
)

fun String.cleanUtf8() = this.replace("\u00A0", " ")


data class RouteRequest(
    val userId: String,
    val name: String,
    val description: String,
    val startLocation: String,
    val endLocation: String,
    val points: List<LatLng>
)

data class RouteResponse(
    val id: String,
    val creatorId: String,
    val name: String,
    val description: String,
    val distance: Double,
    val createdAt: String,
    val top3: List<String>
)

data class RouteAttemptRequest(
    val userId: String,
    val time: Double
)

data class RouteListResponse(val results: List<RouteResponse>)